package application;

import java.awt.*;

public abstract class ApplicationSettings {
    protected int resX,resY;
    protected boolean usePerspective;
    protected int animationLength ;
    protected Color BG_Color;

    public int getResX() {
        return resX;
    }

    public void setResX(int resX) {
        this.resX = resX;
    }

    public int getResY() {
        return resY;
    }

    public void setResY(int resY) {
        this.resY = resY;
    }

    public boolean isUsePerspective() {
        return usePerspective;
    }

    public void setUsePerspective(boolean usePerspective) {
        this.usePerspective = usePerspective;
    }

    public int getAnimationLength() {
        return animationLength;
    }

    public void setAnimationLength(int animationLength) {
        this.animationLength = animationLength;
    }

    public Color getBG_Color() {
        return BG_Color;
    }

    public void setBG_Color(Color BG_Color) {
        this.BG_Color = BG_Color;
    }

}
