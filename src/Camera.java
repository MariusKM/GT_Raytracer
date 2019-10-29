import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class Camera {
    Vector3 position;
    Vector3 focusPoint;
    float FOV;
    float scale;
    Vector3 viewDir;
    Vector3 rotatedUpVector;
    Vector3 vVec;
    Vector3 uVec;
    double aspectRatio;

    float width,height;


    public Camera (Vector3 pos, Vector3 focus, float FOV){

        this.position = pos;
        this.focusPoint = focus;
        this.FOV = FOV;
        this.scale  =  (float)tan(toRadians(FOV* 0.5));
        this.width = width;
        this.height = height;
        this.aspectRatio = width/height;
        this.viewDir = position.sub(focusPoint);
        this.viewDir.normalize();
        this.rotatedUpVector = new Vector3(sin(FOV), cos(FOV),0);
        this.vVec = viewDir;
        this.vVec.crossProduct(rotatedUpVector);
        this.vVec.normalize();
        this.uVec = vVec;
        this.uVec.crossProduct(viewDir);

    }

    public Vector3 rayDirection(Vector3 pixelCenterCoordinate){

        Vector3 rayDirection = pixelCenterCoordinate.sub(position) ;
        rayDirection.normalize();

        return  rayDirection;
    }

    public Vector3 pixelCenterCoordinate (int i, int j){
        double dist = position.distance(focusPoint);
        double H  =  2* dist* tan(FOV/2);
        Double W = H * aspectRatio;
        Vector3 pixelCenterCoordinate = new Vector3();
        Vector3 C = viewDir;

        C.mult(1);
        Vector3 L = C.sub(new Vector3(width/2 , height/2,0));

        double PixelHeight =  H/ height;
        double PixelWidth = W/ width;
        Vector3 uVec1 = uVec;
        uVec1.mult(PixelWidth* j);
        Vector3 vVec1 = vVec;
        vVec1.mult(PixelHeight* i);


        pixelCenterCoordinate = L;
        pixelCenterCoordinate.add(uVec1);
        pixelCenterCoordinate.add(vVec1);



        return  pixelCenterCoordinate;
    }

}


