

import javax.swing.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Vector;   // use java vector as a list
import java.lang.Thread;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

import static java.lang.Math.sqrt;


public class RayTracerSimple extends java.applet.Applet {



    static SceneSimple sceneSimple;



    public static void main(String args []) {
         Camera cam = new Camera(new Vector3(0,0,-1),new Vector3(0,0,1),45);
        int resX = 1024, resY = 1024;
        int[] pixels = new int[resX * resY]; // put RGB values here

        SphereObject[] spheres = new SphereObject[4];
        spheres[0] =  new SphereObject(0,0,0,0.15);
        spheres[1] =  new SphereObject(0,0.5,0,0.15);
        spheres[2] =  new SphereObject(0.5,0.5,0,0.15);
        spheres[3] =  new SphereObject(0.5,-0.5,1,0.15);
        /*sceneSimple = new SceneSimple();
        sceneSimple.cameraPos = new Vector3(0,0,0);
        sceneSimple.sceneObjects.add(sphere);*/

        double scale = tan(toRadians(45* 0.5));
        float imageAspectRatio = resX / resY;


        for (int y  = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                float pixelPosX = (2 * (x + 0.5f) / (float)resX- 1) * imageAspectRatio*cam.scale;
                float pixelPosY = (1 - 2 * (y + 0.5f) / (float)resY)* cam.scale;
                Vector3 rayDir = new Vector3(pixelPosX,pixelPosY,0).sub(cam.position);
                rayDir.normalize();


                 Ray ray = new Ray(cam.position, rayDir);

                for (SphereObject s: spheres
                     ) {
                    boolean intersect = intetrsect(ray,s);

                    if (intersect){
                        pixels[y *resX+ x] = 0xff0000;
                    }
                }


            }
        }
        // to set a red color value for a pixel at coordinates x,y use
     /*   double yStep = 1.0/resX, xStep = 1.0/resY;
        double tmpy = 1.0, tmpz = 0.0; // tmpz is the projection plane
        int pixY=0, pixX=0;

        for (pixY=0; pixY < resY; pixY++){
            double tmpx = -1.0;
            tmpy -= yStep;
            for (pixX = 0; pixX < resX; pixX++){

                tmpx += xStep;
                Vector3 projectedPlane  = new Vector3(tmpx, tmpy, tmpz);
                Vector3 rayDir = new Vector3(projectedPlane);
                rayDir.sub(cam.position);
                rayDir.normalize();

                // Ray ray = new Ray(cam.position, rayDir);
                Ray ray = new Ray(new Vector3(tmpx, tmpy, tmpz), new Vector3(0,0,1));

                boolean intersect = intetrsect(ray,sphere);

                if (intersect){
                    pixels[pixY *resX+ pixX] = 0xff0000;
                }
            }
        }*/



        Image image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    static boolean solveQuadratic( double a,  double b,  double c, double x0, double x1)
    {
        double discr = b * b - 4 * a * c; // Diskriminanter Term in der PQ formel, Term unter der Wurzel)
        if (discr < 0){
            // wenn dieser kleiner Null ist, dann gibt es keine Schnittpunkte
            return false;
        }
        else if (discr == 0){
            // wenn dieser gleich Null ist, dann gibt es keine Schnittpunkte
            x0 = x1 = - 0.5 * b / a;
        }
        else {
            // ergebnis für 2 Schnittpunkte
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

        return true;
    }

   static boolean intetrsect(Ray ray, SphereObject sphere)
    {
        float  t0 = 0, t1 = 0; // Schnittpunktwerte für T

        /*
        a =1\\
        b=2D(Origin-C)
        c=|O-C|^2-R^2     */
        Vector3 L = ray.Origin.sub(sphere.center); // Vector ray origin to sphere origin;
        //System.out.println(L.toString());
        Vector3 dir = ray.Direction;
        dir.normalize();
        double a = dir.dotProduct(dir);// ray.Direction.dotProduct(ray.Direction); // directional Vector sq
        double b = 2 * ray.Direction.dotProduct(L);
        double c = L.dotProduct(L) - sphere.radiusSq; //
        if (!solveQuadratic(a, b, c, t0, t1)){
            return false;
        }

        if (t0 > t1){
            // siehe zu, dass t0 kleiner als t1
            float  tempt0 = t0;
            float  tempt1 = t1;
            t0 = tempt1;
            t1 = tempt0;
        }

        if (t0 < 0) {
            t0 = t1; // if t0 is negative its behind, so  let's use t1 instead
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, no intersection
            }
        }

        ray.t = t0;

        return true;
    }
}




