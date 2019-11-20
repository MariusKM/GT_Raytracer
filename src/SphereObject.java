import java.awt.*;

class SphereObject extends SceneObject {
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
        normal = new Vector3(intersection);
        normal.sub(center);
        normal.normalize();

        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = center.distance(light.getPosition());


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
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float)(normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity()*5;
        }




        finalCol.mult(intensity);


        //System.out.println(finalCol.toString());
       // Color finalColorRGB = new Color(finalCol.x, finalCol.y, finalCol.z );
        Color finalColorRGB = new Color(RayTracerSimple.clampF(finalCol.x,0,1), RayTracerSimple.clampF(finalCol.y,0,1), RayTracerSimple.clampF(finalCol.z,0,1) );
        int pixelCol = finalColorRGB.getRGB();

        return (pixelCol);
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
        normal.sub(center);
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
            intensity = (float)(normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }

        if (intensity < 0.0)
            intensity = 0.0f;

        if (intensity > 1.0)
            intensity = 1.0f;





        // int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity )), (int) (lightColor.getGreen() * ((float) intensity )), (int) (lightColor.getBlue() * ((float) intensity )));
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
        float[] quadraticResults = RayTracerSimple.solveQuadratic(a, b, c);

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


    public boolean shadowCheck( SceneSimple scene, Ray myRay) {
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

