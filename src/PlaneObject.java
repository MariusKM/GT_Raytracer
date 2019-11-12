import java.awt.*;

public class PlaneObject extends SceneObject {
    private Vector3 pointOnPlane;
    private Vector3 planeNormal;


    public PlaneObject(Vector3 pointOnPlane, Vector3 planeNormal) {
        this.pointOnPlane = pointOnPlane;
        this.planeNormal = planeNormal;
    }

    public boolean intersect(Ray3 Ray3, SceneObject plane) {

        //s = (k – np)/(nv)
        Vector3 normal = new Vector3(this.planeNormal);
        Vector3 rayDir = new Vector3(Ray3.getDirection());
        float zaehler = normal.dotProduct(rayDir);

        Vector3 vecToOrigin = this.pointOnPlane.sub(Ray3.getOrigin());
        float t = vecToOrigin.dotProduct(normal) / zaehler;
        if (t >= 0) {
            if (t < Ray3.getT0()) {
                Ray3.setT0(t);
                Ray3.setNearest(plane);
            }
            Ray3.setT0(t);
            return true;
        }


        return false;
    }

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

        Ray3 shadowRay3 = new Ray3(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay3);
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float)(normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }


        if (intensity < 0.0)
            intensity = 0.0f;

        if (intensity > 1.0)
            intensity = 1.0f;







        Color lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity )), (int) (lightColor.getGreen() * ((float) intensity)), (int) (lightColor.getBlue() * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));


        int pixelCol = objectColor.getRGB();


        return (pixelCol);
    }

    public boolean shadowCheck(SceneSimple scene, Ray3 myRay3) {
        for (SceneObject s : scene.getSceneObjects()) {
            if (!s.equals(this) && !s.isGizmo()) {
                boolean intersect = s.intersect(myRay3, s);

                if (intersect) {
                    return true;
                }
            }
        }

        return false;
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
