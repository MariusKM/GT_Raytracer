package application;

import components.Scene;
import objects.SceneObject;
import util.Animator;

import java.util.ArrayList;

public class AnimationManager {

    private static AnimationManager instance;
    public static AnimationManager getInstance(){
        if(instance == null) instance = new AnimationManager();
        return instance;
    }
    private  int animationLength;

    private ArrayList<Animator> valuesToAnimate;

    public  void setAnimationLength(int animationLength) {
        this.animationLength = animationLength;
    }

    public  ArrayList<Animator> getValuesToAnimate() {
        return valuesToAnimate;
    }

    public  void setValuesToAnimate(ArrayList<Animator> valuesToAnimate) {
        this.valuesToAnimate = valuesToAnimate;
    }

    private AnimationManager(){
        valuesToAnimate = new ArrayList<>();
    };

    public void  setUpAnimation(Scene currentScene) {
        for (SceneObject S : currentScene.getSceneObjects()
        ) {
            if (S.getAnimators().size() >0) {
                getValuesToAnimate().addAll(S.getAnimators());
            };
        }
    }

    public  boolean isFinished(){
        return  TimeHandler.frameCounter<= animationLength;
    }

    public  void animate( ){
        for (Animator anim:
             valuesToAnimate) {
            anim.animate();
        }
        System.out.println("Last frame took " + TimeHandler.getDelta_time());
    }
}
