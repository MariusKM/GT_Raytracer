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

    float width,height;


    public Camera (Vector3 pos, Vector3 focus, float FOV){

        this.position = pos;
        this.focusPoint = focus;
        this.FOV = FOV;
        this.scale  =  (float)tan(toRadians(45* 0.5));
        this.width = width;
        this.height = height;

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

   /* public Vector3 pixelCenterCoordinate (int i, int j){

        Vector3 pixelCenterCoordinate = new Vector3();

        Vector3 C = viewDir;
        C.mult(0);

        Vector3 L = C - width/2 - height/2;


        pixelCenterCoordinate = L + (pixelWidth) * (j) * u + (pixelHeight) * (i) * v ;



        return  pixelCenterCoordinate;
    }*/

}


