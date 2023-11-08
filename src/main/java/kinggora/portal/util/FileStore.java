package kinggora.portal.util;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${file.storage}")
    private String[] storagePath;

    /**
     * 첨부 파일을 스토리지에 업로드
     *
     * @param postId        게시글 id
     * @param multipartFile 파일 폼 데이터
     * @return 업로드된 파일 정보 리스트
     */
    public UploadFile uploadFile(MultipartFile multipartFile, int postId, FileType type) {
        String origFileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        String extension = extracted(origFileName);
        String storeFileName = createStoreFileName(extension);
        String storeDir = Paths.get("C:", storagePath) + File.separator;
        File saveFile = new File(storeDir, storeFileName);
        try {
            multipartFile.transferTo(saveFile);
        } catch (IOException e) {
            log.error("FileStore.uploadFile()", e);
        }

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

    /**
     * 스토리지 파일 삭제
     *
     * @param uploadFiles 파일 정보 리스트
     */
    public void deleteFiles(List<UploadFile> uploadFiles) {
        for (UploadFile uploadFile : uploadFiles) {
            File file = new File(uploadFile.getStoreDir() + uploadFile.getStoreName());
            if (file.exists()) {
                boolean result = file.delete();
                if (!result) {
                    throw new RuntimeException("fail FileStore.deleteFiles");
                }
            }
        }
    }

    /**
     * 저장소의 파일을 InputStreamResource 로 반환
     * 해당 파일이 존재하지 않으면 FILE_NOT_FOUND 예외 반환
     *
     * @param uploadFile 파일 정보
     * @return 파일 input stream resource
     */
    public InputStreamResource loadAsResource(UploadFile uploadFile) {
        File file = new File(uploadFile.getStoreDir(), uploadFile.getStoreName());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }
        return new InputStreamResource(fileInputStream);
    }

    public boolean checkImageFile(MultipartFile file) {
        File checkFile = new File(file.getOriginalFilename());
        String type = null;
        try {
            type = Files.probeContentType(checkFile.toPath());
        } catch (IOException e) {
            log.error("FileStore.checkImageFile", e);
        }
        return type != null && type.startsWith("image");
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
}
