abstract class SceneObject {

    private boolean shade = true;
    private Material material;

    public abstract boolean intersect(Ray ray, SceneObject object);
    public abstract int shadeDiffuse(Vector3 rayDir , Vector3 sceneOrigin, Light light, double t);

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
}
