package Objects;

import Util.RenderUtil;
import math.Matrix4x4;
import math.MatrixOps;
import math.Vector3;

import java.awt.*;

public class ComplexObject extends SceneObject {

    public enum Operation{VEREINIGUNG, DIFFERENZ, SCHNITT };
    private Operation operation;
    public Quadrik quadA, quadB;
    public Quadrik intersectObj;
    public Vector3 normal;
    private float Tintersectzion;

    public ComplexObject(Quadrik a, Quadrik b, Operation op){
        this.quadA = a;
        this.quadB = b;
        this.operation = op;
    }


    /*
     * Berechne die Flächen Normale der Objects.Quadrik bei dem Punkt P
     */
    public Vector3 normal(Vector3 p) {
       Vector3 res = null;

       if (quadA.isInside(p)){
           res = quadA.normal(p);
       }
       if (quadB.isInside(p)){
           res = quadB.normal(p);
       }
       return res;
    }


    @Override
    public boolean intersect(Ray ray) {
        //TODO: Cases für schnittpunkte Regeln, zwei Rays verwenden!!!!
        Ray rayA, rayB;
        rayA = new Ray(ray);
        rayB = new Ray(ray);


        boolean result =false;
        switch(operation) {
            case SCHNITT:
                // zwingend A und B
                result =  quadA.intersect(rayA) && quadB.intersect(rayB);

                if(result){
                    // zweiten Eintrittspunkt wählen
                    Quadrik temp;
                    if (rayA.getT0()<rayB.getT0()){
                         temp = quadB;
                         Tintersectzion = rayB.getT0();
                    }else{
                        temp = quadA;
                        Tintersectzion = rayA.getT0();
                    }
                    intersectObj = temp;
                    ray.setNearest(this);
                }
                break;

            case DIFFERENZ:
                // quasi: A ohne B
                // A und nicht B A

                result = quadA.intersect(rayA) & !quadB.intersect(rayB);

                if(result){
                    Quadrik temp;
                    if (rayA.getT0()<rayB.getT0()){
                        temp = quadA;
                        Tintersectzion = rayA.getT0();
                    }else{

                        if (rayA.getT0() < rayB.getT1()){
                           if (rayB.getT0()< rayA.getT0()){
                               temp = quadB;
                               Tintersectzion = rayB.getT1();
                           }else{
                               temp = quadA;
                               Tintersectzion = rayA.getT1();
                           }
                        }else{
                            temp = quadA;
                            Tintersectzion = rayA.getT0();
                        }

                    }
                    intersectObj = temp;
                    ray.setNearest(this);
                }
                break;

            default://fall through
            case VEREINIGUNG:
                // Sobald A oder B
                boolean resultA = quadA.intersect(rayA);
                boolean resultB =  quadB.intersect(rayB);
                if(resultA||resultB){

                    Quadrik temp = (resultA) ? (Quadrik)rayA.getNearest(): (Quadrik)rayB.getNearest();
                    intersectObj = temp;
                    ray.setNearest(this);
                }
                break;
        }

        return result;
    }

    public void transform(math.TransformationMatrix4x4 m) {
        //if (list.size()==0) return;
        //Transform each object

        Matrix4x4 im = m.getInverseMatrix();
        quadA.setMatrix( MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), quadA.getMatrix()), im));
        quadA.setConstantsFromMatrix();

        quadB.setMatrix( MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), quadB.getMatrix()), im));
        quadB.setConstantsFromMatrix();

    }

    @Override
    public int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {
        Vector3 intersection, normal, lightDir;
        float intensity;


        // berechne intersection Point
        intersection = new Vector3(rayDir);
        intersection.mult(Tintersectzion);
        intersection.add(sceneOrigin);

        // find surface normal
        normal = intersectObj.normal(intersection);

        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = intersection.distance(light.getPosition());
        //System.out.println(lightDist);
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = false;
        if (shadow) {
            intensity = 0;
            return Color.black.getRGB();
        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity = intensity * (float) light.getIntensity();
        }

        if (intensity < 0.0)
            intensity = 0.0f;

        if (intensity > 1.0)
            intensity = 1.0f;


        // int clampedIntensity = RayTracerSimple.clamp((int)intensity,0, 255);

        Color lightColor = light.getColor();
        //quadrieren
        Color entgammasiertColor = new Color((int)Math.pow(lightColor.getAlpha(),2), (int) Math.pow(lightColor.getRed(),2),(int) Math.pow(lightColor.getGreen(),2), (int)Math.pow(lightColor.getBlue(),2));
        lightColor = entgammasiertColor;
        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity)), (int) (lightColor.getGreen() * ((float) intensity)), (int) (lightColor.getBlue() * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        //Without Gamma
        // Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));
        //Wurzel ziehen Gammakorrektur
        Color objectColor = new Color((int) (Math.sqrt(shadedLight.getRed() * albedo.x)), (int) (Math.sqrt(shadedLight.getGreen() * albedo.y)), (int) (Math.sqrt(shadedLight.getBlue() * albedo.z)));

        int pixelCol = objectColor.getRGB();

        return (pixelCol);
    }


    @Override
    public Vector3 shadeCookTorrance(Ray ray, Vector3 rayDirN,SceneSimple currentScene,boolean refl, float depth) {
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
            //TODO not sure about this
            intersection.mult(ray.getT2Nearest());
            intersection.add(sceneOrigin);
        }else{
            intersection = new Vector3(ray.getDirection());
            intersection.mult(Tintersectzion);
            intersection.add(sceneOrigin);
        }

        // find surface normal
        normal = intersectObj.normal(intersection);

        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = intersection.distance(light.getPosition());


        Vector3 finalCol = RenderUtil.CookTorranceNeu(lightDir,normal, ray.getDirection(),rayDirN,intersection,this, currentScene,refl,depth);

        // SHADOWS && INTENSITY
        Ray shadowRay = new Ray(intersection, lightDir);
        boolean shadow = shadowCheck(this.getScene(), shadowRay);
        if (shadow) {
            intensity = 0;

        } else {
            intensity = (float) (normal.dotProduct(lightDir) / Math.pow(lightDist + 1, 2));
            intensity  *= light.getIntensity();
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
                }else if ( s instanceof ComplexObject) {

                    intersect =((ComplexObject) s).intersect(myRay);
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
}
