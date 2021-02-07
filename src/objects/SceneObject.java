package objects;

import components.Light;
import components.Scene;
import util.Animator;
import util.CookTorrance;
import util.Material;
import math.Vector3;
import util.ShadingModel;

import java.util.ArrayList;

public abstract class SceneObject {

    private boolean shade = true;
    private Material material;
    private Scene scene;
    private boolean isGizmo = false;
    private Vector3 normal;
    private ArrayList<Animator> animators = new ArrayList<>();
    protected ShadingModel shader = new CookTorrance();

    public ArrayList<Animator> getAnimators() {
        return animators;
    }

    public void setAnimator(ArrayList<Animator> animators) {
        this.animators = animators;

    }

    public Vector3 getNormal() {
        return normal;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }

    public abstract boolean intersect(Ray ray);

    public abstract Vector3 shade(Ray ray, Scene currentScene, boolean refl, float depth);

    public abstract boolean shadowCheck(Scene scene, Ray myRay);

    public boolean isShade() {
        return shade;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public boolean isGizmo() {
        return isGizmo;
    }

    public void setGizmo(boolean gizmo) {
        isGizmo = gizmo;
    }

}
