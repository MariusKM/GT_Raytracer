package Objects;

import Util.RenderUtil;
import math.*;

import java.awt.*;

public class Quadrik extends SceneObject {


    // a*x^2+b*y^2+c*z^2+2*d*x*y+2*e*x*z+2*f*y*z+2*g*x+2*h*y+2*j*z+k<=0
    private double a, b, c, d, e, f, g, h, j, k;


    private Matrix4x4 matrix; // constants as matrix


    public Quadrik(double a, double b, double c,
                   double d, double e, double f,
                   double g, double h, double j, double k) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.j = j;
        this.k = k;
        setMatrixFromConstants();
    }


    /**
     * sets the parameter matrix
     */
    private void setMatrixFromConstants() {
        matrix = new Matrix4x4(a, d, e, g,
                d, b, f, h,
                e, f, c, j,
                g, h, j, k);
    }

    /**
     * sets parameter from the parameter matrix
     */
    public void setConstantsFromMatrix() {
        a = matrix.m00;
        b = matrix.m11;
        c = matrix.m22;
        d = matrix.m01;
        e = matrix.m02;
        f = matrix.m12;
        g = matrix.m03;
        h = matrix.m13;
        j = matrix.m23;
        k = matrix.m33;
    }


    /*
     * Berechne den Wert der Objects.Quadrik an dem Punkt P
     */
    public double q(Point3D p) {
        return a * p.x * p.x + b * p.y * p.y + c * p.z * p.z + 2.0 * (d * p.x * p.y + e * p.x * p.z + f * p.y * p.z + g * p.x + h * p.y + j * p.z) + k;
    }

    /*
     * Schaue ob ein Punkt in der Objects.Quadrik liegt
     */
    public boolean isInside(Vector3 p) {
        double sum = a * p.x * p.x + b * p.y * p.y + c * p.z * p.z + 2.0 * (d * p.x * p.y + e * p.x * p.z + f * p.y * p.z + g * p.x + h * p.y + j * p.z) + k;
        return sum<= 0.0;
    }

    /*
     * berechne die Flächen Normale der Objects.Quadrik bei dem Punkt P
     */
    public Vector3 normal(Vector3 p) {
        Vector3 v = new Vector3((float) (g + a * p.x + d * p.y + e * p.z),
                (float) (h + d * p.x + b * p.y + f * p.z),
                (float) (j + e * p.x + f * p.y + c * p.z));
        v.normalize();
        return v;
    }


    @Override
    public boolean intersect(Ray ray) {
        double p1, p2, p3, r1, r2, r3;
        // Start Vektor
        p1 = ray.getOrigin().x;
        p2 = ray.getOrigin().y;
        p3 = ray.getOrigin().z;

        // Richtungs Vektor
        r1 = ray.getDirection().x;
        r2 = ray.getDirection().y;
        r3 = ray.getDirection().z;
        //  Aqt^2 +Bqt^2 +Cq = 0 Einzelne Terme berechnen

        // nach Aq (s)
        double Aq = r1 * r1 * a + r2 * r2 * b + r3 * r3 * c + 2.0 * (r1 * r2 * d + r1 * r3 * e + r2 * r3 * f); // (
        // nach Bq (t)
        double Bq = p1 * r1 * a + p2 * r2 * b + p3 * r3 * c + (p1 * r2 + p2 * r1) * d + (p1 * r3 + p3 * r1) * e + (p2 * r3 + p3 * r2) * f + r1 * g + r2 * h + r3 * j;
        // nach  Cq ( u)
        double Cq = p1 * p1 * a + p2 * p2 * b + p3 * p3 * c + 2.0 * (p1 * p2 * d + p1 * p3 * e + p2 * p3 * f + p1 * g + p2 * h + p3 * j) + k;
        // Term berechnen  ((Bq2 - 4AqCq) ABC Formel
        double D = Bq * Bq - Aq * Cq;

        // checken ob D kleiner null, oder Aq gegen null, dann keinen schnittPunkt
        if ((D < 0.0) || (Math.abs(Aq) < Constants.nearzero)) {
            return false;
        }
        // Term berechnen
        double sqrtD = Math.sqrt(D);
        // Schnittpunkt 1 berechnen
        double t1 = (-Bq - sqrtD) / Aq;
        // Schnittpunkt 2 berechnen
        double t2 = (-Bq + sqrtD) / Aq;

        // siehe zu dass der kleiner Schnittpunkt der erste ist
        if (t2 < t1) {
            double temp = t1;
            t1 = t2;
            t2 = temp;
        }
        // wenn t1 <= 0 dann keinen schnittpunkt
        if (t1 < 0.0) {
            return false;
        }

        if (isInside(ray.getPoint(0.5f * (float) (t1 + t2)))) {
            ray.setT0((float) (t1 - Constants.nearzero));
            ray.setT1((float) (t2 + Constants.nearzero));
            ray.setNearest(this);
            return true;
        } else {
            if (isInside(ray.getOrigin())) {
                ray.setT0(0);
                ray.setT1((float) (t1 + Constants.nearzero));
                return true;

            } else {
                ray.setT0((float) (t1 + Constants.nearzero));
                ray.setT1((float) (t2 - Constants.nearzero));
                ray.setNearest(this);
                return true;
            }

        }
    }

    public boolean intersectBody(Ray ray) {
        double p1, p2, p3, r1, r2, r3;
        // Start Vektor
        p1 = ray.getOrigin().x;
        p2 = ray.getOrigin().y;
        p3 = ray.getOrigin().z;
        // Richtungs Vektor
        r1 = ray.getDirection().x;
        r2 = ray.getDirection().y;
        r3 = ray.getDirection().z;
        //  Aqt^2 +Bqt^2 +Cq = 0 Einzelne Terme berechnen

        // nach Aq (s)
        double Aq = r1 * r1 * a + r2 * r2 * b + r3 * r3 * c + 2.0 * (r1 * r2 * d + r1 * r3 * e + r2 * r3 * f); // (
        // nach Bq (t)
        double Bq = p1 * r1 * a + p2 * r2 * b + p3 * r3 * c + (p1 * r2 + p2 * r1) * d + (p1 * r3 + p3 * r1) * e + (p2 * r3 + p3 * r2) * f + r1 * g + r2 * h + r3 * j;
        // nach  Cq ( u)
        double Cq = p1 * p1 * a + p2 * p2 * b + p3 * p3 * c + 2.0 * (p1 * p2 * d + p1 * p3 * e + p2 * p3 * f + p1 * g + p2 * h + p3 * j) + k;
        // Term berechnen  ((Bq2 - 4AqCq) ABC Formel
        double D = Bq * Bq - Aq * Cq;

        // checken D kleiner null, oder Aq gegen null, dann keinen schnittPunkt
        if ((D >= 0.0) || (Math.abs(Aq) < Constants.nearzero)) {
            return false;
        }
        // Term berechnen
        double sqrtD = Math.sqrt(D);
        // Schnittpunkt 1 berechnen
        double t1 = (-Bq - sqrtD) / Aq;
        // Schnittpunkt 2 berechnen
        double t2 = (-Bq + sqrtD) / Aq;

        // siehe zu dass der kleiner Schnittpunkt der erste ist
        if (t2 < t1) {
            double temp = t1;
            t1 = t2;
            t2 = temp;
        }
        // wenn t2 <= 0 dann keinen schnittpunkt
        if (t2 < 0.0) {
            return false;
        }

        if (isInside(ray.getPoint(0.5f * (float) (t1 + t2)))) {
            ray.setT0((float) (t1 - Constants.nearzero));
            ray.setT1((float) (t2 + Constants.nearzero));
            ray.setNearest(this);
            return true;

        } else {
            if (isInside(ray.getOrigin())) {
                ray.setT0(0);
                ray.setT1((float) (t1 + Constants.nearzero));

                return true;
            } else {
                ray.setT0((float) (t1 + Constants.nearzero));
                ray.setT1((float) (t2 - Constants.nearzero));
                ray.setNearest(this);
                return true;
            }

        }
}

    public void transform(math.TransformationMatrix4x4 m) {
        Matrix4x4 im = m.getInverseMatrix();
        matrix = MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), matrix),im);
        setConstantsFromMatrix();
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
        normal = normal(intersection);


        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = intersection.distance(light.getPosition());
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
    public Vector3 shadeCookTorrance(Ray ray,Vector3 rayDirN, SceneSimple currentScene,boolean refl, float depth) {
        Vector3 intersection, normal, lightDir;
        float intensity;
        Light light = currentScene.getSceneLight();
        Vector3 sceneOrigin = currentScene.getSceneCam().getPosition();
        float metalness = getMaterial().getMetalness();
        float roughness = getMaterial().getRoughness();
        float roughnessSq = (float)Math.pow(roughness,2);
        Vector3 albedo = getMaterial().getAlbedoColor();


        // berechne intersection Point
        if (getMaterial().isTransparent()) {
            intersection = new Vector3(ray.getDirection());
            intersection.mult(ray.getT2Nearest());
            intersection.add(sceneOrigin);
        }else{
            intersection = new Vector3(ray.getDirection());
            intersection.mult(ray.getT0());
            intersection.add(sceneOrigin);
        }

        // find surface normal
        normal = normal(intersection); //normal(intersection);//new math.Vector3(this.normal);



        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = intersection.distance(light.getPosition());



        Vector3 finalCol = RenderUtil.CookTorranceNeu(lightDir, normal, ray.getDirection(), rayDirN, intersection, this, currentScene, refl, depth);
        // TODO Multiple Lights
        // SHADOWS && INTENSITY
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;

        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity *= light.getIntensity();
        }

        finalCol.mult(intensity);
        return finalCol;
    }

    @Override
    public boolean shadowCheck(SceneSimple scene, Ray myRay) {
        for (SceneObject s : scene.getSceneObjects()) {
            Vector3 offset = new Vector3(myRay.getDirection());
            offset.mult(-1);
            offset.mult(0.00001f);
            offset.add(myRay.getOrigin());
            myRay.setOrigin(offset);
            if (!s.equals(this) && !s.isGizmo()) {
                boolean intersect;
                if(s instanceof Ellipsoid){
                     intersect =((Ellipsoid) s).intersect(myRay);
                }else{
                     intersect = s.intersect(myRay);
                }
                if (intersect) {
                    return true;
                }
            }

        }
        return  true;
    }
    public void setMatrix(Matrix4x4 matrix) {
        this.matrix = matrix;
    }
    public Matrix4x4 getMatrix() {
        return matrix;
    }
}

