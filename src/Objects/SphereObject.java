package Objects;

import Util.RenderUtil;
import Util.MathUtil;
import math.Vector3;

import java.awt.*;

public class SphereObject extends SceneObject {
    private Vector3 center;
    private float radius, radiusSq; // precompute radiusSq since we use it a lot

// TODO : Clean up shading


    public SphereObject(float x, float y, float z, float r) {
        center = new Vector3(x, y, z);
        radius = r;
        radiusSq = r * r;
    }

    public SphereObject(Vector3 v, float r) {
        center = new Vector3(v.x, v.y, v.z);
        radius = r;
        radiusSq = r * r;
    }

    public SphereObject() {
        center = new Vector3(0, 0, 0);
        radius = radiusSq = 1;
    }

@Override

   public Vector3 shadeCookTorrance(Vector3 rayDir,Vector3 rayDirN, SceneSimple currentScene, float t,boolean refl, float depth) {

        Vector3 intersection, normal, lightDir;
        float intensity;
        Light light = currentScene.getSceneLight();
        Vector3 sceneOrigin = currentScene.getSceneCam().getPosition();


        float metalness = getMaterial().getMetalness();
        float roughness = getMaterial().getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = getMaterial().getAlbedoColor();
        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(t);
        intersection.add(sceneOrigin);

        // find surface normal
        normal = new Vector3(intersection);
        normal.sub(normal, center);
        normal.normalize();


        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();


        float lightDist = center.distance(light.getPosition());

        Vector3 finalCol = RenderUtil.CookTorranceNeu(lightDir,normal, rayDir, rayDirN,intersection,this, currentScene,refl,depth);
        // TODO Multiple Lights
        // SHADOWS && INTENSITY
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;
            return new Vector3(0,0,0);
        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity  *= light.getIntensity();
        }

        finalCol.mult(intensity);
        return finalCol;

    }

    public int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {

        Vector3 intersection, normal, lightDir;
        float intensity;


        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(t);
        intersection.add(sceneOrigin);

        // find surface normal
        normal = new Vector3(intersection);
        normal.sub(normal, center);
        normal.normalize();

        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = center.distance(light.getPosition());
        //System.out.println(lightDist);
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }

        if (intensity < 0.0)
            intensity = 0.0f;

        if (intensity > 1.0)
            intensity = 1.0f;


        // int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity)), (int) (lightColor.getGreen() * ((float) intensity)), (int) (lightColor.getBlue() * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));

        int pixelCol = objectColor.getRGB();

        return (pixelCol);
    }

    public boolean intersect(Ray Ray) {
        /*
        a =1\\
        b=2D(Origin-C)
        c=|O-C|^2-R^2    */
        SphereObject sphere = this;

        Vector3 L = Ray.getOrigin().sub(sphere.getCenter());
        Vector3 dir = Ray.getDirection();
        dir.normalize();
        float a = dir.dotProduct(dir);// directional math.Vector sq
        float b = 2 * Ray.getDirection().dotProduct(L);
        float c = L.dotProduct(L) - sphere.getRadiusSq();
        float[] quadraticResults = MathUtil.solveQuadratic(a, b, c);

        float t0 = quadraticResults[1];
        float t1 = quadraticResults[2];
        if (quadraticResults[0] < 0) {
            return false;
        }

        if (t0 > t1) {

            // siehe zu, dass t0 kleiner als t1
            float tempt0 = t0;
            float tempt1 = t1;
            t0 = tempt1;
            t1 = tempt0;
        }


        if (t0 < 0) {
            t0 = t1; // if negative, INtersection is behind us
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, keine schnittpunkte
            }
        }
        if (t0 < Ray.getT0()) {
            Ray.setT0(t0);
            Ray.setNearest(sphere);
        }


        return true;
    }


    public boolean shadowCheck(SceneSimple scene, Ray myRay) {
        boolean shadow = RenderUtil.shadowCheck(scene, myRay, this);
        return shadow;
    }


    public Vector3 getCenter() {
        return center;
    }

    public void setCenter(Vector3 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadiusSq() {
        return radiusSq;
    }

    public void setRadiusSq(float radiusSq) {
        this.radiusSq = radiusSq;
    }


}

