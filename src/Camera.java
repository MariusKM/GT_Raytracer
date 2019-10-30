import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class Camera {
    private Vector3 position;
    private Vector3 focusPoint;
    private double FOV;
    private double scale;
    private Vector3 viewDir;
    private Vector3 rotatedUpVector;
    private Vector3 vVec;
    private Vector3 uVec;
    private double aspectRatio ;



    private double width,height;

    private double distFromPlane, planeHeight, planeWidth;
    private Vector3 planeCenter,planeBottomLeft,pixelCenterCoordinate;
    private double pixelWidth,pixelHeight;


    public Camera (Vector3 pos, Vector3 focus, double FOV, double width,double height){

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
        rotatedUpVector = new Vector3(sin(FOV), cos(FOV),0);
        vVec = new Vector3(viewDir);
        vVec.crossProduct(rotatedUpVector);
        vVec.normalize();
        uVec = new Vector3(vVec);
        uVec.crossProduct(viewDir);

        distFromPlane = position.distance(focusPoint);
        planeHeight =  2* distFromPlane* tan(FOV/2);
        planeWidth = planeHeight * aspectRatio;
        pixelCenterCoordinate = new Vector3();
        planeCenter = new Vector3(viewDir);
        planeCenter.mult(distFromPlane);
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

    public double getFOV() {
        return FOV;
    }

    public void setFOV(double FOV) {
        this.FOV = FOV;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

}


