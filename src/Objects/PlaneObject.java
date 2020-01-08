package Objects;

import Util.RenderUtil;
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

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity)), (int) (lightColor.getGreen() * ((float) intensity)), (int) (lightColor.getBlue() * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));


        int pixelCol = objectColor.getRGB();


        return (pixelCol);
    }


    @Override
    public Vector3 shadeCookTorrance(Ray ray, SceneSimple currentScene, boolean refl, float depth) {
        Vector3 intersection,intersection2, normal, lightDir;
        float intensity;


        Light light = currentScene.getSceneLight();
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
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = pointOnPlane.distance(light.getPosition());

        Vector3 finalCol = RenderUtil.CookTorranceNeu(ray,lightDir, normal, this, currentScene, refl, depth);

        // TODO Multiple Lights
        intensity = getIntensity(intersection,light,1);

        finalCol.mult(intensity);
        return finalCol;

    }

    public boolean shadowCheck(SceneSimple scene, Ray myRay) {
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
