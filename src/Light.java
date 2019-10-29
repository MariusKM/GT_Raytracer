import java.awt.Color;

public class Light {
    Vector3 position;
    double intensity;
    Color color;

    public Light (Vector3 position, double intensity, Color color){

        this.position  = position;
        this.intensity = intensity;
        this.color = color;
    }

}
