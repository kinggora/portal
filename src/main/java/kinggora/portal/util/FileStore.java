package kinggora.portal.util;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.AttachFile;
import kinggora.portal.exception.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.storage}")
    private String storagePath;

    /**
     * 첨부 파일을 스토리지에 업로드
     *
     * @param postId        게시글 id
     * @param multipartFile 파일 폼 데이터
     * @return 업로드된 파일 정보 리스트
     */
    public AttachFile uploadFile(Integer postId, MultipartFile multipartFile) {
        String origFileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        String extension = extracted(origFileName);
        String storeFileName = createStoreFileName(extension);
        String storeDir = storagePath + File.separator;

        File saveFile = new File(storeDir, storeFileName);
        try {
            multipartFile.transferTo(saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return AttachFile.builder()
                .postId(postId)
                .origName(origFileName)
                .storeName(storeFileName)
                .ext(extension)
                .size(size)
                .storeDir(storeDir)
                .build();
    }

    /**
     * 스토리지 파일 삭제
     *
     * @param attachFiles 파일 정보 리스트
     */
    public void deleteFiles(List<AttachFile> attachFiles) {
        for (AttachFile attachFile : attachFiles) {
            File file = new File(attachFile.getStoreDir() + attachFile.getStoreName());
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
     * @param attachFile 파일 정보
     * @return 파일 input stream resource
     */
    public InputStreamResource loadAsResource(AttachFile attachFile) {
        File file = new File(attachFile.getStoreDir(), attachFile.getStoreName());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }
        return new InputStreamResource(fileInputStream);
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
