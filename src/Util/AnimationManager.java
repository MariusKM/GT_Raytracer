package Util;

import java.util.ArrayList;

public class AnimationManager {
    public static int delta_timeMS;
    public static float delta_time;
    public static long last_time;
    private static ArrayList<Animator> valuesToAnimate = new ArrayList<Animator>();

    public static ArrayList<Animator> getValuesToAnimate() {
        return valuesToAnimate;
    }

    public static void setValuesToAnimate(ArrayList<Animator> valuesToAnimate) {
        AnimationManager.valuesToAnimate = valuesToAnimate;
    }
    static void handleTime() {
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
