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
    public float speed;
    public SceneObject object;
    protected float  lerpVal = 0;
    public boolean pingPong;


   /* public Animator(SceneObject object, boolean animatePosition, Vector3 translationSpeed, boolean animateScale, float scaleSpeed, boolean animateMaterial, Material targetMaterial, float materialSpeed, int howManyFrames){
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

    } */
   public Animator(SceneObject object,float speed, int howManyFrames){
        this.speed = speed;
        this.object = object;
        this.howManyFrames = howManyFrames;
    }

    public void animate (){
        lerpVal+= speed;
        if (pingPong){
            if (lerpVal >1 || lerpVal<0){
                speed *= -1;
            }
        }


    }






}
