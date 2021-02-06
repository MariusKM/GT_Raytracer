package util;

import math.Vector3;

public class Material {

    private Vector3 albedoColor;
    private float roughness, metalness, reflectivity,refractiveIndex;
    private boolean transparent;

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

    public float getRefractiveIndex() {
        return refractiveIndex;
    }

    public void setRefractiveIndex(float refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
    public Material(Vector3 albedo, float roughness, float metalness, float reflectivity, float refractiveIndex,boolean isTransparent){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
        this.refractiveIndex = refractiveIndex;
        this.transparent = isTransparent;
    }
    public Material(Vector3 albedo, float roughness, float metalness, float reflectivity,float refractiveIndex){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
        this.refractiveIndex = refractiveIndex;
        this.transparent = false;
    }

    public Material(Vector3 albedo, float roughness, float metalness, float reflectivity){

        this.albedoColor = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
        this.refractiveIndex = 1.0f;
        this.transparent = false;
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
