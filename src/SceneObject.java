abstract class SceneObject {

    private boolean shade = true;
    private Material material;
    private SceneSimple scene;
    private boolean isGizmo = false;


    public abstract boolean intersect(Ray ray, SceneObject object);

    public abstract int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t);

    public abstract boolean shadowCheck( SceneSimple scene, Ray myRay);

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