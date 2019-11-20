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
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
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

    @Override
    public int shadeCookTorrance(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {
        Vector3 intersection, normal, lightDir;
        float intensity;

        float metalness = getMaterial().getMetalness();
        float roughness = getMaterial().getRoughness();
        float roughnessSq = (float)Math.pow(roughness,2);
        Vector3 albedo = getMaterial().getAlbedoColor();
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

        // D

        // H = (V+L)/2
        Vector3 H = new Vector3(rayDir);
        H.mult(-1);
        H.add(lightDir);
        H.mult(0.5f);

        // D = 𝑟^2/ 𝜋 ((𝑁∙𝐻)^2 (r^2-1)+1)^2


        Vector3 normalD = new Vector3(normal);
        float nennerD = (float)(Math.PI*Math.pow((Math.pow(normalD.dotProduct(H),2) * (roughnessSq-1)+1),2));

        float D = roughnessSq/nennerD;

        // F

        //(1 – metalness) * 0.04f
        float termF0 = (1-metalness) * 0.04f;
        //metalness * albedo
        Vector3 F0 =  new Vector3(albedo);
        F0.mult(metalness);
        // F0 = (1 – metalness) * 0.04f + metalness * albedo
        Vector3 termF0v = new Vector3(termF0,termF0,termF0);
        F0.add(termF0v);


        Vector3 normalF = new Vector3(normal);
        //termF =(1 – N·V)^5
        float dotNormalF = normalF.dotProduct(rayDir);
        float termF = (float)Math.pow(1 - (dotNormalF),5);

        //F = F0+ (1 – F0)(1 – N·V)^5
        Vector3 F02 = new Vector3(1,1,1).sub(F0);
        Vector3 F = new Vector3(F0);
        F.add(F02);
        F.mult(termF);

        //kd = (1 – F)(1 – metallness)
        Vector3 kd = new Vector3(1,1,1).sub(F);
        kd.mult(1-metalness);


        //G
        float halfRoughness = roughness/2;
        // termG1 = N·V / (N·V(1 – r/2) + r/2);
        Vector3 normalG = new Vector3(normal);
        float termG1 =  normalG.dotProduct(rayDir)/ ( normalG.dotProduct(rayDir)*(1-(halfRoughness))+(halfRoughness));

        //termG2 =  N·L / (N·L(1 – r/2) + r
        float termG2 = normalG.dotProduct(lightDir)/(normalG.dotProduct(lightDir)*(1-halfRoughness)+roughness);

        //G = N·V / (N·V(1 – r/2) + r/2) * N·L / (N·L(1 – r/2) + r/

        float G = termG1 * termG2;

        // Farbe = (N·L)(kd* albedo + D * F * G)
        Vector3 normalCol = new Vector3(normal);

        float t1 = normalCol.dotProduct(lightDir);
        Vector3 diffusLicht = new Vector3(kd.x * albedo.x , kd.y * albedo.y, kd.z *albedo.z);

        Vector3 glanzLicht = new Vector3(F);
        glanzLicht.mult(D);
        glanzLicht.mult(G);

        Vector3 finalCol =  new Vector3(diffusLicht) ;
        finalCol.add(glanzLicht);

        finalCol.mult(t1);

        // SHADOWS && INTENSITY
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow =false; //shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float)(normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }





        finalCol.mult(intensity);

         //System.out.println(finalCol.toString());
         Color finalColorRGB = new Color(RayTracerSimple.clampF(finalCol.x,0,1), RayTracerSimple.clampF(finalCol.y,0,1), RayTracerSimple.clampF(finalCol.z,0,1) );
        //Color finalColorRGB = new Color(finalCol.x, finalCol.y, finalCol.z );
        int pixelCol = finalColorRGB.getRGB();

        return (pixelCol);
    }

    public boolean shadowCheck(SceneSimple scene, Ray myRay) {
        for (SceneObject s : scene.getSceneObjects()) {
            if (!s.equals(this) && !s.isGizmo()) {
                boolean intersect = s.intersect(myRay);

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
