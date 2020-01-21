

import Objects.*;
import Util.*;
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
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class RayTracerSimple extends java.applet.Applet {


    static SceneSimple sceneSimple;

    static int resX = 2048, resY = 2048;
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
    static int kernelSize = 10; //dimension
    static Color BG_Color = new Color(0.125f, 0.115f, 0.125f);
    static Image image;

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

            paintPix();
            gaußFilter();
            drawGUI();
            handleTime();
            handleAnimation();



            // exit = true;
        }
        while (!exit);

        String path = "D:/Projekte/uni/GT_Raytracer/render/";

        savePic(image, "jpeg", path + random() + ".jpeg");
    }

    private static void filter() {
        int new_pixels[] = new int[resX * resY];
        for (int i = 0; i < resX * resY; i++) {

            int width = resX;
            int[] kernel = new int[kernelSize * kernelSize];

            int newPix = Integer.MIN_VALUE;

            int medianEl = kernel.length / 2;
            for (int y = 0, k = 0; y < kernelSize; y++) {
                for (int x = 0; x < kernelSize; x++, k++) {
                    int kernelPos = i + width * y + x;
                    try {
                        kernel[k] = pixels[kernelPos];
                        if (y == kernelSize - 1 && x == kernelSize - 1) {
                            Arrays.sort(kernel);
                            if (kernelSize > 1) {
                                newPix = kernel[medianEl];
                            } else {
                                newPix = pixels[i + width * y + x];
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        kernel[k] = pixels[i];
                    }
                }
            }
            new_pixels[i] = newPix;
        }
        //pixels = new_pixels;
        System.arraycopy(new_pixels, 0, pixels, 0, new_pixels.length);
    }

    private static void gaußFilter() {
        //int[] filter = {1, 2, 1, 2, 4, 2, 1, 2, 1};
        int[] filter = {
                1, 4, 6, 4, 1,
                4, 16, 24, 16, 4,
                6, 24, 36, 24, 6,
                4, 16, 24, 16, 4,
                1, 4, 6, 4, 1};
        int filterWidth = 5;
        int output[] = new int[resX * resY];

        final int width = resX;
        final int height = resY;
        final int sum = IntStream.of(filter).sum();

        final int pixelIndexOffset = width - filterWidth;
        final int centerOffsetX = filterWidth / 2;
        final int centerOffsetY = filter.length / filterWidth / 2;

        // apply filter
        for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                     filterIndex < filter.length;
                     pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
                        int col = pixels[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels seperately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }
        System.arraycopy(output, 0, pixels, 0, output.length);
    }

    /*
    Do the animation stuff
     */
    static void handleAnimation() {
        AnimationUtil.animate();
    }

    static void setUpAnimation() {
        for (SceneObject S : sceneSimple.getSceneObjects()
        ) {
            if (S.getAnimators().size() >0) {
                AnimationUtil.getValuesToAnimate().addAll(S.getAnimators());
            };
        }
    }


    static void handleTime() {
        long time = System.nanoTime();
        delta_timeMS = (int) ((time - last_time) / 1000000);
        delta_time = ((float) delta_timeMS) / 1000;
        last_time = time;
    }

    static void drawGUI() {

        if (exit) {
            return;
        }
        image = Toolkit.getDefaultToolkit()
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
                    intersect = s.intersect(myRay);
                }
                int indexer = usePerspective ? (resY - y - 1) * resY + x : (y * resY + x);
                if (myRay.getNearest() != null) {
                    temp = myRay.getNearest();
                    intersectObj = temp;

                    Vector3 finalCol = intersectObj.shadeCookTorrance(myRay, sceneSimple, false, 5);

                    Color finalColorRGB = new Color(MathUtil.clampF(finalCol.x, 0, 1), MathUtil.clampF(finalCol.y, 0, 1), MathUtil.clampF(finalCol.z, 0, 1));

                    int pixelColor = (intersectObj.isShade()) ? finalColorRGB.getRGB() : Color.WHITE.getRGB();
                    pixels[indexer] = pixelColor;

                } else {
                    pixels[indexer] = sceneSimple.getBgCol().getRGB();
                }

                System.out.println("painted pixel no " + indexer);
            }

        }

    }

    static void initScene() {
        // TODO test why spheres only get light when under the light
        cam = new Camera(new Vector3(0.75f, 0.65f, 2), new Vector3(0, 0, -1), 90, resX, resY);

        KeyHandler keyHandler = new KeyHandler();
        frame.addKeyListener(keyHandler);
        pixels = new int[resX * resY]; // put RGB values here
        sceneSimple = new SceneSimple();
        sceneLight = new Light(new Vector3(0.75f, 1.5f, 1.5f), 25,  new Vector3(0.9f,0.7f,1f),0.3f);
        sceneSimple.setSceneCam(cam);
        sceneSimple.getSceneLight().add(sceneLight);
        sceneSimple.setBgCol(BG_Color);

        Light sceneLight2 = new Light(new Vector3(0.75f, 1.5f, 0f), 25, new Vector3(0.9f,0.6f,1f),0.3f);
        sceneSimple.getSceneLight().add(sceneLight2);
        Light sceneLight3 = new Light(new Vector3(0f, 0.5f, -0.5f), 25,   new Vector3(1f,0.8f,1f),0.3f);
        sceneSimple.getSceneLight().add(sceneLight2);

        PlaneObject groundPlane = new PlaneObject(new Vector3(0.0f, 0, 0), new Vector3(0, 1, 0));
        Material groundMat = new Material(new Vector3(0.7f, 0.35f, 0.35f), 0.1f, 0f,1f,1.3f,false);
        groundPlane.setMaterial(groundMat);
        sceneSimple.getSceneObjects().add(groundPlane);
        groundPlane.setScene(sceneSimple);

        SceneObject testSphere = new SphereObject(new Vector3(0.5f, 0.5f, 1.05f), 0.3f);
        Material defaultMat = new Material(new Vector3((float) (0.5f), (float) (0.5f), (float) (0.5)), 0.01f, 1, 0f, 1f, true);
        testSphere.setMaterial(defaultMat);

        SphereObject testSphere1 = new SphereObject(new Vector3(0.25f, 1.25f, 0), 0.5f);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.001f, 1f, 0.8f, 1.3f, false);
        testSphere1.setMaterial(defaultMat);
        TransformationAnimator anim = new TransformationAnimator(testSphere1,0.2f, TransformationAnimator.Vector3Type.scale,new Vector3(0, 0.0f, 0),10);
        anim.pingPong = true;
        MaterialAnimator animMat = new MaterialAnimator(testSphere1,0.1f, MaterialAnimator.MaterialValueType.color,new Material(new Vector3(1,1,1),0,0,0),10);
        animMat.pingPong = true;
        testSphere1.getAnimators().add(anim);
        testSphere1.getAnimators().add(animMat);

        SceneObject testSphere2 = new SphereObject(new Vector3(1f, 0.25f, 1.05f), 0.2f);
        SceneObject testSphere3 = new SphereObject(new Vector3(0.0f, 0.25f, 1.05f), 0.2f);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.8f, 1.3f, false);
        testSphere2.setMaterial(defaultMat);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.8f, 1.3f, false);

        testSphere3.setMaterial(defaultMat);
        sceneObjects = new Objects.SceneObject[]{
                ///   testSphere,
                testSphere1,
                testSphere2,
                testSphere3

        };

        Material ellipsoidMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.9f, 1.3f, false);

        sceneObjects = createSceneObjects(numSpheres, 0.2f, 0.01f);



        TransformationMatrix4x4 trans = new TransformationMatrix4x4();
        trans.createTranslationMatrix(new Vector3D(1f, 0.5f, 0));
        SceneObject ellipse = new Ellipsoid(0.4, 0.7, 0.4, trans);
        sceneSimple.getSceneObjects().add(ellipse);
        ellipse.setScene(sceneSimple);
        ellipse.setMaterial(ellipsoidMat);
        TransformationAnimator anim2 = new TransformationAnimator(ellipse,0.1f, TransformationAnimator.Vector3Type.position,new Vector3(0, 0.1f, 0),10);

        ellipse.getAnimators().add(anim2);

        TransformationMatrix4x4 trans2 = new TransformationMatrix4x4();
        trans2.createTranslationMatrix(new Vector3D(-0.5f, 0.25f, 0.5));
        SceneObject ellipse2 = new Ellipsoid(0.7, 0.4, 0.4, trans2);
        ellipse2.setScene(sceneSimple);
        ellipse2.setMaterial(ellipsoidMat);
        sceneSimple.getSceneObjects().add(ellipse2);

        ComplexObject xobj = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj2 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj3 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);

        for (SceneObject s : sceneObjects) {


            defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.25f * random()), (float) (0.75f * random())),  0.01f, 1f,0.8f,1.3f,false);

            s.setMaterial(defaultMat);
            sceneSimple.getSceneObjects().add(s);
            s.setScene(sceneSimple);
        }
        setUpAnimation();
    }

    static SceneObject[] createSceneObjects(int numObjects, float maxRad, float minRad) {
        SceneObject[] objects = new SceneObject[numObjects];
        Vector3 objectPos;
        float radiusX, radiusY, radiusZ;
        for (int i = 0; i < numObjects; i++) {
            objectPos = randomVecInRange(-0.5f, 1.5f, 0.25f, 1, 0, 1.5);
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




