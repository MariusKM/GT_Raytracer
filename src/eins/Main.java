package eins;

import javax.swing.*;
import java.awt.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static int aufX;
    public static int aufY;

    public static void main(String[] args)
    {
        try {
            Properties prop = new Properties();
            prop.load( new FileInputStream("src/eins/einstellungen.properties"));

            aufX = Integer.valueOf(prop.getProperty("auflösungx"));
            aufY = Integer.valueOf(prop.getProperty("auflösungy"));
            int[] pixel = new int[aufX * aufY]; // put RGB values here

            // to set a red color value for a pixel at coordinates x,y use
            // pixels[y * resX + x] = 0xff0000;


            Szene szene1 = new Szene(0xffdead);
            szene1.objekte.add(new Kugel(new Vektor3(512,334,0),10, 0xff0000));
            szene1.objekte.add(new Kugel(new Vektor3(512,444,3),10, 0x0000ff));
            szene1.objekte.add(new Kugel(new Vektor3(512,224,-2),10, 0x00ff00));
          //  szene1.objekte.add(new Lampe(new Vektor3(0,2,0),2));
            Kamera kam = new Kamera(new Vektor3(0,0,-1),new Vektor3(0,0,1));

            Strahler strahler = new Strahler(szene1,kam);
            pixel = strahler.zeichne();

            Image image = Toolkit.getDefaultToolkit()
                    .createImage(new MemoryImageSource(aufX, aufY,
                                 new DirectColorModel(24, 0xff0000, 0xff00, 0xff),
                                 pixel, 0, aufX));
            //Fenster
            JFrame frame = new JFrame();
            frame.add(new JLabel(new ImageIcon(image)));
            frame.setResizable(false);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
