public class Material {

    public Vector3 albedoColor;
    double roughness;


    public Material(Vector3 albedo, double roughness){

        this.albedoColor = albedo;
        this.roughness = roughness;
    }
}
