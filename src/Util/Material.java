package Util;

import math.Vector3;

public class Material {

    private Vector3 albedoColor;
    private float roughness, metalness, reflectivity;

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

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
    public Material(Vector3 albedo, float roughness, float metalness, float reflectivity){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
    }
    public Material(Vector3 albedo, float roughness, float metalness){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = 0;
    }


    public Material(Vector3 albedo, float roughness){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = 0;
        this.reflectivity = 0;
    }
}
