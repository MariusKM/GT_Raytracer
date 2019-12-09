package Objects;

import Util.RenderUtil;
import math.MathUtil;
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
    public int shadeCookTorrance(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {
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

        Vector3 rayDirN = new Vector3(rayDir);
        rayDirN.mult(-1);

        Vector3 finalCol = RenderUtil.CookTorrance(lightDir,normal, rayDirN, getMaterial());

        // SHADOWS && INTENSITY
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }


        finalCol.mult(intensity);

        //System.out.println(finalCol.toString());
        Color finalColorRGB = new Color(MathUtil.clampF(finalCol.x, 0, 1), MathUtil.clampF(finalCol.y, 0, 1), MathUtil.clampF(finalCol.z, 0, 1));
        //Color finalColorRGB = new Color(finalCol.x, finalCol.y, finalCol.z );
        int pixelCol = finalColorRGB.getRGB();

        return (pixelCol);
    }

    public boolean shadowCheck(SceneSimple scene, Ray myRay) {
        boolean shadow = RenderUtil.shadowCheck(scene, myRay, this);
        return shadow;
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
