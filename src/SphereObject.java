import java.awt.*;

class SphereObject  {
    Vector3 center;
    double radius, radiusSq; // precompute radiusSq since we use it a lot
    static Scene scene;
    static SceneConsts sConsts;
    public boolean shade = true;
    Material material;

    public SphereObject(double x, double y, double z, double r) {
        center = new Vector3(x, y, z);
        radius = r;
        radiusSq = r*r;


    }

    public SphereObject(Vector3 v, double r) {
        center = new Vector3(v.x,v.y,v.z);
        radius = r;
        radiusSq = r*r;


    }

    public SphereObject() {
        center = new Vector3(0,0,0);
        radius = radiusSq = 1;

    }

    public int shade( Vector3 rayDir , Vector3 sceneOrigin, Light light, double t){

        Vector3 intersection, normal, lightDir;
        double intensity;


        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(t);
        intersection.add(sceneOrigin);

        // find surface normal
        normal = new Vector3 (intersection);
        normal.sub(center);
        normal.normalize();

        // get light direction
        lightDir = new Vector3 (light.position);
        lightDir.sub(lightDir,intersection);
        lightDir.normalize();
        double lightDist = center.distance(light.position);
        //System.out.println(lightDist);

        intensity = normal.dotProduct(lightDir)/Math.pow(lightDist+1,2);
        intensity*= light.intensity;
        if(intensity < 0.0)
            intensity = 0.0;

        if(intensity > 1.0)
            intensity = 1.0;

        /* find the corresponding color in the color lookup table */
        intensity = intensity *255;


        int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.color;

        Color shadedLight = new Color((int)(lightColor.getRed()*((double)clampedIntensity/255)), (int)(lightColor.getGreen()*((double)clampedIntensity/255)), (int)(lightColor.getBlue()*((double)clampedIntensity/255)));

        Color objectColor =  new Color((int)(shadedLight.getRed()*material.albedoColor.x),(int)(shadedLight.getGreen()*material.albedoColor.y),(int)(shadedLight.getBlue()*material.albedoColor.z) );



        int pixelCol = objectColor.getRGB();


        return(pixelCol);
    }
/*
    public int shade( Vector3 rayDir , Vector3 sceneOrigin, Light light, double t){

        Vector3 intersection, normal, lightDir;
        double intensity;


        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(t);
        intersection.add(sceneOrigin);

        // find surface normal
        normal = new Vector3 (intersection);
        normal.sub(center);
        normal.normalize();

        // get light direction
        lightDir = new Vector3 (light.position);
        lightDir.sub(lightDir,intersection);
        lightDir.normalize();
        double lightDist = center.distance(light.position);
        //System.out.println(lightDist);






        intensity = normal.dotProduct(lightDir)/Math.pow(lightDist+1,2);
        intensity*= light.intensity;
        if(intensity < 0.0)
            intensity = 0.0;

        if(intensity > 1.0)
            intensity = 1.0;


        intensity = intensity *255;






        return((int)intensity);
    }*/






}