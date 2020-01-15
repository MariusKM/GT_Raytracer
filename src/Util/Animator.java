package Util;

import Objects.SceneObject;
import Objects.SphereObject;
import math.Vector;
import math.Vector3;

import java.security.SecurityPermission;
import java.util.HashMap;
import java.util.Map;

public class Animator {

    public int howManyFrames = 1;
    public boolean active;
    public SceneObject object;
    public boolean animatePosition, animateScale;
    public Vector3 translationSpeed;
    public float scaleSpeed;
    public boolean animateMaterial;
    public Material material, targetMaterial;
    public float materialSpeed;
    public boolean lerp;


    public Animator(SceneObject object, boolean animatePosition, Vector3 translationSpeed, boolean animateScale, float scaleSpeed, boolean animateMaterial, Material targetMaterial, float materialSpeed, int howManyFrames){
        this.object = object;
        this.animatePosition = animatePosition;
        this.translationSpeed = translationSpeed;
        this.animateScale = animateScale;
        this.scaleSpeed = scaleSpeed;
        this.howManyFrames = howManyFrames;
        this.animateMaterial = animateMaterial;
        if ( this.animateMaterial){
            material = this.object.getMaterial();
        }
        this.targetMaterial  = targetMaterial;
        this.materialSpeed = materialSpeed;


    }

    public enum AnimationParameter {
        Position,
        Rotation,
        Scale

    }


    public void animate (float deltaTime){

        if(animatePosition){
            if (this.object instanceof SphereObject){

                Vector3 newPos = ((SphereObject) object).getCenter();
                Vector3 speed = translationSpeed;
              //  speed.mult(deltaTime);

                newPos.add(speed);
                ((SphereObject) object).setCenter(newPos);
            }

            }

        if (animateScale){
            if (this.object instanceof SphereObject){

                float newRad = ((SphereObject) object).getRadius();
                newRad += scaleSpeed;//*deltaTime;

                ((SphereObject) object).setRadius(newRad);
            }

        }

        if (animateMaterial){



        }



    }




}
