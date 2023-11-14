package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.dto.FileDto;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.FileRepository;
import kinggora.portal.util.FileStore;
import kinggora.portal.util.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;
    private final ThumbnailUtil thumbnailUtil;

    /**
     * 파일 업로드 및 메타 데이터 저장
     */
    public void saveFiles(int postId, FileDto dto) {
        // in store
        List<UploadFile> uploadedFiles = uploadFiles(postId, dto);

        // in database
        if (!uploadedFiles.isEmpty()) {
            fileRepository.saveFiles(uploadedFiles);
        }
    }

    /**
     * 첨부 파일 단건 조회
     *
     * @param id 파일 id
     * @return
     */
    public UploadFile findFileById(int id) {
        return fileRepository.findFileById(id)
                .orElseThrow(() -> new BizException(ErrorCode.FILE_NOT_FOUND));
    }

    /**
     * 게시글 첨부 파일 조회
     *
     * @param postId 게시글 id
     * @return
     */
    public List<UploadFile> findFiles(int postId) {
        return fileRepository.findFiles(postId);
    }

    public List<UploadFile> findThumbnails(List<Integer> postIds) {
        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }
        return fileRepository.findFilesOfType(postIds, FileType.THUMBNAIL);
    }

    /**
     * 게시글 첨부 파일 삭제
     *
     * @param postId 게시글 id
     * @return
     */
    public void deleteFiles(int postId) {
        List<UploadFile> files = fileRepository.findFiles(postId);
        fileStore.deleteFiles(files);
    }

    /**
     * 파일 다운로드
     *
     * @param storeFileName 실제 저장된 파일명
     * @return 파일 input stream resource
     */
    public Resource loadAsResource(String storeFileName) {
        return fileStore.loadAsResource(storeFileName);
    }

    private List<UploadFile> uploadFiles(int postId, FileDto dto) {
        List<UploadFile> uploadedFiles = new ArrayList<>();

        // attachment files
        List<MultipartFile> attachmentFiles = dto.getAttachment();
        for (MultipartFile attachmentFile : attachmentFiles) {
            if (!attachmentFile.isEmpty() && attachmentFile.getSize() > 0) {
                uploadedFiles.add(uploadFile(attachmentFile, postId, FileType.ATTACHMENT));
            }
        }
        // content files
        List<MultipartFile> contentFiles = dto.getContent();
        for (MultipartFile contentFile : contentFiles) {
            if (!contentFile.isEmpty() && contentFile.getSize() > 0 && checkImageFile(contentFile.getOriginalFilename())) {
                uploadedFiles.add(uploadFile(contentFile, postId, FileType.CONTENT));
            }
        }
        // thumbnail file
        Optional<MultipartFile> thumbnailFile = contentFiles.stream()
                .filter(file -> !file.isEmpty() && file.getSize() > 0 && checkImageFile(file.getOriginalFilename())).findFirst();
        thumbnailFile.ifPresent(multipartFile -> uploadedFiles.add(uploadThumbFile(multipartFile, postId)));

        return uploadedFiles;
    }

    private UploadFile uploadFile(MultipartFile file, int postId, FileType type) {
        String origFileName = file.getOriginalFilename();
        String extension = extracted(origFileName);
        String storeFileName = createStoreFileName(extension);
        long size = file.getSize();

        fileStore.uploadFile(file, storeFileName);
        String storeDir = type.equals(FileType.CONTENT) ? fileStore.getUrl(storeFileName) : null;

        return UploadFile.builder()
                .postId(postId)
                .origName(origFileName)
                .storeDir(storeDir)
                .storeName(storeFileName)
                .ext(extension)
                .size(size)
                .type(type)
                .build();
    }

    private UploadFile uploadThumbFile(MultipartFile origFile, int postId) {
        byte[] thumbnail = thumbnailUtil.createThumbnail(origFile);
        String extension = ThumbnailUtil.THUMB_EXT;
        String storeFileName = createStoreFileName(extension);
        long size = thumbnail.length;

        fileStore.uploadThumbFile(thumbnail, storeFileName);
        String storeDir = fileStore.getUrl(storeFileName);

        return UploadFile.builder()
                .postId(postId)
                .storeDir(storeDir)
                .storeName(storeFileName)
                .ext(extension)
                .size(size)
                .type(FileType.THUMBNAIL)
                .build();
    }

    /**
     * 스토리지에 저장될 이름 생성
     *
     * @param ext 확장자
     * @return 파일 이름
     */
    private String createStoreFileName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 이름에서 확장자(ext) 추출
     *
     * @param fileName 파일 이름
     * @return 확장자
     */
    private String extracted(String fileName) {
        if (fileName != null) {
            int pos = fileName.lastIndexOf(".");
            return fileName.substring(pos + 1);
        }
        return null;
    }

    /**
     * 이미지 파일인지 확인
     *
     * @param fileName 파일명
     * @return true: image file, false: image file X
     */
    private boolean checkImageFile(String fileName) {
        File checkFile = new File(fileName);
        String type = null;
        try {
            type = Files.probeContentType(checkFile.toPath());
        } catch (IOException e) {
            log.error("FileStore.checkImageFile", e);
        }
        return type != null && type.startsWith("image");
    }

//    public List<FileInfo> getFileInfos(List<Integer> postIds) {
//        List<FileInfo> fileInfos = new ArrayList<>();
//        for (int postId : postIds) {
//            fileInfos.add(createFileInfo(postId));
//        }
//        return fileInfos;
//    }
//
//    private FileInfo createFileInfo(int postId) {
//        List<UploadFile> files = findFiles(postId);
//        boolean attached = findFileOfType(files, FileType.ATTACHMENT).isPresent();
//        boolean imaged = findFileOfType(files, FileType.CONTENT).isPresent();
//        Optional<UploadFile> thumbnail = findFileOfType(files, FileType.THUMBNAIL);
//        return FileInfo.builder()
//                .attached(attached)
//                .imaged(imaged)
//                .thumbnail(thumbnail.orElse(null))
//                .build();
//    }
//
//    private Optional<UploadFile> findFileOfType(List<UploadFile> files, FileType type) {
//        return files.stream()
//                .filter(file -> file.getType().equals(type)).findFirst();
//    }

}
