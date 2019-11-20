import math.Constants;
import math.Matrix4x4;
import math.MatrixOps;

import java.awt.*;
import java.util.ArrayList;

public class ComplexObject extends SceneObject {

    private String operation="Vereinigung";
    public ArrayList<Quadrik3> list = new ArrayList<Quadrik3>();
    public Quadrik3 quadA, quadB;
    public Quadrik3 intersectObj;
    public Vector3 normal;

    ComplexObject(Quadrik3 a,Quadrik3 b, String operation ){
        list.add(a);//ugly
        list.add(b);
        this.quadA = a;
        this.quadB = b;
        this.operation=operation;
    }


    /* (non-Javadoc)
     * berechne die Flächen Normale der Quadrik bei dem Punkt P
     */
    public Vector3 normal(Vector3 p) {
        Vector3 res=null;
        for(Quadrik3 a : list) {
           if (a.isInside(p)){
               res = a.normal(p);
           }
        }
        return res;
    }


    @Override
    public boolean intersect(Ray3 ray3) {
        //TODO: Cases für schnittpunkte Regeln, zwei Rays verwenden!!!!
        boolean result =false;
        switch(operation) {
            case "Schnitt":
                // zwingend A und B
                boolean result1 = quadA.intersect(ray3);
                boolean result2 = quadB.intersect(ray3);
                result = result1 && result2 ;
                if(result){
                    Quadrik3 temp = quadB;
                    intersectObj = temp;
                    ray3.setNearest(this);
                }else{
                    ray3.setNearest(null);
                }
                break;

            case "Differenz":
                // quasi: A ohne B
                // A und nicht B A
                result = quadA.intersect(ray3) & !quadB.intersect(ray3);

                if(result){
                    SceneObject temp = ray3.getNearest();
                    if (temp == quadA){

                    }
                    intersectObj = (Quadrik3)temp;
                    ray3.setNearest(this);
                }else{
                    ray3.setNearest(null);
                }

                break;

            default://fall through
            case "Vereinigung":
                // Sobald A oder B
                result = quadA.intersect(ray3) || quadB.intersect(ray3);
                if(result){
                    SceneObject temp = ray3.getNearest();
                    intersectObj = (Quadrik3)temp;
                    ray3.setNearest(this);
                }else{
                    ray3.setNearest(null);
                }
                break;
        }
        //lastIntersection();



        return result;
    }

    /* (non-Javadoc)
     * @see objects.IObject#transform(math.TransformationMatrix4x4)
     */
    public void transform(math.TransformationMatrix4x4 m) {
        //if (list.size()==0) return;
        //Transform each object
        for(Quadrik3 a : list) {
            Matrix4x4 im = m.getInverseMatrix();
            a.setMatrix( MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), a.getMatrix()), im));
            a.setConstantsFromMatrix();
        }
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
        normal = intersectObj.normal(intersection); //normal(intersection);//new Vector3(this.normal);


        // get light direction
        lightDir = new Vector3(light.getPosition());
        lightDir.sub(lightDir, intersection);
        lightDir.normalize();
        float lightDist = intersection.distance(light.getPosition());
        //System.out.println(lightDist);
        Ray3 shadowRay3 = new Ray3(intersection, lightDir);
        boolean shadow = false;//shadowCheck(this.getScene(), shadowRay3);
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

        Color lightColor = light.getColor();

        Color shadedLight = new Color((int) (lightColor.getRed() * ((float) intensity)), (int) (lightColor.getGreen() * ((float) intensity)), (int) (lightColor.getBlue() * ((float) intensity)));
        Vector3 albedo = this.getMaterial().getAlbedoColor();
        Color objectColor = new Color((int) (shadedLight.getRed() * albedo.x), (int) (shadedLight.getGreen() * albedo.y), (int) (shadedLight.getBlue() * albedo.z));

        int pixelCol = objectColor.getRGB();

        return (pixelCol);
    }

    @Override
    public boolean shadowCheck(SceneSimple scene, Ray3 myRay3) {
        for (SceneObject s : scene.getSceneObjects()) {
            if (!s.equals(this) && !s.isGizmo()) {
                boolean intersect = s.intersect(myRay3);

                if (intersect) {
                    return true;
                }
            }

        }
        return  true;
    }
}
