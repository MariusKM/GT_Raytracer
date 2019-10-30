public class Material {

    private Vector3 albedoColor;
    private double roughness;

    public Vector3 getAlbedoColor() {
        return albedoColor;
    }

    public void setAlbedoColor(Vector3 albedoColor) {
        this.albedoColor = albedoColor;
    }

    public double getRoughness() {
        return roughness;
    }

    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }

    public Material(Vector3 albedo, double roughness){

        this.albedoColor = albedo;
        this.roughness = roughness;
    }
}
