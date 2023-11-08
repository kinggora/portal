package kinggora.portal.util;

import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class ThumbnailUtil {

    private final static String THUMB_EXT = "jpg";
    private final static int THUMB_WIDTH = 300;
    private final static int THUMB_HEIGHT = 300;
    private final static int RATIO = 3;

    /**
     * 원 이미지 파일에 대한 썸네일 이미지 파일 생성
     *
     * @param file 원 이미지 파일
     * @return 썸네일 이미지 파일 정보
     */
    public UploadFile createThumbnail(UploadFile file) {
        String storeFileName = UUID.randomUUID() + "." + THUMB_EXT;
        File origFile = new File(file.getStoreDir(), file.getStoreName());
        File thumbnailFile = new File(file.getStoreDir(), storeFileName);

        BufferedImage bufferedThumbImage = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphic = bufferedThumbImage.createGraphics();

        try {
            BufferedImage bufferedOrigImage = ImageIO.read(origFile);
            graphic.drawImage(bufferedOrigImage, 0, 0, THUMB_WIDTH, THUMB_HEIGHT, null);
            ImageIO.write(bufferedThumbImage, THUMB_EXT, thumbnailFile);
        } catch (IOException e) {
            log.error("ThumbnailUtil.createThumbnail", e);
        }

        return UploadFile.builder()
                .postId(file.getPostId())
                .origName(storeFileName)
                .storeDir(file.getStoreDir())
                .storeName(storeFileName)
                .ext(THUMB_EXT)
                .size(thumbnailFile.length())
                .type(FileType.THUMBNAIL)
                .build();
    }

    private double getReducedSize(double origSize) {
        return Math.ceil(origSize / RATIO);
    }
}
