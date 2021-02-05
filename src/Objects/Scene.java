package Objects;

import java.awt.*;

import Util.AnimationManager;
import Util.Material;
import Util.MaterialAnimator;
import Util.TransformationAnimator;
import application.ApplicationSettings;
import application.GUI;
import application.KeyHandler;
import math.Vector3;

import java.util.ArrayList;

import static Util.MathUtil.generatRandomPositiveNegitiveValue;
import static java.lang.Math.random;

public class Scene {

    private ArrayList<SceneObject> sceneObjects;
    private ArrayList<Light> sceneLights = new ArrayList<>();
    private Camera sceneCam;
    private Color bgCol;
    int resX, resY;
    public Scene(ApplicationSettings applicationSettings){
        sceneObjects = new ArrayList<SceneObject>();
        this.resX = applicationSettings.getResX();
        this.resY = applicationSettings.getResY();
        this.bgCol = applicationSettings.getBG_Color();
    }

    public ArrayList<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(ArrayList<SceneObject> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }

    public ArrayList<Light> getSceneLight() {
        return sceneLights;
    }

    public void setSceneLight(ArrayList<Light> sceneLight) {
        this.sceneLights = sceneLight;
    }

    public Camera getSceneCam() {
        return sceneCam;
    }

    public void setSceneCam(Camera sceneCam) {
        this.sceneCam = sceneCam;
    }

    public Color getBgCol() {
        return bgCol;
    }

    public void setBgCol(Color bgCol) {
        this.bgCol = bgCol;
    }

    public void initializeScene(){

    }

    void setupGround(){
        PlaneObject groundPlane = new PlaneObject(new Vector3(0.0f, 0, 0), new Vector3(0, 1, 0));
        Material groundMat = new Material(new Vector3(0.7f, 0.35f, 0.35f), 0.1f, 0f,1f,1.3f,false);
        groundPlane.setMaterial(groundMat);
        getSceneObjects().add(groundPlane);
        groundPlane.setScene(this);
    }
    protected  void setUpCamera(){
        setSceneCam(new Camera(new Vector3(0.75f, 0.65f, 2), new Vector3(0, 0, -1), 90, resX, resY));
    }
    protected void setUpKeyHandler(){
        KeyHandler keyHandler = new KeyHandler();
        GUI.getFrame().addKeyListener(keyHandler);
    }
    protected void setUpLight(){
        Light sceneLight = new Light(new Vector3(0.75f, 1.5f, 1.5f), 25,  new Vector3(0.9f,0.7f,1f),0.3f);
        getSceneLight().add(sceneLight);

        Light sceneLight2 = new Light(new Vector3(0.75f, 1.5f, 0f), 25, new Vector3(0.9f,0.6f,1f),0.3f);
        getSceneLight().add(sceneLight2);

        Light sceneLight3 = new Light(new Vector3(0f, 0.5f, -0.5f), 25,   new Vector3(1f,0.8f,1f),0.3f);
        getSceneLight().add(sceneLight3);
    }

    protected void configureObjects( SceneObject[] generatedObjects){

        // Configure Scene Objects
        for (SceneObject s : generatedObjects) {

           Material defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.25f * random()), (float) (0.75f * random())),  0.01f, 1f,0.8f,1.3f,false);

            s.setMaterial(defaultMat);
            getSceneObjects().add(s);
            s.setScene(this);
            if (s instanceof Quadrik){

                int scaleOrTrans = generatRandomPositiveNegitiveValue(1,-1);

                if (scaleOrTrans >0){

                    Vector3 vec =new Vector3(((float)Math.random()) *((Ellipsoid)s).initScale.x+((Ellipsoid)s).initScale.x,((float)Math.random())*((Ellipsoid)s).initScale.y+((Ellipsoid)s).initScale.y,((float)Math.random()) *((Ellipsoid)s).initScale.z+((Ellipsoid)s).initScale.z);
                    TransformationAnimator sAnim = new TransformationAnimator(s,0.001f * (float)Math.random(), TransformationAnimator.Vector3Type.scale,vec,10);
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
        AnimationManager.setUpAnimation(this);

    }



}
