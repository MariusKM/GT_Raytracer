package animation;

import objects.SceneObject;
import math.Vector3;
import render.Material;
import math.MathUtil;

public class MaterialAnimator  extends Animator {



    public enum MaterialValueType {
        color,
        roughness,
        reflectivity,
        refractiveIndex
    }

    public MaterialAnimator.MaterialValueType valueType;
    private Material startMat, targetMat;
    public Vector3 targetVec;
    private Vector3 startVec;
    private float startFloat, targetFloat;


    public MaterialAnimator(SceneObject object, float speed, MaterialAnimator.MaterialValueType valueType, Material targetMat, int howManyFrames) {
        super(object, speed, howManyFrames);
        this.startMat = object.getMaterial();
        this.targetMat = targetMat;
        this.valueType = valueType;

        switch (valueType) {

            case color:
                    startVec = startMat.getAlbedoColor();
                    targetVec = this.targetMat.getAlbedoColor();
                break;

            case roughness:
                startFloat = startMat.getRoughness();
                startFloat = this.targetMat.getRoughness();
                break;

            case reflectivity:
                startFloat = startMat.getReflectivity();
                startFloat = this.targetMat.getReflectivity();
                break;
        }
    }
    @Override
    public void animate() {
        super.animate();

        float newVal;
        switch (valueType) {

            case color:
                Vector3 newCol = Vector3.lerp(startVec, targetVec, lerpVal);
                object.getMaterial().setAlbedoColor(newCol);
                break;

            case roughness:
                 newVal = MathUtil.lerp(startFloat,targetFloat,lerpVal);
                object.getMaterial().setRoughness(newVal);
                break;

            case reflectivity:
                 newVal = MathUtil.lerp(startFloat,targetFloat,lerpVal);
                object.getMaterial().setReflectivity(newVal);
                break;
        }
    }


}