

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
    static boolean usePerspective = false;
    static int numSpheres = 30;

    public static void main(String args[]) {

        Camera cam = new Camera(new Vector3(0, 0, -1), new Vector3(0, 0, 1), 90, resX, resY);
        //Camera cam = new Camera(new Vector3(1,0,-1),new Vector3(0,0,1),90,resX,resY);
        //Camera cam = new Camera(new Vector3(0,0,-1),new Vector3(0,0,1),45,resX,resY);

        int[] pixels = new int[resX * resY]; // put RGB values here
        sceneSimple = new SceneSimple();
        Light sceneLight = new Light(new Vector3(0, 1.25, 0.25), 30, Color.white);


        SceneObject[] spheres = createSpheres(numSpheres,0.15f,0.01f);
       /* spheres[0] = new SphereObject(-0.5, 0, 0.5, 0.25);
        spheres[1] = new SphereObject(0.75, 0.75, 0.25, 0.15);
        spheres[2] = new SphereObject(0.5, 0.5, 1, 0.35);
        spheres[3] = new SphereObject(0, -0.5, 1, 0.4);*/

        SceneObject lightObject= new SphereObject(sceneLight.getPosition(), 0.05);
        lightObject.setShade(false);
        lightObject.setGizmo(true);
        sceneSimple.getSceneObjects().add(lightObject);
        lightObject.setScene(sceneSimple);

        PlaneObject groundPlane = new PlaneObject(new Vector3(0, -2, 0), new Vector3(0, 1, 0));
        Material groundMat = new Material(new Vector3(0.7, 0.35, 0.35), 0);
        groundPlane.setMaterial(groundMat);
        sceneSimple.getSceneObjects().add(groundPlane);
        groundPlane.setScene(sceneSimple);


        for (SceneObject s : spheres) {

            Material defaultMat = new Material(new Vector3(1.0, 0.5f * random(), 0 * random()), 0);
            s.setMaterial(defaultMat);
    ;       sceneSimple.getSceneObjects().add(s);
            s.setScene(sceneSimple);
        }

        float t = 0;
        int insideCounter = 0 , outsideCounter = 0 ;
        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vector3 rayDir;
                if (usePerspective) {
                    Vector3 pixelPos = cam.pixelCenterCoordinate(x, y);
                    rayDir = pixelPos.sub(cam.getPosition());

                } else {
                    double pixelPosX = (2 * (x + 0.5f) / (float) resX - 1) * cam.getAspectRatio() * cam.getScale();
                    double pixelPosY = (1 - 2 * (y + 0.5f) / (float) resY) * cam.getScale();
                    rayDir = new Vector3(pixelPosX, pixelPosY, 0).sub(cam.getPosition());
                }


                rayDir.normalize();

                Ray myRay = new Ray(cam.getPosition(), rayDir);
                boolean intersect = false;
                SceneObject temp;
                SceneObject intersectObj;

                for (SceneObject s : sceneSimple.getSceneObjects()) {
                    intersect = s.intersect(myRay, s);
                  /* if (intersect){
                        insideCounter++;
                    }*/

                }

                if (myRay.getNearest() != null) {
                    temp = myRay.getNearest();
                    //outsideCounter++;

                    intersectObj = temp;
                    int pixelColor = (intersectObj.isShade()) ?  intersectObj.shadeDiffuse(rayDir, cam.getPosition(), sceneLight, myRay.getT()) : Color.WHITE.getRGB();

                    pixels[y * resY + x] = pixelColor;

                }else{
                    pixels[y * resY + x] = Color.darkGray.getRGB();
                }



            }
        }
     //   System.out.println("INTERSECT INSIDE:: "+insideCounter+ " || "+ outsideCounter );

        Image image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        String path = "C:/Users/mariu/Workspaces/uni/GT Ray Tracing";
        savePic(image, "jpeg", path + random() + ".jpeg");
    }

    static SceneObject[] createSpheres(int numSpheres, float maxRad, float minRad){
        SceneObject[] spheres = new SceneObject[numSpheres];
        Vector3 spherePos;
        double sphereRadius;
        for (int i = 0; i< numSpheres; i++){
            spherePos = randomVecInRange(-0.5,1,0,1,0,2);
            sphereRadius = random() * maxRad +minRad;
            spheres[i] = new SphereObject(spherePos,sphereRadius);
        }
        return spheres;
    }

    static Vector3 randomVecInRange(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax){

        Vector3 randomVec = new Vector3(random()*xmax+xmin,random()*ymax+ymin,random()*zmax+zmin);
        return randomVec;
    }

    static void savePic(Image image, String type, String dst) {
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

    static double[] solveQuadratic(double a, double b, double c) {
        double x0, x1;
        double[] results = new double[3];
        double discr = b * b - 4 * a * c; // Diskriminanter Term in der PQ formel, Term unter der Wurzel)
        if (discr < 0) {
            // wenn dieser kleiner Null ist, dann gibt es keine Schnittpunkte
            results[0] = -1;
            return results;
        } else if (discr == 0) {
            // wenn dieser gleich Null ist, dann gibt es einen Schnittpunkt (Tangente)
            x0 = x1 = -0.5 * b / a;
        } else {
            // Ergebnis für 2 Schnittpunkte
            double q = (b > 0) ? -0.5 * (b + sqrt(discr)) : -0.5 * (b - sqrt(discr));
            x0 = q / a;
            x1 = c / q;

        }
        if (x0 > x1) {
            // siehe zu, dass x0 kleiner als x1
            double tempx0 = x0;
            double tempx1 = x1;
            x0 = tempx1;
            x1 = tempx0;

        }
        ;
        results[0] = 1; // identfikator
        results[1] = x0;
        results[2] = x1;


        return results;
    }


    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }


    static Color blend(Color a, Color b) {

        double r = (a.getRed() * b.getRed()) / 255;
        double gr = (a.getGreen() * b.getGreen()) / 255;
        double bl = (a.getBlue() * b.getBlue()) / 255;
        double al = (a.getAlpha() * b.getAlpha()) / 255;

        Color blendedCol = new Color((int) r, (int) gr, (int) bl, (int) al);
        return blendedCol;


    }
}




