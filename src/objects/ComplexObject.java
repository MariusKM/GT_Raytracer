package objects;

import render.RenderUtil;
import components.Light;
import components.Scene;
import math.Matrix4x4;
import math.MatrixOps;
import math.Vector3;

public class ComplexObject extends SceneObject {

    public enum Operation {VEREINIGUNG, DIFFERENZ, SCHNITT}

    ;
    private Operation operation;
    public Quadrik quadA, quadB;
    public Quadrik intersectObj;
    public Vector3 normal;
    private float Tintersectzion;

    public ComplexObject(Quadrik a, Quadrik b, Operation op) {
        this.quadA = a;
        this.quadB = b;
        this.operation = op;
    }


    /*
     * Berechne die Flächen Normale der Objects.Quadrik bei dem Punkt P
     */
    public Vector3 normal(Vector3 p) {
        Vector3 res = null;

        if (quadA.isInside(p)) {
            res = quadA.normal(p);
        }
        if (quadB.isInside(p)) {
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


        boolean result = false;
        switch (operation) {
            case SCHNITT:
                // zwingend A und B
                result = quadA.intersect(rayA) && quadB.intersect(rayB);

                if (result) {
                    // zweiten Eintrittspunkt wählen
                    Quadrik temp;
                    if (rayA.getT0() < rayB.getT0()) {
                        temp = quadB;
                        Tintersectzion = rayB.getT0();
                    } else {
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

                if (result) {
                    Quadrik temp;
                    if (rayA.getT0() < rayB.getT0()) {
                        temp = quadA;
                        Tintersectzion = rayA.getT0();
                    } else {

                        if (rayA.getT0() < rayB.getT1()) {
                            if (rayB.getT0() < rayA.getT0()) {
                                temp = quadB;
                                Tintersectzion = rayB.getT1();
                            } else {
                                temp = quadA;
                                Tintersectzion = rayA.getT1();
                            }
                        } else {
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
                boolean resultB = quadB.intersect(rayB);
                if (resultA || resultB) {

                    Quadrik temp = (resultA) ? (Quadrik) rayA.getNearest() : (Quadrik) rayB.getNearest();
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
        quadA.setMatrix(MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), quadA.getMatrix()), im));
        quadA.setConstantsFromMatrix();

        quadB.setMatrix(MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), quadB.getMatrix()), im));
        quadB.setConstantsFromMatrix();

    }





    @Override
    public Vector3 shade(Ray ray, Scene currentScene, boolean refl, float depth) {
        Vector3 intersection,intersection2, normal, lightDir;
        float intensity;

        Vector3 sceneOrigin = currentScene.getSceneCam().getPosition();
        float metalness = getMaterial().getMetalness();
        float roughness = getMaterial().getRoughness();
        float roughnessSq = (float) Math.pow(roughness, 2);
        Vector3 albedo = getMaterial().getAlbedoColor();
        // berechne intersection Point

        // not sure about this
        intersection = new Vector3(ray.getDirection());
        intersection.mult(Tintersectzion);
        intersection.add(sceneOrigin);
        ray.intersection1 = intersection;
        intersection2 = new Vector3(ray.getDirection());
        intersection2.mult(Tintersectzion);
        intersection2.add(sceneOrigin);
        ray.intersection2 = intersection2;


        // find surface normal
        normal = intersectObj.normal(intersection);

        // get light direction
        Vector3 finalCol = new Vector3(0,0,0);
        for (Light light: currentScene.getSceneLight()) {
            lightDir = new Vector3(light.getPosition());
            lightDir.sub(lightDir, intersection);
            lightDir.normalize();
            Vector3 currentCol = shader.computeColor(ray,lightDir, normal, this, currentScene, refl, depth);


            intensity = getIntensity(intersection,light,5);
            currentCol.mult(intensity);
            finalCol.add (currentCol);
        }
        return finalCol;
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

            float lightDist = intersection.distance(points[i]);

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

    @Override
    public boolean shadowCheck(Scene scene, Ray myRay) {
        for (SceneObject s : scene.getSceneObjects()) {
            Vector3 offset = new Vector3(myRay.getDirection());
            offset.mult(-1);
            offset.mult(0.00001f);
            offset.add(myRay.getOrigin());
            myRay.setOrigin(offset);
            if (!s.equals(this) && !s.isGizmo()) {
                boolean intersect;
                if (s instanceof Ellipsoid) {
                    intersect = ((Ellipsoid) s).intersect(myRay);
                } else if (s instanceof ComplexObject) {

                    intersect = ((ComplexObject) s).intersect(myRay);
                } else {
                    intersect = s.intersect(myRay);
                }

                if (intersect) {
                    return true;
                }
            }

        }
        return true;
    }
}
