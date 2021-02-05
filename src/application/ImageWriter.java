package application;

import Util.AnimationManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    private static String path; //= "D:/Uni/GT2A2 Raytracer/GT_Raytracer/render/Anim";
    private static String type; //= "D:/Uni/GT2A2 Raytracer/GT_Raytracer/render/Anim", "jpeg";

    public ImageWriter(String destinationPath, String fileType) {
        path = destinationPath;
        type = fileType;
    }

    public static void saveImage(Image image, ApplicationSettings applicationSettings) {
        int width = applicationSettings.getResX();
        int height = applicationSettings.getResY();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = bi.getGraphics();
        try {
            g.drawImage(image, 0, 0, null);
            ImageIO.write(bi, type, new File(path+"00"+ AnimationManager.getFrameCounter()+ ".jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
