package Objects;

import Util.RenderUtil;
import Util.MathUtil;
import math.Vector;
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
    public Vector3 shadeCookTorrance(Ray ray, SceneSimple currentScene, boolean refl, float depth) {

        Vector3 intersection,intersection2, normal;
        float intensity;

        Vector3 sceneOrigin = currentScene.getSceneCam().getPosition();

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
        normal = new Vector3(intersection);
        normal.sub(normal, getCenter());
        normal.normalize();
        setNormal(normal);
        Vector3 finalCol = new Vector3(0,0,0);
        for (Light light: currentScene.getSceneLight()) {
            Vector3 lightDir;
            // get light direction
            lightDir = new Vector3(light.getPosition());
            lightDir.sub(lightDir, intersection);
            lightDir.normalize();

            Vector3 currentCol = RenderUtil.CookTorranceNeu(ray,lightDir, normal, this, currentScene, refl, depth);

            Vector3 lightCol = light.getColor();
            intensity = getIntensity(intersection,light,5);
            currentCol.mult(intensity);
            Vector3 computedCol = new Vector3(lightCol.x *currentCol.x ,lightCol.y*currentCol.y ,lightCol.z *currentCol.z );
            finalCol.add (computedCol);
        }

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

        Vector3 lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.x* ((float) intensity)), (int) (lightColor.y * ((float) intensity)), (int) (lightColor.z * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));

        int pixelCol = objectColor.getRGB();

        return (pixelCol);
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

            float lightDist = center.distance(points[i]);

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
        float b = 2 * dir.dotProduct(L);
        float c = L.dotProduct(L) - sphere.getRadiusSq();
        float[] quadraticResults = MathUtil.solveQuadratic(a, b, c);

        float t0 = quadraticResults[1];
        float t1 = quadraticResults[2];

        if (quadraticResults[0] < 0) {
            return false;
        }

        if (t0 < 0) {
            t0 = t1; // if negative, Intersection is behind us
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, keine schnittpunkte
            }
        }

        if (t0 < Ray.getT0()) {
            Ray.setT0(t0);
            Ray.setT1(t1);
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
        this.radiusSq = radius*radius;
    }

    public float getRadiusSq() {
        return radiusSq;
    }

    public void setRadiusSq(float radiusSq) {
        this.radiusSq = radiusSq;
    }

}

