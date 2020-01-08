package Objects;

import Util.Material;
import math.Vector3;

import java.awt.*;

public abstract class SceneObject {

    private boolean shade = true;
    private Material material;
    private SceneSimple scene;
    private boolean isGizmo = false;
    private float speed = 0.00f;
    private Vector3 normal;

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
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


}
