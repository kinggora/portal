package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.dto.request.FileDto;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.FileRepository;
import kinggora.portal.util.FileStore;
import kinggora.portal.util.FileValidator;
import kinggora.portal.util.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;
    private final ThumbnailUtil thumbnailUtil;
    private final FileValidator fileValidator;

    /**
     * 파일 업로드 및 메타데이터 저장
     *
     * @param postId 게시글 id
     * @param dto    파일 form 데이터 (첨부 파일, 이미지 파일)
     */
    public void saveFiles(int postId, FileDto dto) {
        // Uploading file to storage
        List<MultipartFile> attachment = dto.getAttachment();
        attachment.removeIf(file -> !fileValidator.isValidAttachFile(file));
        List<UploadFile> uploadAttachFiles = uploadAttachFiles(postId, attachment);

        List<MultipartFile> content = dto.getContent();
        content.removeIf(file -> !fileValidator.isValidImageFile(file));
        List<UploadFile> uploadImageFiles = uploadImageFiles(postId, content);

        List<UploadFile> result = Stream.concat(uploadAttachFiles.stream(), uploadImageFiles.stream())
                .collect(Collectors.toList());

        // Saving metadata to database
        if (!result.isEmpty()) {
            fileRepository.saveFiles(result);
        }
    }

    /**
     * 첨부 파일 단건 조회
     *
     * @param id 파일 id
     * @return
     */
    public UploadFile findFileById(int id) {
        Optional<UploadFile> optional = fileRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }
        UploadFile uploadFile = optional.get();
        if (uploadFile.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_FILE);
        }
        return uploadFile;
    }

    /**
     * 게시글 첨부 파일 조회
     *
     * @param postId 게시글 id
     * @return
     */
    public List<UploadFile> findFilesByPostId(int postId) {
        return fileRepository.findByPostId(postId);
    }

    /**
     * 파일 삭제
     * 삭제할 파일의 type이 CONTENT 인 경우 썸네일도 함께 삭제
     *
     * @param id 파일 id
     * @return
     */
    public void deleteFile(int id) {
        UploadFile file = findFileById(id);
        if (file.getType() == FileType.CONTENT) {
            deleteThumbFile(file);
        }
        // Deleting metadata from database
        fileRepository.deleteById(file.getId());
        // Deleting file from storage
        fileStore.deleteFile(file.getStoreName());
    }

    /**
     * 게시글 첨부 파일 삭제
     *
     * @param postId 게시글 id
     * @return
     */
    public void deleteFilesByPostId(int postId) {
        List<UploadFile> files = fileRepository.findByPostId(postId);
        if (!files.isEmpty()) {
            // Deleting metadata from database
            fileRepository.deleteByPostId(postId);
            // Deleting file from storage
            fileStore.deleteAll(files);
        }
    }

    public void deleteThumbFile(UploadFile sourceFile) {
        List<UploadFile> files = findFilesByPostId(sourceFile.getPostId());
        for (UploadFile file : files) {
            if (FileType.THUMBNAIL.equals(file.getType()) && file.getOrigName().equals(sourceFile.getStoreName())) {
                fileRepository.deleteById(file.getId());
                fileStore.deleteFile(file.getStoreName());
                break;
            }
        }
    }

    private List<UploadFile> uploadAttachFiles(int postId, List<MultipartFile> files) {
        List<UploadFile> uploadFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            UploadFile uploadFile = uploadFile(file, postId, FileType.ATTACHMENT);
            if (uploadFile != null) {
                uploadFiles.add(uploadFile);
            }
        }
        return uploadFiles;
    }

    private List<UploadFile> uploadImageFiles(int postId, List<MultipartFile> files) {
        List<UploadFile> uploadFiles = new ArrayList<>();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                UploadFile imageFile = uploadFile(file, postId, FileType.CONTENT);
                if (imageFile != null) {
                    uploadFiles.add(imageFile);
                    UploadFile thumbFile = thumbnailUtil.uploadThumbFile(file, imageFile);
                    uploadFiles.add(thumbFile);
                }
            }
        }
        return uploadFiles;
    }

    private UploadFile uploadFile(MultipartFile file, int postId, FileType type) {
        String origFileName = fileValidator.getValidFileName(file.getOriginalFilename());
        String extension = extracted(origFileName);
        String storeName = createStoreFileName(extension);
        if (!fileStore.uploadFile(file, storeName)) {
            return null;
        }
        long size = file.getSize();
        String url = getUrl(type, storeName);
        return UploadFile.builder()
                .postId(postId)
                .origName(origFileName)
                .url(url)
                .storeName(storeName)
                .ext(extension)
                .size(size)
                .url(url)
                .type(type)
                .regDate(LocalDateTime.now())
                .build();
    }

    private String getUrl(FileType fileType, String fileName) {
        switch (fileType) {
            case ATTACHMENT:
                return "";
            case THUMBNAIL:
            case CONTENT:
                return fileStore.getUrl(fileName);
        }
        return null;
    }

    /**
     * 스토리지에 저장될 이름 생성
     *
     * @param ext 확장자
     * @return 파일 이름
     */
    private String createStoreFileName(String ext) {
        if (ext == null) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 이름에서 확장자(ext) 추출
     *
     * @param fileName 파일 이름
     * @return 확장자
     */
    private String extracted(String fileName) {
        if (fileName == null) {
            return null;
        }
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }
        return fileName.substring(pos + 1);
    }

    public Resource loadAsResource(String fileName) {
        return fileStore.loadAsResource(fileName);
    }

}
