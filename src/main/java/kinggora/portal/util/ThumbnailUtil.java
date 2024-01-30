package kinggora.portal.util;

import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * 썸네일 유틸리티
 * 썸네일 이미지 생성, 업로드
 * properties 파일로부터 썸네일 파일의 확장자, Content-Type, width, height 설정 초기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThumbnailUtil {


    private final S3FileStore s3FileStore;
    @Value("${file.thumbnail.extension}")
    private String THUMB_EXT;
    @Value("${file.thumbnail.content-type}")
    private String THUMB_CONTENT_TYPE;
    @Value("${file.thumbnail.size.width}")
    private int THUMB_WIDTH;
    @Value("${file.thumbnail.size.height}")
    private int THUMB_HEIGHT;

    /**
     * 썸네일 파일 업로드
     * sourceMetaData 기반으로 썸네일 파일의 메타 데이터 생성
     *
     * @param imageFile      원 이미지 파일
     * @param sourceMetaData 원 이미지 파일 메타데이터
     * @return 썸네일 파일 메타 데이터
     */
    public UploadFile uploadThumbFile(MultipartFile imageFile, UploadFile sourceMetaData) {
        byte[] thumbnail = createThumbnail(imageFile);
        String origName = sourceMetaData.getStoreName();
        String storeName = createStoreFileName(sourceMetaData.getStoreName());
        s3FileStore.uploadFile(thumbnail, storeName, THUMB_CONTENT_TYPE);
        String url = s3FileStore.getUrl(storeName);
        return UploadFile.builder()
                .postId(sourceMetaData.getPostId())
                .url(url)
                .origName(origName)
                .storeName(storeName)
                .ext(THUMB_EXT)
                .size(thumbnail.length)
                .type(FileType.THUMBNAIL)
                .regDate(LocalDateTime.now())
                .build();
    }

    /**
     * sourceImageFile에 대한 썸네일 이미지 파일 생성
     *
     * @param sourceImageFile 원 이미지 파일
     * @return 썸네일 이미지 byte array
     */
    private byte[] createThumbnail(MultipartFile sourceImageFile) {
        BufferedImage bufferedThumbImage = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphic = bufferedThumbImage.createGraphics();
        byte[] thumbByteArray = null;
        try (InputStream inputStream = sourceImageFile.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedOrigImage = ImageIO.read(inputStream);
            graphic.drawImage(bufferedOrigImage, 0, 0, THUMB_WIDTH, THUMB_HEIGHT, null);
            ImageIO.write(bufferedThumbImage, THUMB_EXT, outputStream);
            thumbByteArray = outputStream.toByteArray();
        } catch (IOException e) {
            log.error("ThumbnailUtil.createThumbnail", e);
        }
        return thumbByteArray;
    }

    /**
     * 썸네일 파일명 생성
     *
     * @param source 원 파일명
     * @return 썸네일 파일명
     */
    public String createStoreFileName(String source) {
        int pos = source.lastIndexOf(".");
        if (pos == -1) {
            return "t_" + source + "." + THUMB_EXT;
        }
        return "t_" + source.substring(0, pos + 1) + THUMB_EXT;
    }
}
