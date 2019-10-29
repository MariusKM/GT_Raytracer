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
    double aspectRatio ;

    float width,height;


    public Camera (Vector3 pos, Vector3 focus, float FOV, float width,float height){

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

    }

    public Vector3 rayDirection(Vector3 pixelCenterCoordinate){

        Vector3 rayDirection = pixelCenterCoordinate.sub(position) ;
        rayDirection.normalize();

        return  rayDirection;
    }

    public Vector3 pixelCenterCoordinate (int x, int y){
        double dist = position.distance(focusPoint);
        double H  =  2* dist* tan(FOV/2);
        Double W = H * aspectRatio;
        Vector3 pixelCenterCoordinate = new Vector3();
        Vector3 C = new Vector3(viewDir);

        C.mult(dist);
        Vector3 L = C.sub(new Vector3((width/2)/(width/2) , (height/2)/(height/2),0));

        double PixelHeight =  H/ height;
        double PixelWidth = W/ width;
        Vector3 uVec1 = new Vector3(uVec);
        uVec1.mult(PixelWidth* x);
        Vector3 vVec1 = new Vector3(vVec);
        vVec1.mult(PixelHeight* y);


        pixelCenterCoordinate = new Vector3(L);
        pixelCenterCoordinate.add(uVec1);
        pixelCenterCoordinate.add(vVec1);

     //  L + ((pixelWidth) * (x) * u) + ((pixelHeight) * (y) * v) ;

        return  pixelCenterCoordinate;
    }

}


