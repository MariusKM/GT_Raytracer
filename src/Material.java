public class Material {

    private Vector3 albedoColor;
    private float roughness, metalness;

    public Vector3 getAlbedoColor() {
        return albedoColor;
    }

    public void setAlbedoColor(Vector3 albedoColor) {
        this.albedoColor = albedoColor;
    }

    public float getRoughness() {
        return roughness;
    }

    public float getMetalness() {
        return metalness;
    }

    public void setMetalness(float metalness) {
        this.metalness = metalness;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }

    public Material(Vector3 albedo, float roughness,float metalness){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
    }
}
