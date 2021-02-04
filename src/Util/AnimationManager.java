package Util;

import java.util.ArrayList;

public class AnimationManager {
    private static int delta_timeMS;
    private static float delta_time;
    private static long last_time;
    private static int animationLength;
    private static int frameCounter = -1;
    private static ArrayList<Animator> valuesToAnimate = new ArrayList<Animator>();

    public static int getFrameCounter() {
        return frameCounter;
    }

    public static void setLast_time(long last_time) {
        AnimationManager.last_time = last_time;
    }

    public static float getDelta_time() {
        return delta_time;
    }

    public static void setAnimationLength(int animationLength) {
        AnimationManager.animationLength = animationLength;
    }

    public static ArrayList<Animator> getValuesToAnimate() {
        return valuesToAnimate;
    }

    public static void setValuesToAnimate(ArrayList<Animator> valuesToAnimate) {
        AnimationManager.valuesToAnimate = valuesToAnimate;
    }

    public static boolean isFinished(){
        return  frameCounter<= animationLength;

    }
    static void handleTime() {
        AnimationManager.frameCounter++;

        long time = System.nanoTime();
        delta_timeMS = (int) ((time - last_time) / 1000000);
        delta_time = ((float) delta_timeMS) / 1000;
        last_time = time;
    }
    public static void animate( ){
        handleTime();
        for (Animator anim:
             valuesToAnimate) {
            anim.animate();
        }
    }
}
