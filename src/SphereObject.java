import java.awt.*;

class SphereObject extends SceneObject {
    private Vector3 center;
    private double radius, radiusSq; // precompute radiusSq since we use it a lot
// TODO : Clean up shading


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

    public int shadeDiffuse( Vector3 rayDir , Vector3 sceneOrigin, Light light, double t){

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


       // int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int)(lightColor.getRed()*((double)intensity/255)), (int)(lightColor.getGreen()*((double)intensity/255)), (int)(lightColor.getBlue()*((double)intensity/255)));
        Vector3 albedo =  this.getMaterial().getAlbedoColor();
        Color objectColor =  new Color((int)(shadedLight.getRed()*albedo.x),(int)(shadedLight.getGreen()*albedo.y),(int)(shadedLight.getBlue()*albedo.z) );



        int pixelCol = objectColor.getRGB();


        return(pixelCol);
    }

    public boolean intersect( Ray Ray, SceneObject object)
    {

        /*
        a =1\\
        b=2D(Origin-C)
        c=|O-C|^2-R^2    */
        SphereObject sphere = (SphereObject)object;

        Vector3 L = Ray.getOrigin().sub(sphere.getCenter());
        Vector3 dir = Ray.getDirection();
        dir.normalize();
        double a = dir.dotProduct(dir);// directional Vector sq
        double b = 2 * Ray.getDirection().dotProduct(L);
        double c = L.dotProduct(L) - sphere.getRadiusSq();
        double[] quadraticResults = RayTracerSimple.solveQuadratic(a, b, c);

        double  t0 =  quadraticResults[1];
        double  t1 =  quadraticResults[2];
        if (quadraticResults[0] <0){

            return false;
        }

        if (t0 > t1){

            // siehe zu, dass t0 kleiner als t1
            double  tempt0 = t0;
            double  tempt1 = t1;
            t0 = tempt1;
            t1 = tempt0;
        }


        if (t0 < 0) {
            t0 = t1; // if negative, INtersection is behind us
            if (t0 < 0) {
                return false; // both t0 and t1 are negative, keine schnittpunkte
            }
        }
        if (t0< Ray.getT()){
            Ray.setT(t0);
            Ray.setNearest(sphere);
        }


        return true;
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

 /*   public boolean isShade() {
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
    }*/
}