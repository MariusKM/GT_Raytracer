

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

import static Util.MathUtil.generatRandomPositiveNegitiveValue;
import static java.lang.Math.*;

public class RayTracerApplication extends java.applet.Applet {


    static SceneSimple currentScene;


    static int numSpheres = 25;
    static SceneObject[] sceneObjects;
    static int[] pixels;
    protected static Camera cam;
    static Light sceneLight;
    private static boolean exit;

    public static void setExit(boolean exit) {
        RayTracerApplication.exit = exit;
    }

    static ApplicationSettings applicationSettings;
    static RayTracer rayTracer;
    static GaussFilter filter;

    public static void main(String args[]) {
        initialize();
        do {
            rayTracer.render(pixels,cam, currentScene);
            handleFilter();
            GUI.drawGUI(exit,pixels);
            AnimationManager.animate();
            if (AnimationManager.isFinished()){
                String path = "D:/Uni/GT2A2 Raytracer/GT_Raytracer/render/Anim";
                ImageWriter.saveImage(GUI.getImage(), "jpeg", path +"00"+AnimationManager.getFrameCounter() + ".jpeg",applicationSettings);
            }else{
                exit = true;
            }
            System.out.println("Last frame took " + AnimationManager.getDelta_time());
        }
        while (!exit);
    }

    static void initialize(){
        applicationSettings = new DefaultApplicationSettings();
        rayTracer = new RayTracer(applicationSettings);
        filter = new GaussFilter(applicationSettings.getResX(),applicationSettings.getResY());
        AnimationManager.setAnimationLength(applicationSettings.getAnimationLength());
        GUI.initialize(applicationSettings);
        initScene();
        AnimationManager.setLast_time(System.nanoTime());
    }

    static void handleFilter(){
        var output = filter.applyFilter(pixels);
        System.arraycopy(output, 0, pixels, 0, output.length);
    }


    static void initScene() {
        int resX =applicationSettings.getResX();
        int resY =applicationSettings.getResY();

        // TODO test why spheres only get light when under the light
        cam = new Camera(new Vector3(0.75f, 0.65f, 2), new Vector3(0, 0, -1), 90, resX, resY);

        KeyHandler keyHandler = new KeyHandler();
        GUI.getFrame().addKeyListener(keyHandler);
        pixels = new int[resX * resY]; // put RGB values here
        currentScene = new SceneSimple();
        sceneLight = new Light(new Vector3(0.75f, 1.5f, 1.5f), 25,  new Vector3(0.9f,0.7f,1f),0.3f);
        currentScene.setSceneCam(cam);
        currentScene.getSceneLight().add(sceneLight);
        currentScene.setBgCol(applicationSettings.getBG_Color());

        Light sceneLight2 = new Light(new Vector3(0.75f, 1.5f, 0f), 25, new Vector3(0.9f,0.6f,1f),0.3f);
        currentScene.getSceneLight().add(sceneLight2);
        Light sceneLight3 = new Light(new Vector3(0f, 0.5f, -0.5f), 25,   new Vector3(1f,0.8f,1f),0.3f);
        currentScene.getSceneLight().add(sceneLight3);

        PlaneObject groundPlane = new PlaneObject(new Vector3(0.0f, 0, 0), new Vector3(0, 1, 0));
        Material groundMat = new Material(new Vector3(0.7f, 0.35f, 0.35f), 0.1f, 0f,1f,1.3f,false);
        groundPlane.setMaterial(groundMat);
        currentScene.getSceneObjects().add(groundPlane);
        groundPlane.setScene(currentScene);

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
           //     testSphere1,
              //  testSphere2,
            //    testSphere3

        };

        Material ellipsoidMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.9f, 1.3f, false);

        sceneObjects = createSceneObjects(numSpheres, 0.2f, 0.01f);



        TransformationMatrix4x4 trans = new TransformationMatrix4x4();
        trans.createTranslationMatrix(new Vector3D(1f, 0.5f, 0));
        SceneObject ellipse = new Ellipsoid(0.4, 0.7, 0.4, trans);
        currentScene.getSceneObjects().add(ellipse);
        ellipse.setScene(currentScene);
        ellipse.setMaterial(ellipsoidMat);
        TransformationAnimator anim2 = new TransformationAnimator(ellipse,0.1f, TransformationAnimator.Vector3Type.position,new Vector3(0.0f, 0.11f, 0.0f),10);

        ellipse.getAnimators().add(anim2);

        TransformationMatrix4x4 trans2 = new TransformationMatrix4x4();
        trans2.createTranslationMatrix(new Vector3D(-0.5f, 0.25f, 0.5));
        SceneObject ellipse2 = new Ellipsoid(0.7, 0.4, 0.4, trans2);
        ellipse2.setScene(currentScene);
        ellipse2.setMaterial(ellipsoidMat);
        currentScene.getSceneObjects().add(ellipse2);

        ComplexObject xobj = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj2 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);
        ComplexObject xobj3 = new ComplexObject((Quadrik) ellipse, (Quadrik) ellipse2, ComplexObject.Operation.DIFFERENZ);

        for (SceneObject s : sceneObjects) {


            defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.25f * random()), (float) (0.75f * random())),  0.01f, 1f,0.8f,1.3f,false);

            s.setMaterial(defaultMat);
            currentScene.getSceneObjects().add(s);
            s.setScene(currentScene);
            if (s instanceof Quadrik){

                int scaleOrTrans = generatRandomPositiveNegitiveValue(1,-1);

                if (scaleOrTrans >0){

                    Vector3 vec =new Vector3(((float)Math.random()) *((Ellipsoid)s).initScale.x+((Ellipsoid)s).initScale.x,((float)Math.random())*((Ellipsoid)s).initScale.y+((Ellipsoid)s).initScale.y,((float)Math.random()) *((Ellipsoid)s).initScale.z+((Ellipsoid)s).initScale.z);
                    TransformationAnimator  sAnim = new TransformationAnimator(s,0.001f * (float)Math.random(), TransformationAnimator.Vector3Type.scale,vec,10);
                    sAnim.pingPong = true;
                    s.getAnimators().add(sAnim);

                }else{
                    int posOrNeg = generatRandomPositiveNegitiveValue(1,-1);
                    TransformationAnimator  sAnim = new TransformationAnimator(s,0.1f * (float)Math.random(), TransformationAnimator.Vector3Type.position,new Vector3(0, 0.1f*posOrNeg, 0),10);
                    sAnim.pingPong = true;
                    s.getAnimators().add(sAnim);

                }


                scaleOrTrans = generatRandomPositiveNegitiveValue(1,-1);

                if (scaleOrTrans >0){


                    MaterialAnimator SanimMat = new MaterialAnimator(s,0.1f, MaterialAnimator.MaterialValueType.color,new Material(new Vector3(1 * (float)Math.random(),1 * (float)Math.random(),1 * (float)Math.random()),0,0,0),10);
                    SanimMat.pingPong = true;
                    s.getAnimators().add(SanimMat);
                }

            }
        }
        AnimationManager.setUpAnimation(currentScene);
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




}




