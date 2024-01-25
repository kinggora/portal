package kinggora.portal.service;

import io.jsonwebtoken.lang.Collections;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.data.request.FileDto;
import kinggora.portal.repository.FileRepository;
import kinggora.portal.util.FileValidator;
import kinggora.portal.util.S3FileStore;
import kinggora.portal.util.ThumbnailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final S3FileStore s3FileStore;
    private final ThumbnailUtil thumbnailUtil;
    private final FileValidator fileValidator;

    /**
     * 파일 업로드 및 메타데이터 저장
     * 1. 유효하지 않은 파일 제거
     * 2. 파일 업로드 -> 메타 데이터 생성
     * 3. 메타 데이터 DB 저장
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
     * @return 파일 메타 데이터
     * @throws BizException 조회한 파일이 존재하지 않거나 이미 삭제되었을 때 발생
     */
    public UploadFile findFileById(int id) {
        UploadFile uploadFile = fileRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.FILE_NOT_FOUND));
        if (uploadFile.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_FILE);
        }
        return uploadFile;
    }

    /**
     * 게시글 첨부 파일 조회
     *
     * @param postId 게시글 id
     * @return 게시글에 대한 첨부파일 리스트
     */
    public List<UploadFile> findFilesByPostId(int postId) {
        return fileRepository.findByPostId(postId);
    }

    /**
     * 파일 삭제
     * 1. DB에서 메타 데이터 삭제
     * 2. 스토리지에서 파일 삭제
     * 삭제할 파일의 type이 CONTENT 인 경우 썸네일도 함께 삭제
     *
     * @param id 파일 id
     */
    public void deleteFile(int id) {
        UploadFile file = findFileById(id);
        if (FileType.CONTENT.equals(file.getType())) {
            Optional<UploadFile> thumbFile = findThumbFile(file);
            if (thumbFile.isPresent()) {
                fileRepository.deleteById(thumbFile.get().getId());
                s3FileStore.deleteFile(thumbFile.get().getStoreName());
            }
        }
        // Deleting metadata from database
        fileRepository.deleteById(file.getId());
        // Deleting file from storage
        s3FileStore.deleteFile(file.getStoreName());
    }

    /**
     * 게시글 첨부 파일 삭제
     * 1. 게시글 id로 게시글에 첨부된 파일 모두 조회
     * 2. 스토리지 파일 삭제
     * 3. 파일 메타 데이터 삭제
     *
     * @param postId 게시글 id
     */
    public void deleteFilesByPostId(int postId) {
        List<UploadFile> files = fileRepository.findByPostId(postId);
        if (!files.isEmpty()) {
            // Deleting file from storage
            for (UploadFile file : files) {
                s3FileStore.deleteFile(file.getStoreName());
            }
            // Deleting metadata from database
            fileRepository.deleteByPostId(postId);
        }
    }

    /**
     * 파일을 byte array로 변환
     *
     * @param fileName 변환할 파일명
     * @return byte array
     */
    public byte[] loadAsBytes(String fileName) {
        return s3FileStore.loadAsBytes(fileName);
    }

    /**
     * 원본 파일에 대한 썸네일 파일 찾기
     * sourceFile 이 첨부된 게시글의 첨부 파일 리스트 조회
     * thumbFile.origName == sourceFile.storeName
     *
     * @param sourceFile 썸네일의 원본 파일
     * @return sourceFile에 대한 썸네일 파일
     */
    private Optional<UploadFile> findThumbFile(UploadFile sourceFile) {
        List<UploadFile> files = findFilesByPostId(sourceFile.getPostId());
        for (UploadFile file : files) {
            if (FileType.THUMBNAIL.equals(file.getType()) && file.getOrigName().equals(sourceFile.getStoreName())) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

    /**
     * 첨부 파일 업로드 및 메타 데이터 생성
     *
     * @param postId 게시글 id
     * @param files  업로드할 파일
     * @return 업로드 파일의 메타 데이터 리스트
     */
    private List<UploadFile> uploadAttachFiles(int postId, List<MultipartFile> files) {
        List<UploadFile> uploadFiles = new ArrayList<>();
        if (!Collections.isEmpty(files)) {
            for (MultipartFile file : files) {
                UploadFile uploadFile = uploadFile(file, postId, FileType.ATTACHMENT);
                if (uploadFile != null) {
                    uploadFiles.add(uploadFile);
                }
            }
        }
        return uploadFiles;
    }

    /**
     * 이미지 파일 업로드 요청 및 메타 데이터 생성
     * 업로드 성공한 이미지 파일에 대한 썸네일 파일 생성 및 업로드
     * 업로드 한 파일의 메타데이터 리스트 생성
     *
     * @param postId 게시글 id
     * @param files  업로드 할 파일
     * @return 이미지 파일의 메타 데이터 리스트
     */
    private List<UploadFile> uploadImageFiles(int postId, List<MultipartFile> files) {
        List<UploadFile> uploadFiles = new ArrayList<>();
        if (!Collections.isEmpty(files)) {
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

    /**
     * 실제 파일 업로드 및 메타 데이터 생성
     * MultipartFile의 OriginalFilename로 유효한 파일명 생성
     * 파일 업로드 성공 시 업로드 한 파일의 메타 데이터(UploadFile) 생성
     *
     * @param file   파일 폼 데이터
     * @param postId 게시글 id
     * @param type   파일 Type
     * @return 업로드 파일의 메타 데이터
     */
    private UploadFile uploadFile(MultipartFile file, int postId, FileType type) {
        String origFileName = fileValidator.getValidFileName(file.getOriginalFilename());
        String extension = extracted(origFileName);
        String storeName = createStoreFileName(extension);
        if (!s3FileStore.uploadFile(file, storeName)) {
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

    /**
     * fileType 따라 적합한 URL 반환
     *
     * @param fileType 파일 타입
     * @param fileName 파일명
     * @return URL string
     */
    private String getUrl(FileType fileType, String fileName) {
        switch (fileType) {
            case ATTACHMENT:
                return "";
            case THUMBNAIL:
            case CONTENT:
                return s3FileStore.getUrl(fileName);
        }
        return null;
    }

    /**
     * 스토리지에 저장될 이름 생성
     * 랜덤하게 생성한 UUID 사용
     *
     * @param ext 확장자
     * @return 파일 이름
     */
    private String createStoreFileName(String ext) {
        if (StringUtils.isEmpty(ext)) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 이름에서 확장자(ext) 추출
     * 파일명이 null 이거나 빈 문자열인 경우, '.'이 없는 경우 null 반환
     * '.' 위치 기반으로 확장자 추출
     *
     * @param fileName 파일 이름
     * @return 추출한 확장자 or null
     */
    private String extracted(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }
        return fileName.substring(pos + 1);
    }
}
