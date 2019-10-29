

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
import java.awt.Color;
import static java.lang.Math.sqrt;
import java.awt.Color;

public class RayTracerSimple extends java.applet.Applet {



    static SceneSimple sceneSimple;



    public static void main(String args []) {
        Camera cam = new Camera(new Vector3(0,0,-1),new Vector3(0,0,0),90);
        int resX = 1024, resY = 1024;
        int[] pixels = new int[resX * resY]; // put RGB values here

        SphereObject[] spheres = new SphereObject[5];
        spheres[0] =  new SphereObject(0,0,0.5,0.15);
        spheres[1] =  new SphereObject(0,0.5,0.25,0.15);
        spheres[2] =  new SphereObject(0.5,0.5,2.5,0.15);
        spheres[3] =  new SphereObject(0.5,-0.5,2,0.15);
        spheres[4] =  new SphereObject(2,2,5,0.05);
        spheres[4].shade = false;
        /*sceneSimple = new SceneSimple();
        sceneSimple.cameraPos = new Vector3(0,0,0);
        sceneSimple.sceneObjects.add(sphere);*/

        Light sceneLight = new Light(new Vector3(2,2,2.5), 10, Color.white);

        float  scale = (float)tan(toRadians(45* 0.5));
        float imageAspectRatio = resX / resY;
        float t = 0 ;

        for (int y  = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                float pixelPosX = (2 * (x + 0.5f) / (float)resX- 1) * imageAspectRatio*cam.scale;
                float pixelPosY = (1 - 2 * (y + 0.5f) / (float)resY)* cam.scale;
                Vector3 rayDir = new Vector3(pixelPosX,pixelPosY,0).sub(cam.position);
                rayDir.normalize();

                /*Vector3 pixelPos = cam.pixelCenterCoordinate(y,x);
                System.out.println(pixelPos.toString());
                Vector3 rayDir = pixelPos.sub(cam.position);
                rayDir.normalize();*/
                Ray myRay = new Ray(cam.position, rayDir);

                for (SphereObject s: spheres
                     ) {
                    boolean intersect = intetrsect(myRay,s);





                    if (intersect){

                        int intensity = s.shade(rayDir,cam.position,sceneLight,myRay.t);

                        int clampedIntesity =   clamp(intensity,-255,255);

                        System.out.println(255*((double)clampedIntesity/255));

                        Color lightColor = new Color(0, (int)(47*((double)clampedIntesity/255)), (int)(255*((double)clampedIntesity/255)));
                       // Color materialColor = new Color((int)(255*((double)clampedIntesity/255)), 0, 0);
                        int materialColor = new Color((int)(255*((double)clampedIntesity/255)), 0, 0).getRGB();
                      /*  int newR = clamp(lightColor.getRed()+materialColor.getRed(),0,255);
                        int newG = clamp(lightColor.getGreen()+materialColor.getGreen(),0,255);
                        int newB = clamp(lightColor.getBlue()+materialColor.getBlue(),0,255);
                        Color finalColor =new Color(newR,newG,newB);*/


                         // Color lightColor = Color.white;
                        //  Color materialColor = Color.red;
                       //   Color blendedCol = brighter(materialColor,clampedIntesity);
                        pixels[y *resX+ x] =(s.shade) ? materialColor  :0x0000ff;
                        //pixels[y *resX+ x] =(s.shade) ? blendedCol.getRGB()  :blendedCol.getRGB();
                    }

                }
git 

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

    static boolean test(Ray Ray){


        Ray.hitPos = new Vector3(100,100,100);
        Ray.t = 1000;
        return false;
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
        results[0] = 1;
        results[1] = x0;
        results[2] = x1;


        return results;
    }

   static boolean intetrsect( Ray Ray, SphereObject sphere)
    {


        /*
        a =1\\
        b=2D(Origin-C)
        c=|O-C|^2-R^2     */
        Vector3 L = Ray.Origin.sub(sphere.center); // Vector ray origin to sphere origin;
        //System.out.println(L.toString());
        Vector3 dir = Ray.Direction;
        dir.normalize();
        double a = dir.dotProduct(dir);// ray.Direction.dotProduct(ray.Direction); // directional Vector sq
        double b = 2 * Ray.Direction.dotProduct(L);
        double c = L.dotProduct(L) - sphere.radiusSq; //
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
            t0 = t1; // if t0 is negative its behind, so  let's use t1 instead
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, no intersection
            }
        }

        Ray.t = t0;


        return true;
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static Color brighter(Color col, double brightnessFac) {
        int r = col.getRed();
        int g = col.getGreen();
        int b = col.getBlue();
        int alpha = col.getAlpha();
        brightnessFac /= 255;
        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int)(1.0/(1.0-brightnessFac));
        if ( r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/brightnessFac), 255),
                Math.min((int)(g/brightnessFac), 255),
                Math.min((int)(b/brightnessFac), 255),
                alpha);
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




