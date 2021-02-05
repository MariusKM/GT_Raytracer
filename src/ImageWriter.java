import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    static void saveImage(Image image, String type, String dst, ApplicationSettings applicationSettings) {
        int width = applicationSettings.getResX();
        int height = applicationSettings.getResY();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = bi.getGraphics();
        try {
            g.drawImage(image, 0, 0, null);
            ImageIO.write(bi, type, new File(dst));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
