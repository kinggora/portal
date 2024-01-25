package kinggora.portal.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * AWS S3 파일 업로드/다운로드
 * properties 파일로부터 버킷명, Cloud Front Domain 설정 초기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:/application-s3.properties")
public class S3FileStore {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.cloudfront.domain}")
    private String cloudFrontDomain;
    private final AmazonS3 amazonS3;

    /**
     * 파일 업로드
     * multipartFile, fileName 기반으로 PutObjectRequest 생성
     * AWS S3 인터페이스에 putObject 요청
     *
     * @param multipartFile 업로드할 파일 폼 데이터
     * @param fileName      업로드할 파일명
     * @return 업로드 성공 여부
     */
    public boolean uploadFile(MultipartFile multipartFile, String fileName) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putRequest = createPutObjectRequest(fileName, inputStream, multipartFile.getContentType(), multipartFile.getSize());
            amazonS3.putObject(putRequest);
        } catch (SdkClientException | IOException e) {
            log.error("S3FileStore.uploadFile, filename={}", fileName, e);
            return false;
        }
        return true;
    }

    /**
     * 파일 업로드
     * bytes, fileName, contentType 기반으로 PutObjectRequest 생성
     * AWS S3 인터페이스에 putObject 요청
     *
     * @param bytes       업로드할 파일의 Byte Array
     * @param fileName    업로드할 파일명
     * @param contentType HTTP Header Content-Type
     */
    public void uploadFile(byte[] bytes, String fileName, String contentType) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            PutObjectRequest putRequest = createPutObjectRequest(fileName, inputStream, contentType, bytes.length);
            amazonS3.putObject(putRequest);
        } catch (SdkClientException | IOException e) {
            log.error("S3FileStore.uploadFile, filename={}", fileName, e);
        }
    }

    /**
     * Cloud Front Domain URL 반환
     *
     * @param fileName 파일명
     * @return URL String
     */
    public String getUrl(String fileName) {
        return "https://" + cloudFrontDomain + "/" + fileName;
    }

    /**
     * 파일 삭제
     * AWS S3 인터페이스에 deleteObject 요청
     *
     * @param fileName 삭제할 파일명
     */
    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (SdkClientException e) {
            log.error("S3FileStore.deleteFile, filename={}", fileName, e);
        }
    }

    /**
     * 파일 byte array 반환
     * AWS S3 인터페이스에 getObject 요청
     * 파일 byte Array 생성
     *
     * @param fileName byte array 반환할 파일명
     * @return 파일 byte array
     */
    public byte[] loadAsBytes(String fileName) {
        byte[] bytes;
        try (S3Object file = amazonS3.getObject(bucket, fileName)) {
            S3ObjectInputStream objectContent = file.getObjectContent();
            bytes = IOUtils.toByteArray(objectContent);
        } catch (SdkClientException | IOException e) {
            log.error("S3FileStore.loadAsBytes, filename={}", fileName, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return bytes;
    }

    /**
     * 파라미터 기반 PutObjectRequest 객체 생성
     *
     * @param fileName      파일명
     * @param inputStream   File Input Stream
     * @param contentType   HTTP Header Content-Type value
     * @param contentLength HTTP Header Content-Length value
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
    private boolean fileExists(String fileName) {
        return amazonS3.doesObjectExist(bucket, fileName);
    }
}
