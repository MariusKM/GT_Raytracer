import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RTWindow {
    public static void main(String[] args) {
        int resX = 1024, resY = 768;
        int[] pixels = new int[resX * resY]; // put RGB values here

        // to set a red color value for a pixel at coordinates x,y use

        for (int y = 0; y < resY; y++){

            for (int x = 0; x < resX; x++){
                pixels[y * resX + x] = 0xff0000;
            }
        }

        // pixels[y * resX + x] = 0xff0000;

        Image image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

