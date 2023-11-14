package kinggora.portal.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    /**
     * 첨부 파일을 스토리지에 업로드
     *
     * @param fileName 파일명
     * @return 업로드된 파일 정보 리스트
     */
    public String getUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void uploadFile(MultipartFile multipartFile, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("FileStore.uploadFile", e);
        }
    }

    public void uploadThumbFile(byte[] bytes, String fileName) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ThumbnailUtil.THUMB_EXT);
        metadata.setContentLength(bytes.length);

        amazonS3.putObject(bucket, fileName, byteArrayInputStream, metadata);
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
     * @param storeFileName 실제 저장된 파일명
     * @return 파일 input stream resource
     */
    public Resource loadAsResource(String storeFileName) {
        S3Object file;
        try {
            file = amazonS3.getObject(bucket, storeFileName);
        } catch (AmazonS3Exception e) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND, e.getMessage());
        }
        S3ObjectInputStream objectContent = file.getObjectContent();
        return new InputStreamResource(objectContent);
    }

}
