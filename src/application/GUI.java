package application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

public abstract class GUI {
    private static Image image;
    private static JFrame frame = new JFrame();
    private static JLabel graphics = new JLabel();
    private static  int resX, resY;

    public static Image getImage() {
        return image;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void initialize(ApplicationSettings applicationSettings){
        resX = applicationSettings.getResX();
        resY = applicationSettings.getResY();
    }

    public static void drawGUI(boolean exit, int[] pixels) {

        if (exit) {
            return;
        }
        image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX,resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));


        //JLabel graphics = new JLabel(new ImageIcon(image));
        graphics.setIcon(new ImageIcon(image));
        frame.add(graphics);

        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
