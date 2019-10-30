import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class Camera {
    Vector3 position;
    Vector3 focusPoint;
    double FOV;
    double scale;
    Vector3 viewDir;
    Vector3 rotatedUpVector;
    Vector3 vVec;
    Vector3 uVec;
    double aspectRatio ;

    double width,height;

    double distFromPlane, planeHeight, planeWidth;
    Vector3 planeCenter,planeBottomLeft,pixelCenterCoordinate;
    double pixelWidth,pixelHeight;


    public Camera (Vector3 pos, Vector3 focus, double FOV, double width,double height){

        this.position = pos;
        this.focusPoint = focus;
        this.FOV = FOV;
        this.scale  =  (float)tan(toRadians(FOV* 0.5));
        this.width = width;
        this.height = height;
        this.aspectRatio = width/height;
        this.viewDir = focusPoint.sub(position);
        this.viewDir.normalize();
        this.rotatedUpVector = new Vector3(sin(FOV), cos(FOV),0);
        this.vVec = new Vector3(viewDir);
        this.vVec.crossProduct(rotatedUpVector);
        this.vVec.normalize();
        this.uVec = new Vector3(vVec);
        this.uVec.crossProduct(viewDir);

         distFromPlane = position.distance(focusPoint);
         planeHeight =  2* distFromPlane* tan(FOV/2);
         planeWidth = planeHeight * aspectRatio;
         pixelCenterCoordinate = new Vector3();
         planeCenter = new Vector3(viewDir);

         planeCenter.mult(distFromPlane);
         //planeBottomLeft = planeCenter.sub(new Vector3((planeWidth/2)/(planeWidth/2) , (planeHeight/2)/(planeHeight/2),0));

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

}


