package kinggora.portal.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class ThumbnailUtil {

    public final static String THUMB_EXT = "jpg";
    private final static int THUMB_WIDTH = 300;
    private final static int THUMB_HEIGHT = 300;
    private final static int RATIO = 3;

    /**
     * 원 이미지 파일에 대한 썸네일 이미지 파일 생성
     *
     * @param origFile 원 이미지 파일
     * @return 썸네일 이미지 byte array
     */
    public byte[] createThumbnail(MultipartFile origFile) {
        BufferedImage bufferedThumbImage = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphic = bufferedThumbImage.createGraphics();
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();

        try {
            BufferedImage bufferedOrigImage = ImageIO.read(origFile.getInputStream());
            graphic.drawImage(bufferedOrigImage, 0, 0, THUMB_WIDTH, THUMB_HEIGHT, null);
            ImageIO.write(bufferedThumbImage, THUMB_EXT, thumbOutput);  //
        } catch (IOException e) {
            log.error("ThumbnailUtil.createThumbnail", e);
        }
        return thumbOutput.toByteArray();
    }

    private double getReducedSize(double origSize) {
        return Math.ceil(origSize / RATIO);
    }
}
