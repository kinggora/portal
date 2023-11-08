package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.dto.FileDto;
import kinggora.portal.domain.dto.FileInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<UploadFile> findThumbnails(List<Integer> postIds) {
        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }
        return fileRepository.findFilesOfType(postIds, FileType.THUMBNAIL);
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
     * @param file 파일 정보
     * @return 파일 input stream resource
     */
    public Resource loadAsResource(UploadFile file) {
        return fileStore.loadAsResource(file);
    }

    private List<UploadFile> uploadFiles(int postId, FileDto dto) {
        List<UploadFile> uploadedFiles = new ArrayList<>();

        // attachment files
        List<MultipartFile> attachmentFiles = dto.getAttachment();
        for (MultipartFile attachmentFile : attachmentFiles) {
            if (!attachmentFile.isEmpty() && attachmentFile.getSize() > 0) {
                uploadedFiles.add(fileStore.uploadFile(attachmentFile, postId, FileType.ATTACHMENT));
            }
        }
        // content files
        List<MultipartFile> contentFiles = dto.getContent();
        for (MultipartFile contentFile : contentFiles) {
            if (!contentFile.isEmpty() && contentFile.getSize() > 0 && fileStore.checkImageFile(contentFile)) {
                uploadedFiles.add(fileStore.uploadFile(contentFile, postId, FileType.CONTENT));
            }
        }
        // thumbnail file
        Optional<UploadFile> firstContentFile = uploadedFiles.stream()
                .filter(file -> file.getType().equals(FileType.CONTENT)).findFirst();
        if (firstContentFile.isPresent()) {
            uploadedFiles.add(thumbnailUtil.createThumbnail(firstContentFile.get()));
        }

        return uploadedFiles;
    }

    public List<FileInfo> getFileInfos(List<Integer> postIds) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (int postId : postIds) {
            fileInfos.add(createFileInfo(postId));
        }
        return fileInfos;
    }

    private FileInfo createFileInfo(int postId) {
        List<UploadFile> files = findFiles(postId);
        boolean attached = findFileOfType(files, FileType.ATTACHMENT).isPresent();
        boolean imaged = findFileOfType(files, FileType.CONTENT).isPresent();
        Optional<UploadFile> thumbnail = findFileOfType(files, FileType.THUMBNAIL);
        return FileInfo.builder()
                .attached(attached)
                .imaged(imaged)
                .thumbnail(thumbnail.orElse(null))
                .build();
    }

    private Optional<UploadFile> findFileOfType(List<UploadFile> files, FileType type) {
        return files.stream()
                .filter(file -> file.getType().equals(type)).findFirst();
    }

}
