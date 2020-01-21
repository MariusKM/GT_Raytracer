package Util;

import Objects.Ellipsoid;
import Objects.Quadrik;
import Objects.SceneObject;
import Objects.SphereObject;
import math.Matrix4x4;
import math.TransformationMatrix4x4;
import math.Vector3;
import math.Vector3D;

public class TransformationAnimator extends Animator {

    public enum Vector3Type {
        position,
        rotation,
        scale
    }

    public Vector3Type vectorType;
    public Vector3 targetVec;
    private Vector3 startVec;


    public TransformationAnimator(SceneObject object, float speed, Vector3Type type, Vector3 targetVec, int howManyFrames) {
        super(object, speed, howManyFrames);
        this.targetVec = targetVec;
        this.vectorType = type;
        switch (vectorType) {

            case position:
                if (this.object instanceof SphereObject) {
                    startVec = ((SphereObject) object).getCenter();
                }else if( this.object instanceof Quadrik){
                    startVec = new Vector3(0,0,0);
                  /*  Matrix4x4 matrix = ((Quadrik) object).getMatrix();
                    startVec = new Vector3((float)matrix.m03,(float)matrix.m13,(float)matrix.m23);
                    TransformationMatrix4x4 trans = new TransformationMatrix4x4();
                    trans.createTranslationMatrix(new Vector3D(targetVec.x,targetVec.y, targetVec.z));
                    SceneObject temp = new Ellipsoid(0.4, 0.7, 0.4, trans);
                    Matrix4x4 matrixTarget = ((Quadrik) temp).getMatrix();
                    Vector3 newVec = new Vector3((float)matrixTarget.m03,(float)matrixTarget.m13,(float)matrixTarget.m23);
                    this.targetVec = new Vector3(newVec);*/
                }
                break;

            case scale:
                if (this.object instanceof SphereObject) {
                    startVec = new Vector3 (((SphereObject) object).getRadius(),((SphereObject) object).getRadius(),((SphereObject) object).getRadius());
                }else if( this.object instanceof Quadrik){
                    Matrix4x4 matrix = ((Quadrik) object).getMatrix();
                    startVec = new Vector3((float)matrix.m00,(float)matrix.m11,(float)matrix.m22);
                    Vector3 newVec = new Vector3((float)1.0/(targetVec.x*targetVec.x),(float)1.0/(targetVec.y*targetVec.y),(float)1.0/(targetVec.y*targetVec.y));
                    this.targetVec = new Vector3(newVec);
                }

                break;

            case rotation:
                if( this.object instanceof Quadrik){

                    startVec = new Vector3(0,0,0);
                }
                break;
        }
    }

    @Override
    public void animate() {
        super.animate();

        TransformationMatrix4x4 trans = new TransformationMatrix4x4();
        Vector3 newVec;
        switch (vectorType) {

            case position:
                if (this.object instanceof SphereObject) {
                    Vector3 newPos = Vector3.lerp(startVec, targetVec, lerpVal);
                    ((SphereObject) object).setCenter(newPos);
                }else if( this.object instanceof Quadrik){
                     newVec = Vector3.lerp(startVec, targetVec, lerpVal);
                    trans.createTranslationMatrix(new Vector3D(newVec.x,newVec.y, newVec.z));
                    ((Quadrik) object).transform(trans);

                  /*  newVec = Vector3.lerp(startVec, targetVec, lerpVal);
                    Matrix4x4 transformMat =  ((Quadrik) object).getMatrix();
                    transformMat.m03 =newVec.x;
                    transformMat.m13 =newVec.y;
                    transformMat.m23 =newVec.z;
                    ((Quadrik) object).setMatrix(transformMat);
                    ((Quadrik) object).setConstantsFromMatrix();;*/
                }
                break;

            case scale:
                if (this.object instanceof SphereObject) {
                    float newScl = MathUtil.lerp(startVec.x, targetVec.x, lerpVal);
                    ((SphereObject) object).setRadius(newScl);
                }else if( this.object instanceof Quadrik) {
                     newVec = Vector3.lerp(startVec, targetVec, lerpVal);
                    Matrix4x4 scaleMat =  ((Quadrik) object).getMatrix();
                    scaleMat.m00 =newVec.x;
                    scaleMat.m11 =newVec.y;
                    scaleMat.m22 =newVec.z;
                    ((Quadrik) object).setMatrix(scaleMat);
                    ((Quadrik) object).setConstantsFromMatrix();;
                }
                break;

            case rotation:

                if( this.object instanceof Quadrik){
                    newVec = Vector3.lerp(startVec, targetVec, lerpVal);
                    trans.createRotationMatrix(newVec.x, newVec.y, newVec.z);
                    ((Quadrik) object).transform(trans);
                }

                break;
        }
    }


}


