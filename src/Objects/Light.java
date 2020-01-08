package Objects;

import math.Vector3;

import java.awt.Color;

public class Light {
    private Vector3 position;
    private float intensity;
    private Color color;
    private SphereObject volume;

    public SphereObject getVolume() {
        return volume;
    }

    public void setVolume(SphereObject volume) {
        this.volume = volume;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Light (Vector3 position, float intensity, Color color, float range){

        this.position  = position;
        this.intensity = intensity;
        this.color = color;
        volume = new SphereObject(position,range);
        volume.setShade(false);
        volume.setGizmo(true);

    }

}
