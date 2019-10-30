

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;   // use java vector as a list
import java.lang.Thread;
import java.awt.Color;
import java.awt.Color;

import static java.lang.Math.*;

public class RayTracerSimple extends java.applet.Applet {



    static SceneSimple sceneSimple;

    static int resX = 1024, resY = 1024;

    public static void main(String args []) {

          Camera cam = new Camera(new Vector3(0,0,-1),new Vector3(0,0,1),90 ,resX,resY);
          //Camera cam = new Camera(new Vector3(1,0,-1),new Vector3(0,0,1),90,resX,resY);
        //Camera cam = new Camera(new Vector3(0,0,-1),new Vector3(0,0,1),45,resX,resY);

        int[] pixels = new int[resX * resY]; // put RGB values here
        sceneSimple = new SceneSimple();
        Light sceneLight = new Light(new Vector3(0,2,2.5), 10, Color.white);


        SphereObject[] spheres = new SphereObject[5];
        spheres[0] =  new SphereObject(0,0,0.5,0.25);
        spheres[1] =  new SphereObject(0,0.5,0.25,0.15);
        spheres[2] =  new SphereObject(0.5,0.5,2.5,0.35);
        spheres[3] =  new SphereObject(0.5,-0.5,2,0.4);
        spheres[4] =  new SphereObject(sceneLight.getPosition(),0.05);
        spheres[4].setShade(false);


        for (SphereObject s: spheres) {

            Material defaultMat = new Material(new Vector3(1.0 , 0.5f*random(), 0*random()),0);
            s.setMaterial(defaultMat);
            sceneSimple.getSceneObjects().add(s);

        }



        float t = 0 ;

        for (int y  = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
             double pixelPosX = (2 * (x + 0.5f) / (float)resX- 1) * cam.getAspectRatio()*cam.getScale();
             double pixelPosY = (1 - 2 * (y + 0.5f) / (float)resY)* cam.getScale();
             Vector3 rayDir = new Vector3(pixelPosX,pixelPosY,0).sub(cam.getPosition());



               /*    Vector3 pixelPos = cam.pixelCenterCoordinate(x,y);

                Vector3 rayDir = pixelPos.sub(cam.getPosition());
 */

                rayDir.normalize();

                Ray myRay = new Ray(cam.getPosition(), rayDir);

                for (SphereObject s: sceneSimple.getSceneObjects()) {
                    boolean intersect = intetrsect(myRay,s);

                    if (intersect && s == myRay.getNearest()){

                        int pixelColor = s.shade(rayDir,cam.getPosition(),sceneLight,myRay.getT());


                        pixels[y *resY+ x] =(s.isShade()) ? pixelColor  :0x0000ff;
                        break;

                    }

                    pixels[y *resY+ x] =Color.darkGray.getRGB();

                }

            }
        }



        Image image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        savePic(image,"jpeg","render"+random()+".jpeg");
    }

    static  void savePic(Image image, String type, String dst){
        int width = resX;
        int height = resY;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = bi.getGraphics();
        try {
            g.drawImage(image, 0, 0, null);
            ImageIO.write(bi, type, new File(dst));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    static double[] solveQuadratic( double a,  double b,  double c )
    {
        double x0, x1;
        double[] results = new double[3];
        double discr = b * b - 4 * a * c; // Diskriminanter Term in der PQ formel, Term unter der Wurzel)
        if (discr < 0){
            // wenn dieser kleiner Null ist, dann gibt es keine Schnittpunkte
            results[0] = -1;
            return results;
        }
        else if (discr == 0){
            // wenn dieser gleich Null ist, dann gibt es einen Schnittpunkt (Tangente)
            x0 = x1 = - 0.5 * b / a;
        }
        else {
            // Érgebnis für 2 Schnittpunkte
            double q = (b > 0) ? -0.5 *  (b + sqrt(discr)) : -0.5 * (b - sqrt(discr));
            x0 = q / a;
            x1 = c / q;

        }
        if (x0 > x1){
            // siehe zu, dass x0 kleiner als x1
            double tempx0 = x0;
            double tempx1 = x1;
            x0 = tempx1;
            x1 = tempx0;

        };
        results[0] = 1; // identfikator
        results[1] = x0;
        results[2] = x1;


        return results;
    }

   static boolean intetrsect( Ray Ray, SphereObject sphere)
    {

        /*
        a =1\\
        b=2D(Origin-C)
        c=|O-C|^2-R^2    */
        Vector3 L = Ray.getOrigin().sub(sphere.getCenter());
        Vector3 dir = Ray.getDirection();
        dir.normalize();
        double a = dir.dotProduct(dir);// directional Vector sq
        double b = 2 * Ray.getDirection().dotProduct(L);
        double c = L.dotProduct(L) - sphere.getRadiusSq();
        double[] quadraticResults = solveQuadratic(a, b, c);

        double  t0 =  quadraticResults[1];
        double  t1 =  quadraticResults[2];
        if (quadraticResults[0] <0){

            return false;
        }

        if (t0 > t1){

            // siehe zu, dass t0 kleiner als t1
            double  tempt0 = t0;
            double  tempt1 = t1;
            t0 = tempt1;
            t1 = tempt0;
        }


        if (t0 < 0) {
            t0 = t1; // if negative, INtersection is behind us
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, keine schnittpunkte
            }
        }
        if (t0< Ray.getT()){
            Ray.setT(t0);
            Ray.setNearest(sphere);
        }


        return true;
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }


    static Color blend  (Color a, Color b){

         double r =(a.getRed()*b.getRed())/ 255;
         double gr =(a.getGreen()*b.getGreen())/ 255;
         double bl =(a.getBlue()*b.getBlue())/ 255;
         double al =(a.getAlpha()*b.getAlpha())/ 255;

         Color blendedCol = new Color((int)r,(int)gr,(int)bl,(int)al);
         return blendedCol;


    }
}




