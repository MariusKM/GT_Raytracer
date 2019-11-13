abstract class SceneObject {

    private boolean shade = true;
    private Material material;
    private SceneSimple scene;
    private boolean isGizmo = false;
    private float speed = 0.05f;

    public abstract boolean intersect(Ray3 ray3, SceneObject object);

    public abstract int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t);
    public abstract int shadeCookTorrance(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t);

    public abstract boolean shadowCheck( SceneSimple scene, Ray3 myRay3);

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
