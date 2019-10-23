

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Vector;   // use java vector as a list
import java.lang.Thread;



public class RayTracerSimple extends java.applet.Applet {



    static SceneSimple sceneSimple;


    public RayTracerSimple() {
        sceneSimple = new SceneSimple();
        sceneSimple.sceneObjects = new Vector();
    }
    public static void main(String args []) {
        int resX = 1024, resY = 768;
        int[] pixels = new int[resX * resY]; // put RGB values here

        // to set a red color value for a pixel at coordinates x,y use

        for (int y = 0; y < resY; y++){

            for (int x = 0; x < resX; x++){
                pixels[y * resX + x] = 0xff0000;
            }
        }


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

class renderPanel extends JPanel {

    Vector targetList;
    Image img;
    static String calcString = "Tracing...";

   /* public DrawPanel(Vector tl) {
        this.targetList = tl;
        setBackground(Color.red);
    }

    public void paint(Graphics g) {
        System.out.println("in paint");
        int w = size().width;
        int h = size().height;

        if (img == null) {
            super.paint(g);
            g.setColor(Color.green);
            FontMetrics fm = g.getFontMetrics();
            int x = (w - fm.stringWidth(calcString))/2;
            int y = h-1;
            g.drawString(calcString, x, y);

        } else {
            g.drawImage(img, 0, 0, w, h, this);
        }
    }*/

   public Image newImage(){
       int[] buffer = new int[800*600];

       for (int i = 0 ; i< buffer.length; i++){
           buffer[i] = -1;
       }
       Image newImage = Toolkit.getDefaultToolkit().createImage(
               new MemoryImageSource(800, 600,buffer
                        , 0, 800));

       return newImage;

   }

    public Image getImage() {
        return img;
    }

    public void setImage(Image img) {
        this.img = img;
        repaint();
    }
}

class SceneSimple{

 Vector sceneObjects;
 Vector3 lightPos;
 Vector3 cameraPos;


}

class renderFrame extends JFrame {

    public renderFrame() {
        super("Ray Tracer");
    }


}


class Vector3{
    double x,y,z;


    public Vector3(){

        x = 0;
        y = 0;
        z = 0;

    }

    public Vector3(double x,double y,double z){

        this.x = x;
        this.y = y;
        this.z = z;

    }


}