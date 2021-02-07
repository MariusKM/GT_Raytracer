package util;

import components.Scene;
import math.Vector3;
import objects.Ray;
import objects.SceneObject;
import objects.SphereObject;

import java.util.Random;

public abstract class ShadingModel {

    static float F0 = 0.8f;

    static float k = 0.2f;

    static float indexLuft = 1.0f;
    static float indexGlas = 1.5f;
    static float indexWasser = 1.3f;
    static boolean totalReflexion = false;

    static float  refrA, refrB;

    public abstract Vector3 computeColor(Ray ray, Vector3 lightDir, Vector3 normal, SceneObject objectToShade, Scene currentScene, boolean refl, float depth);


    public static Vector3 getRefractionVector(Vector3 rayDir, Vector3 normal, SceneObject objectToShade) {

        Material mat = objectToShade.getMaterial();
        float i1 = indexLuft;
        float i2 = mat.getRefractiveIndex();

        // i = i1/i2;
        float i = i1 / i2;
        // kommt das hier vor dem check oder danach?
        // a = v1 * N
        Vector3 v1 = new Vector3(rayDir);
        refrA = normal.dotProduct(v1);
        // Erkennung über a = v · n möglich: a < 0? Dann a = -a | sonst a > 0? Dann n = -n
        // Frage hier: ist hier auch a = -v1 * n gemeint ode v1 *n

        if (refrA < 0) refrA = -refrA;
        else {
            normal.mult(-1);
        }
        if (objectToShade.getMaterial().isTransparent()){
            objectToShade.setNormal(normal);
        }


        // b = sqrrt(1-i^2(1-a^2))
        float b1 = (float) (1 - Math.pow(i, 2));
        float b2 = (float) (1 - Math.pow(refrA, 2));
        float b3 = b1 * b2;
        if (b3 < 0) {
            totalReflexion = true;
        }

        refrB = (float) Math.sqrt(b3);
        // v2 = i*v1 + (i*a-b)*n
        Vector3 V2 = new Vector3(rayDir);
        V2.mult(i);
        float termV2 = i * refrA - refrB;
        Vector3 normalV2 = new Vector3(normal);
        normalV2.mult(termV2);

        V2.add(normalV2);

        return V2;
    }

    public static Vector3 getReflexionVector(Vector3 rayDir, Vector3 normal, SceneObject objectToShade) {

        //Reflexionsrichtung berechnet sich als r = v – 2(n·v)n
        Vector3 n1 = new Vector3(normal);
        float NdotV = n1.dotProduct(rayDir);
        NdotV *= 2;

        n1.mult((NdotV));
        Vector3 reflDir = new Vector3(rayDir);
        reflDir.sub(reflDir, n1);


        return reflDir;
    }


    public static Vector3 getColRecursive(Vector3 rayDir, Vector3 intersection, SceneObject objectToShade, Scene currentScene, float depth, boolean refraction) {

        //strahl von p starten mit Richtung r
        Ray ray = new Ray(intersection, rayDir);

        Vector3 offset = new Vector3(objectToShade.getNormal());
        // bei refraktion rein rücken
        if (refraction) offset.mult(-1);

        offset.mult(0.01f);
        offset.add(ray.getOrigin());
        ray.setOrigin(offset);

        boolean intersect = false;
        for (SceneObject s : currentScene.getSceneObjects()) {
            if ( !s.isGizmo()) {
                intersect = s.intersect(ray);
            }
        }

        // Background Color if nothing is hit
        Vector3 Color = new Vector3(((float) currentScene.getBgCol().getRed()) / 255, ((float) currentScene.getBgCol().getGreen()) / 255, ((float) currentScene.getBgCol().getBlue()) / 255);
        if (ray.getNearest() != null) {
            SceneObject temp = ray.getNearest();
            SceneObject intersectObj = temp;
            Color = intersectObj.shade(ray, currentScene, true, depth);
        }
        return Color;
    }

    public static Vector3 getPointinSphere(SphereObject sphereObject) {
        var u = sphereObject.getRadius();
        // create random object
        java.util.Random R = new Random();

        var x1 = R.nextGaussian();
        var x2 = R.nextGaussian();
        var x3 = R.nextGaussian();

        var mag = Math.sqrt(x1 * x1 + x2 * x2 + x3 * x3);
        x1 /= mag;
        x2 /= mag;
        x3 /= mag;

        // Math.cbrt is cube root
        var c = Math.cbrt(u);
        Vector3 point = new Vector3((float) (x1 * c), (float) (x2 * c), (float) (x3 * c));
        point.add(sphereObject.getCenter());
        return point;
    }

    public static Vector3 randomSpherePoint(SphereObject sphereObject ){
        float radius = sphereObject.getRadius();
        var u = Math.random();
        var v = Math.random();
        var theta = 2 * Math.PI * u;
        var phi = Math.acos(2 * v - 1);

        var x = sphereObject.getCenter().x + (radius * Math.sin(phi) * Math.cos(theta));
        var y = sphereObject.getCenter().y + (radius * Math.sin(phi) * Math.sin(theta));
        var z = sphereObject.getCenter().z + (radius * Math.cos(phi));

        return new Vector3((float) x,(float)y,(float)z);
    }


    public static boolean shadowCheck(Scene myScene, Ray myRay, SceneObject castingObject) {
        for (SceneObject s : myScene.getSceneObjects()) {
            Vector3 offset = new Vector3(myRay.getDirection());
            offset.mult(-1);
            offset.mult(0.00001f);
            offset.add(myRay.getOrigin());
            myRay.setOrigin(offset);

            if (!s.equals(castingObject) && !s.isGizmo()) {
                boolean intersect = s.intersect(myRay);

                if (intersect) {
                    return true;
                }
            }
        }
        return false;
    }
}
