package kinggora.portal.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class FileValidator {
    @Value("${file.allowed-mime-types}")
    private String[] ALLOWED_MIME_TYPES;
    @Value("${file.maximum-filename-length}")
    private int MAXIMUM_FILENAME_LENGTH;

    public boolean isValidAttachFile(MultipartFile file) {
        log.info("isValidAttachFile={}", file.getOriginalFilename());
        if (!isValidFile(file)) {
            return false;
        }
        String mimeType = detectMIMEType(file);
        if (!isAllowedMIMEType(mimeType)) {
            return false;
        }
        return true;
    }

    public boolean isValidImageFile(MultipartFile file) {
        log.info("isValidImageFile={}", file.getOriginalFilename());
        if (!isValidFile(file)) {
            return false;
        }
        String mimeType = detectMIMEType(file);
        if (!isAllowedMIMEType(mimeType) || !isImageMimeType(mimeType)) {
            return false;
        }
        return true;
    }

    /**
     * 유효한 파일명 반환
     * - 파일명에 사용할 수 없는 특수문자를 정규 표현식을 통해 제거 -> 결과가 빈 문자열이라면 UUID 사용
     * - MAXIMUM_FILENAME_LENGTH 보다 길이가 큰 경우 잘라서 반환
     *
     * @param fileName 파일명
     * @return valid file name
     */
    public String getValidFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        final String illegalExp = "[\\\\/:%*?\"<>|;]";
        String temp = fileName.replaceAll(illegalExp, "");
        if (temp.length() == 0) {
            temp = UUID.randomUUID().toString();
        } else if (temp.length() > MAXIMUM_FILENAME_LENGTH) {
            temp = temp.substring(0, MAXIMUM_FILENAME_LENGTH);
        }
        return temp;
    }

    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getSize() > 0 && file.getOriginalFilename() != null;
    }

    /**
     * 이미지 파일인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: image file, false: image file X
     */
    private boolean isImageMimeType(String mimeType) {
        if (mimeType != null) {
            return mimeType.startsWith("image");
        }
        return false;
    }

    /**
     * 허용하는 MIME Type 인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: allowed MIME type, false: disallow MIME type
     */
    private boolean isAllowedMIMEType(String mimeType) {
        if (mimeType != null) {
            for (String allowedMimeType : ALLOWED_MIME_TYPES) {
                if (mimeType.equals(allowedMimeType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 파일의 MIME Type 판별
     *
     * @param file 업로드 파일
     * @return 파일의 MIME Type
     */
    private String detectMIMEType(MultipartFile file) {
        Tika tika = new Tika();
        String mimeType;
        try (InputStream inputStream = file.getInputStream()) {
            mimeType = tika.detect(inputStream);
        } catch (IOException e) {
            log.error("FileValidator.detectMIMEType", e);
            return null;
        }
        log.info("MultipartFile.getContentType={}", file.getContentType());
        log.info("Tika.detect={}", mimeType);
        return mimeType;
    }

}
