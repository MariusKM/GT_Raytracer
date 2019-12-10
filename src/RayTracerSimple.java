

import Objects.*;
import Util.Material;
import Util.MathUtil;
import math.TransformationMatrix4x4;
import math.Vector3;
import math.Vector3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import static java.lang.Math.*;

public class RayTracerSimple extends java.applet.Applet {


    static SceneSimple sceneSimple;

    static int resX = 1024, resY = 1024;
    static boolean usePerspective = true;
    static int numSpheres = 25;
    static SceneObject[] sceneObjects;
    static int[] pixels;
    static Camera cam;
    static Light sceneLight;
    private static boolean exit;
    static int delta_timeMS;
    static float delta_time;
    static long last_time;
    static Color BG_Color = new Color(0.1f, 0.1f, 0.1f);

    public static boolean isExit() {
        return exit;
    }

    public static void setExit(boolean exit) {
        RayTracerSimple.exit = exit;
    }

    static JFrame frame = new JFrame();
    static JLabel graphics = new JLabel();

    public static void main(String args[]) {

        initScene();
        last_time = System.nanoTime();
        boolean test = true;
        do {
            handleTime();
            handleAnimation();

            paintPix();
            drawGUI();


            // exit = true;
        }
        while (!exit);

        String path = "C:/Users/mariu/Workspaces/uni/GT Objects.Ray Tracing";
        //savePic(image, "jpeg", path + random() + ".jpeg");
    }

    /*
    Do the animation stuff
     */
    static void handleAnimation() {
        float upperLimit = 1.5f;
        float lowerLimit = -1f;

        for (SceneObject s : sceneSimple.getSceneObjects()
        ) {

            if (s instanceof Ellipsoid && !s.isGizmo()) {


                TransformationMatrix4x4 trans = new TransformationMatrix4x4();
                trans.createTranslationMatrix(new Vector3D(0, s.getSpeed() * delta_time, 0));

                ((Ellipsoid) s).transform(trans);
                trans = new TransformationMatrix4x4();
                //trans.createYRotationMatrix(s.getSpeed()*delta_time);
                trans.createRotationMatrix(s.getSpeed() * delta_time, s.getSpeed() * delta_time, s.getSpeed() * delta_time);
                ((Ellipsoid) s).transform(trans);


            } else if (s instanceof SphereObject && !s.isGizmo()) {

                Vector3 newPos = ((SphereObject) s).getCenter();
                if (newPos.y > upperLimit || newPos.y < lowerLimit) {

                    ((SphereObject) s).setSpeed(((SphereObject) s).getSpeed() * -1);
                }


                newPos.add(new Vector3(0, ((SphereObject) s).getSpeed() * delta_time, 0));
                ((SphereObject) s).setCenter(newPos);
            }
        }

    }


    static void handleTime() {

        long time = System.nanoTime();
        delta_timeMS = (int) ((time - last_time) / 1000000);
        delta_time = ((float) delta_timeMS) / 1000;
        last_time = time;
        //System.out.println("last frame took :: "+delta_time+"s");
    }

    static void drawGUI() {

        if (exit) {
            return;
        }
        Image image = Toolkit.getDefaultToolkit()
                .createImage(new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX));


        //JLabel graphics = new JLabel(new ImageIcon(image));
        graphics.setIcon(new ImageIcon(image));
        frame.add(graphics);

        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void paintPix() {
        float t = 0;
        int insideCounter = 0, outsideCounter = 0;
        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vector3 rayDir;
                if (usePerspective) {
                    Vector3 pixelPos = cam.pixelCenterCoordinate(x, y);

                    rayDir = pixelPos.sub(cam.getPosition());


                } else {
                    float pixelPosX = (2 * (x + 0.5f) / (float) resX - 1) * cam.getAspectRatio() * cam.getScale();
                    float pixelPosY = (1 - 2 * (y + 0.5f) / (float) resY) * cam.getScale();
                    rayDir = new Vector3(pixelPosX, pixelPosY, 0).sub(cam.getPosition());
                }


                rayDir.normalize();

                Ray myRay = new Ray(cam.getPosition(), rayDir);
                boolean intersect = false;
                SceneObject temp;
                SceneObject intersectObj;

                for (SceneObject s : sceneSimple.getSceneObjects()) {
                    if (s instanceof PlaneObject) {
                        intersect = s.intersect(myRay);
                    } else {
                        intersect = s.intersect(myRay);
                    }
                }
                int indexer = usePerspective ? (resY - y - 1) * resY + x : (y * resY + x);
                if (myRay.getNearest() != null) {
                    temp = myRay.getNearest();
                    intersectObj = temp;
                    //int pixelColor = (intersectObj.isShade()) ? (intersectObj instanceof Objects.PlaneObject) ? intersectObj.shadeDiffuse(rayDir, cam.getPosition(), sceneLight, myRay.getT0()) :   intersectObj.shadeCookTorrance(rayDir, cam.getPosition(), sceneLight, myRay.getT0()) : Color.WHITE.getRGB();
                    Vector3 rayDirN = new Vector3(rayDir);
                    rayDirN.mult(-1);
                    Vector3 finalCol = intersectObj.shadeCookTorrance(rayDir, rayDirN, sceneSimple, myRay.getT0(), false,5);
                    //System.out.println(finalCol.toString());
                    Color finalColorRGB = new Color(MathUtil.clampF(finalCol.x, 0, 1), MathUtil.clampF(finalCol.y, 0, 1), MathUtil.clampF(finalCol.z, 0, 1));
                    //Color finalColorRGB = new Color(finalCol.x, finalCol.y, finalCol.z );
                    int pixelColor = (intersectObj.isShade()) ? finalColorRGB.getRGB() : Color.WHITE.getRGB();

                    pixels[indexer] = pixelColor;

                } else {
                    pixels[indexer] = sceneSimple.getBgCol().getRGB();
                }
            }
        }

    }

    static void initScene() {
        cam = new Camera(new Vector3(0, 0, 1), new Vector3(0, 0, -1), 90, resX, resY);

        KeyHandler keyHandler = new KeyHandler();
        frame.addKeyListener(keyHandler);
        pixels = new int[resX * resY]; // put RGB values here
        sceneSimple = new SceneSimple();
        sceneLight = new Light(new Vector3(0f, -0.25f, -0), 50, Color.white);
        sceneSimple.setSceneCam(cam);
        sceneSimple.setSceneLight(sceneLight);
        sceneSimple.setBgCol(new Color(0.1f,0.1f,0.1f));

        PlaneObject groundPlane = new PlaneObject(new Vector3(0, -0.5f, 0), new Vector3(0, 1, 0));
        Material groundMat = new Material(new Vector3(0.7f, 0.35f, 0.35f), 0.3f, 0.4f, 0.99f);
        groundPlane.setMaterial(groundMat);
        sceneSimple.getSceneObjects().add(groundPlane);
        groundPlane.setScene(sceneSimple);
        SceneObject testSphere = new SphereObject(new Vector3(0.1f, -0.25f, -0.25f), 0.15f);
        SceneObject testSphere1 = new SphereObject(new Vector3(0.1f, -0.25f,-0.45f), 0.15f);
        SceneObject testSphere2 = new SphereObject(new Vector3(00.6f, 0.5f, -0.25f), 0.15f);
        SceneObject testSphere3 = new SphereObject(new Vector3(-0.3f, 0.5f, -0.25f), 0.15f);
        testSphere.setSpeed(0.1f);
        testSphere.setSpeed(0.1f);
        Material defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.5f, 1,0.5f,1.3f,true);
        testSphere.setMaterial(defaultMat);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.5f, 1,0.5f,1.3f,false);
        testSphere1.setMaterial(defaultMat);
        sceneObjects = new Objects.SceneObject[]{
                testSphere,
                testSphere1,
               /* testSphere2,
                testSphere3*/

        }; ///createSpheres(numSpheres, 0.15f, 0.01f);//createSceneObjects(numSpheres, 0.15f, 0.01f);//
        // sceneObjects = createSpheres(numSpheres, 0.15f, 0.02f);
      /*  Objects.SceneObject lightObject = new Objects.SphereObject(sceneLight.getPosition(), 0.05f);
        lightObject.setShade(false);
        lightObject.setGizmo(true);
        sceneSimple.getSceneObjects().add(lightObject);
        lightObject.setScene(sceneSimple);
        lightObject.setMaterial(groundMat);*/

        TransformationMatrix4x4 trans = new TransformationMatrix4x4();
        trans.createTranslationMatrix(new Vector3D(0, 1, -2));
        SceneObject ellipse = new Ellipsoid(0.4, 0.7, 0.4, trans);

      /* sceneSimple.getSceneObjects().add(ellipse);
        ellipse.setShade(false);
        ellipse.setGizmo(true);
        ellipse.setScene(sceneSimple);
        ellipse.setMaterial(groundMat);*/

        SceneObject ellipse2 = new Ellipsoid(0.7, 0.4, 0.4, trans);
       /* sceneSimple.getSceneObjects().add(ellipse2);
        //ellipse2.setShade(false);
        ellipse2.setGizmo(true);
        ellipse2.setScene(sceneSimple);
        ellipse2.setMaterial(groundMat);*/

        ComplexObject xobj = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj2 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj3 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        //xobj.setShade(false);

        //    sceneSimple.getSceneObjects().add(xobj);
        // sceneSimple.getSceneObjects().add(xobj2);
        // sceneSimple.getSceneObjects().add(xobj3);
        Material CSGmat = new Material(new Vector3(0.7f, 0.35f, 0.35f), 0.25f, 0.3f);
        xobj.setScene(sceneSimple);
        xobj.setMaterial(CSGmat);
        xobj2.setScene(sceneSimple);
        xobj2.setMaterial(CSGmat);
        xobj3.setScene(sceneSimple);
        xobj3.setMaterial(CSGmat);

        for (SceneObject s : sceneObjects) {
          /*  Material defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.5f, 1,0.5f);

            s.setMaterial(defaultMat);*/
            sceneSimple.getSceneObjects().add(s);
            s.setScene(sceneSimple);
        }
    }

    static SceneObject[] createSceneObjects(int numObjects, float maxRad, float minRad) {
        SceneObject[] objects = new SceneObject[numObjects];
        Vector3 objectPos;
        float radiusX, radiusY, radiusZ;
        for (int i = 0; i < numObjects; i++) {
            objectPos = randomVecInRange(-0.5f, 1, -0.75f, 1, -0.5, 0);
            radiusX = (float) (random() * maxRad + minRad);
            radiusY = (float) (random() * maxRad + minRad);
            radiusZ = (float) (random() * maxRad + minRad);
            TransformationMatrix4x4 trans = new TransformationMatrix4x4();
            trans.createTranslationMatrix(new Vector3D(objectPos.x, objectPos.y, objectPos.z));
            SceneObject ellipse = new Ellipsoid(0.9, 0.6, 0.2, trans);
            objects[i] = new Ellipsoid(radiusX, radiusY, radiusZ, trans);
        }
        return objects;
    }

    static SceneObject[] createSpheres(int numSpheres, float maxRad, float minRad) {
        SceneObject[] spheres = new SceneObject[numSpheres];
        Vector3 spherePos;
        float sphereRadius;
        for (int i = 0; i < numSpheres; i++) {
            spherePos = randomVecInRange(-0.5f, 1, -0.75f, 1, -0.25, -0.05);
            sphereRadius = (float) (random() * maxRad + minRad);
            spheres[i] = new SphereObject(spherePos, sphereRadius);
        }
        return spheres;
    }

    static Vector3 randomVecInRange(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {

        Vector3 randomVec = new Vector3((float) (random() * xmax + xmin), (float) (random() * ymax + ymin), (float) (random() * zmax + zmin));
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


}




