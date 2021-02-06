package Util;

import objects.SceneObject;

public abstract class Animator {

    public int howManyFrames = 1;
    public float speed;
    public SceneObject object;
    protected float  lerpVal = 0;
    public boolean pingPong;


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
                lerpVal = lerpVal >1 ?  1: 0;
            }
        }


    }






}
