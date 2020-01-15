package Objects;

import Util.Animator;
import Util.Material;
import math.Vector3;

import java.awt.*;
import java.util.ArrayList;

public abstract class SceneObject {

    private boolean shade = true;
    private Material material;
    private SceneSimple scene;
    private boolean isGizmo = false;

    private Vector3 normal;
    private ArrayList<Animator> animators = new ArrayList<>();

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

    public abstract int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t);

    public abstract Vector3 shadeCookTorrance(Ray ray,SceneSimple currentScene, boolean refl, float depth);

    public abstract boolean shadowCheck(SceneSimple scene, Ray myRay);

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

    public SceneSimple getScene() {
        return scene;
    }

    public void setScene(SceneSimple scene) {
        this.scene = scene;
    }

    public boolean isGizmo() {
        return isGizmo;
    }

    public void setGizmo(boolean gizmo) {
        isGizmo = gizmo;
    }



}
