package components;

import math.Vector3;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class Camera {
    private Vector3 position;
    private Vector3 focusPoint;
    private float FOV;
    private float scale;
    private Vector3 viewDir;
    private Vector3 rotatedUpVector;
    private Vector3 vVec;
    private Vector3 uVec;
    private float aspectRatio ;



    private float  width,height;

    private float  planeHeight, planeWidth;
    private Vector3 planeCenter,planeBottomLeft,pixelCenterCoordinate;
    private float pixelWidth,pixelHeight;


    public Camera (Vector3 pos, Vector3 focus, float FOV, float  width,float  height){

        this.position = pos;
        this.focusPoint = focus;
        this.FOV = FOV;
        this.scale  =  (float)tan(toRadians(FOV* 0.5));
        this.width = width;
        this.height = height;
        this.aspectRatio = width/height;
        init();

    }

    void init(){
        viewDir = focusPoint.sub(position);
        viewDir.normalize();
        rotatedUpVector = new Vector3(0, 1,0);//new math.Vector3((float)sin(FOV), (float)cos(FOV),0);
        uVec = new Vector3(viewDir);
        uVec.crossProduct(rotatedUpVector);
        uVec.normalize();
        vVec = new Vector3(uVec);
        vVec.crossProduct(viewDir);
        vVec.normalize();

        planeHeight =  2* (float )tan(FOV/2);
        planeWidth = planeHeight * aspectRatio;

        planeCenter = new Vector3(viewDir);
        planeCenter.add(position);
        planeBottomLeft = planeCenter.sub(new Vector3((planeWidth/2) ,(planeHeight/2),0));
        pixelHeight =  planeHeight/ height;
        pixelWidth = planeWidth/ width;
    }


    public Vector3 pixelCenterCoordinate (int x, int y){


        Vector3 uVec1 = new Vector3(uVec);
        uVec1.mult(pixelWidth* x);
        Vector3 vVec1 = new Vector3(vVec);
        vVec1.mult(pixelHeight* y);


        pixelCenterCoordinate = new Vector3(planeBottomLeft);
        pixelCenterCoordinate.add(uVec1);
        pixelCenterCoordinate.add(vVec1);

     //  L + ((pixelWidth) * (x) * u) + ((pixelHeight) * (y) * v) ;

        return  pixelCenterCoordinate;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getFocusPoint() {
        return focusPoint;
    }

    public void setFocusPoint(Vector3 focusPoint) {
        this.focusPoint = focusPoint;
    }

    public float getFOV() {
        return FOV;
    }

    public void setFOV(float FOV) {
        this.FOV = FOV;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}


