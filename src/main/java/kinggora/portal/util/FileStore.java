package kinggora.portal.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.UploadFile;
import kinggora.portal.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.cloudfront.domain}")
    private String cloudFrontDomain;
    private final AmazonS3 amazonS3;

    /**
     * 파일을 스토리지에 업로드
     * 파일을 stream 으로 변환하여 PutObjectRequest 생성
     *
     * @param multipartFile 파일 폼 데이터
     * @param fileName      파일명
     * @return 업로드 성공 여부
     */
    public boolean uploadFile(MultipartFile multipartFile, String fileName) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putRequest = createPutObjectRequest(fileName, inputStream, multipartFile.getContentType(), multipartFile.getSize());
            amazonS3.putObject(putRequest);
        } catch (SdkClientException | IOException e) {
            log.error("파일 업로드 실패... filename={}", fileName, e);
            return false;
        }
        return true;
    }

    /**
     * 파일을 스토리지에 업로드
     * byte array 를 stream 으로 변환하여 PutObjectRequest 생성
     *
     * @param bytes       file bytes
     * @param fileName    파일명
     * @param contentType http header content-type value
     */
    public void uploadFile(byte[] bytes, String fileName, String contentType) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            PutObjectRequest putRequest = createPutObjectRequest(fileName, inputStream, contentType, bytes.length);
            amazonS3.putObject(putRequest);
        } catch (SdkClientException | IOException e) {
            log.error("파일 업로드 실패... filename={}", fileName, e);
        }
    }

    /**
     * 스토리지에 저장된 자원의 url 반환
     *
     * @param fileName 파일명
     * @return url string
     */
    public String getUrl(String fileName) {
        return "https://" + cloudFrontDomain + "/" + fileName;
    }

    /**
     * 스토리지 복수 파일 삭제
     *
     * @param uploadFiles 파일 정보 리스트
     */
    public void deleteAll(List<UploadFile> uploadFiles) {
        for (UploadFile uploadFile : uploadFiles) {
            deleteFile(uploadFile.getStoreName());
        }
    }

    /**
     * 스토리지 단일 파일 삭제
     *
     * @param storeFileName 파일이 실제 저장된 이름
     */
    public void deleteFile(String storeFileName) {
        try {
            if (fileExists(storeFileName)) {
                amazonS3.deleteObject(bucket, storeFileName);
            } else {
                log.error("FileStore.deleteFile={}, filename={}", ErrorCode.FILE_NOT_FOUND.getDefaultMessage(), storeFileName);
            }
        } catch (SdkClientException e) {
            log.error("FileStore.deleteFile", e);
        }
    }

    /**
     * 저장소의 파일을 InputStreamResource 로 반환
     * 해당 파일이 존재하지 않으면 FILE_NOT_FOUND 예외 반환
     *
     * @param storeFileName 실제 저장된 파일명
     * @return 파일 input stream resource
     */
    public Resource loadAsResource(String storeFileName) {
        S3Object file;
        try {
            if (fileExists(storeFileName)) {
                file = amazonS3.getObject(bucket, storeFileName);
            } else {
                throw new BizException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (SdkClientException e) {
            log.error("FileStore.loadAsResource={}, filename={}", e.getMessage(), storeFileName);
            throw new BizException(ErrorCode.S3_ERROR, "파일 다운로드 실패");
        }
        S3ObjectInputStream objectContent = file.getObjectContent();
        return new InputStreamResource(objectContent);
    }

    public byte[] loadAsBytes(String storeFileName) {
        S3Object s3Object;
        try {
            if (fileExists(storeFileName)) {
                s3Object = amazonS3.getObject(bucket, storeFileName);
            } else {
                throw new BizException(ErrorCode.FILE_NOT_FOUND);
            }
            return IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (SdkClientException | IOException e) {
            log.error("FileStore.loadAsBytes={}, filename={}", e.getMessage(), storeFileName);
            throw new BizException(ErrorCode.S3_ERROR, "파일 다운로드 실패");
        }

    }

    /**
     * 파라미터 기반 PutObjectRequest 객체 생성
     *
     * @param fileName      파일명
     * @param inputStream   file input stream
     * @param contentType   http header content-type value
     * @param contentLength http header content-length value
     * @return PutObjectRequest 객체
     */
    private PutObjectRequest createPutObjectRequest(String fileName, InputStream inputStream, String contentType, long contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);
        return new PutObjectRequest(bucket, fileName, inputStream, metadata);
    }

    /**
     * 스토리지 파일 존재 여부
     *
     * @param fileName 탐색할 파일명
     * @return 파일 존재 여부
     */
    public boolean fileExists(String fileName) {
        return amazonS3.doesObjectExist(bucket, fileName);
    }

}
