import java.awt.*;

class SphereObject  {
    private Vector3 center;
    private double radius, radiusSq; // precompute radiusSq since we use it a lot
    private boolean shade = true;
    private Material material;

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
        lightDir = new Vector3 (light.getPosition());
        lightDir.sub(lightDir,intersection);
        lightDir.normalize();
        double lightDist = center.distance(light.getPosition());
        //System.out.println(lightDist);

        intensity = normal.dotProduct(lightDir)/Math.pow(lightDist+1,2);
        intensity*= light.getIntensity();
        if(intensity < 0.0)
            intensity = 0.0;

        if(intensity > 1.0)
            intensity = 1.0;


        intensity = intensity *255;


        int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int)(lightColor.getRed()*((double)clampedIntensity/255)), (int)(lightColor.getGreen()*((double)clampedIntensity/255)), (int)(lightColor.getBlue()*((double)clampedIntensity/255)));
        Vector3 albedo =  material.getAlbedoColor();
        Color objectColor =  new Color((int)(shadedLight.getRed()*albedo.x),(int)(shadedLight.getGreen()*albedo.y),(int)(shadedLight.getBlue()*albedo.z) );



        int pixelCol = objectColor.getRGB();


        return(pixelCol);
    }

    public Vector3 getCenter() {
        return center;
    }

    public void setCenter(Vector3 center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadiusSq() {
        return radiusSq;
    }

    public void setRadiusSq(double radiusSq) {
        this.radiusSq = radiusSq;
    }

    public boolean isShade() {
        return shade;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}