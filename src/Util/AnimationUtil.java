package Util;

import java.util.ArrayList;

public class AnimationUtil {

    private static ArrayList<Animator> valuesToAnimate = new ArrayList<Animator>();

    public static ArrayList<Animator> getValuesToAnimate() {
        return valuesToAnimate;
    }

    public static void setValuesToAnimate(ArrayList<Animator> valuesToAnimate) {
        AnimationUtil.valuesToAnimate = valuesToAnimate;
    }

    public static void animate(float deltaTime){

        for (Animator anim:
             valuesToAnimate) {
            anim.animate(deltaTime);
        }
    }
}
