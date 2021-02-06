package objects;

import Util.RenderUtil;
import components.Light;
import components.Scene;
import math.Vector3;

import java.awt.*;

public class PlaneObject extends SceneObject {
    private Vector3 pointOnPlane;
    private Vector3 planeNormal;

    public PlaneObject(Vector3 pointOnPlane, Vector3 planeNormal) {
        this.pointOnPlane = pointOnPlane;
        this.planeNormal = planeNormal;
    }

    @Override
    public boolean intersect(Ray Ray) {
        //s = (k – np)/(nv)
        Vector3 normal = new Vector3(this.planeNormal);
        Vector3 rayDir = new Vector3(Ray.getDirection());
        float zaehler = normal.dotProduct(rayDir);

        Vector3 vecToOrigin = this.pointOnPlane.sub(Ray.getOrigin());
        float t = vecToOrigin.dotProduct(normal) / zaehler;
        if (t >= 0) {
            if (t < Ray.getT0()) {
                Ray.setT0(t);
                Ray.setNearest(this);
            }
            Ray.setT0(t);
            return true;
        }

        return false;
    }

    @Override
    public int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {

        Vector3 intersection, normal, lightDir;
        float intensity;

        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(t);
        intersection.add(sceneOrigin);



        // find surface normal
        normal = new Vector3(planeNormal);


        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = pointOnPlane.distance(light.getPosition());
        //System.out.println(lightDist);

        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = false;//shadowCheck(this.getScene(), shadowRay);
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

        Vector3 lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.x* ((float) intensity)), (int) (lightColor.y * ((float) intensity)), (int) (lightColor.z * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));


        int pixelCol = objectColor.getRGB();


        return (pixelCol);
    }


    @Override
    public Vector3 shadeCookTorrance(Ray ray, Scene currentScene, boolean refl, float depth) {
        Vector3 intersection,intersection2, normal, lightDir;
        float intensity;



        Vector3 sceneOrigin = currentScene.getSceneCam().getPosition();
        // berechne intersection Point

        // berechne intersection Point
        intersection = new Vector3(ray.getDirection());
        intersection.mult(ray.getT0());
        intersection.add(sceneOrigin);
        ray.intersection1 = intersection;
        intersection2 = new Vector3(ray.getDirection());
        intersection2.mult(ray.getT1());
        intersection2.add(sceneOrigin);
        ray.intersection2 = intersection2;

        // find surface normal
        normal = new Vector3(planeNormal);
        setNormal(normal);
        // get light direction
        Vector3 finalCol = new Vector3(0,0,0);
        for (Light light: currentScene.getSceneLight()) {
            lightDir = new Vector3(light.getPosition());
            lightDir.sub(lightDir, intersection);
            lightDir.normalize();
            float lightDist = pointOnPlane.distance(light.getPosition());

            Vector3 currentCol = RenderUtil.CookTorranceNeu(ray,lightDir, normal, this, currentScene, refl, depth);

            Vector3 lightCol = light.getColor();
            intensity = getIntensity(intersection,light,2);
            currentCol.mult(intensity);
            Vector3 computedCol = new Vector3(lightCol.x *currentCol.x ,lightCol.y*currentCol.y ,lightCol.z *currentCol.z );
            finalCol.add (computedCol);
        }
        return finalCol;

    }

    public boolean shadowCheck(Scene scene, Ray myRay) {
        boolean shadow = RenderUtil.shadowCheck(scene, myRay, this);
        return shadow;
    }

    public float getIntensity(Vector3 intersection, Light light, int numPoints){
        // SHADOWS && INTENSITY


        // generate points on sphere
        Vector3 [] points = new Vector3[numPoints];
        for (int i = 0; i <numPoints ; i++){
            points[i]=  RenderUtil.randomSpherePoint(light.getVolume());
        }

        float totalIntensity = 0;

        for (int i = 0; i <numPoints ; i++){
            float intensity  = 0 ;
            Vector3 lightDir;
            // get random light direction
            lightDir = new Vector3(points[i]);
            lightDir.sub(lightDir, intersection);
            lightDir.normalize();

            float lightDist = pointOnPlane.distance(points[i]);

            Ray shadowRay = new Ray(intersection, lightDir);

            boolean shadow = shadowCheck(this.getScene(), shadowRay);
            if (shadow) {
                intensity = 0;

            } else {
                intensity = (float) (getNormal().dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
                intensity *= light.getIntensity();
            }
            totalIntensity+= intensity;

        }


        return  totalIntensity/(float)numPoints;
    }


    public Vector3 getPointOnPlane() {
        return pointOnPlane;
    }

    public void setPointOnPlane(Vector3 pointOnPlane) {
        this.pointOnPlane = pointOnPlane;
    }

    public Vector3 getPlaneNormal() {
        return planeNormal;
    }

    public void setPlaneNormal(Vector3 planeNormal) {
        this.planeNormal = planeNormal;
    }


}
