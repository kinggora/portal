package kinggora.portal.util;

import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 파일 업로드 요청에서 Multipart File에 대한 검증기
 * 널 체크, MIME Type 체크, 유효한 파일명 생성
 */
@Slf4j
@Component
public class FileValidator {
    @Value("${file.allowed-mime-types}")
    private String[] ALLOWED_MIME_TYPES;
    @Value("${file.maximum-filename-length}")
    private int MAXIMUM_FILENAME_LENGTH;

    /**
     * 첨부 파일에 대한 유효성 검증
     * 1. 유효한 파일인지
     * 2. 업로드 허용 MIME Type 인지
     *
     * @param file 검증할 MultipartFile
     * @return true: 유효한 파일, false: 유효하지 않은 파일
     */
    public boolean isValidAttachFile(MultipartFile file) {
        if (!isValidFile(file)) {
            return false;
        }
        String mimeType = detectMIMEType(file);
        if (!isAllowedMIMEType(mimeType)) {
            return false;
        }
        return true;
    }

    /**
     * 이미지 파일에 대한 유효성 검증
     * 1. 유효한 파일인지
     * 2. 업로드 허용 MIME Type 인지
     * 3. 이미지 MIME Type 인지
     *
     * @param file 검증할 MultipartFile
     * @return true: 유효한 이미지 파일, false: 유효하지 않은 파일
     */
    public boolean isValidImageFile(MultipartFile file) {
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
     * - 원 파일명이 null 이라면 null 반환
     *
     * @param fileName 파일명
     * @return 유효한 파일명 or null
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

    /**
     * 유효한 파일인지 확인
     * 1. null 체크
     * 2. empty 체크
     * 3. size > 0 체크
     * 4. 원본 파일이름 null 체크
     *
     * @param file
     * @return
     */
    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getSize() > 0 && file.getOriginalFilename() != null;
    }

    /**
     * MIME Type이 이미지 타입인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: Image MIME Type, false: Image MIME Type 아님
     */
    private boolean isImageMimeType(String mimeType) {
        if (!Strings.hasText(mimeType)) {
            return false;
        }
        return mimeType.startsWith("image");
    }

    /**
     * 허용하는 MIME Type 인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: allowed MIME type, false: disallow MIME type
     */
    private boolean isAllowedMIMEType(String mimeType) {
        if (Strings.hasText(mimeType)) {
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
     * Tika 라이브러리 사용
     *
     * @param file MIME Type을 판별한 MultipartFile
     * @return 판별된 MIME Type
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
        return mimeType;
    }

}
