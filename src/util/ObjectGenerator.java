package util;

import math.TransformationMatrix4x4;
import math.Vector3;
import math.Vector3D;
import objects.Ellipsoid;
import objects.SceneObject;
import objects.SphereObject;

import static java.lang.Math.random;

public class ObjectGenerator {

    public static SceneObject[] generateSceneObjects(int numObjects, float maxRad, float minRad) {
        SceneObject[] objects = new SceneObject[numObjects];
        Vector3 objectPos;
        float radiusX, radiusY, radiusZ;
        for (int i = 0; i < numObjects; i++) {
            objectPos = randomVecInRange(-0.5f, 1.5f, 0.25f, 1, 0, 1.5);
            radiusX = (float) (random() * maxRad + minRad);
            radiusY = (float) (random() * maxRad + minRad);
            radiusZ = (float) (random() * maxRad + minRad);
            TransformationMatrix4x4 trans = new TransformationMatrix4x4();
            trans.createTranslationMatrix(new Vector3D(objectPos.x, objectPos.y, objectPos.z));
            objects[i] = new Ellipsoid(radiusX, radiusY, radiusZ, trans);
        }
        return objects;
    }

    public static SceneObject[] createSpheres(int numSpheres, float maxRad, float minRad) {
        SceneObject[] spheres = new SceneObject[numSpheres];
        Vector3 spherePos;
        float sphereRadius;
        for (int i = 0; i < numSpheres; i++) {
            spherePos = randomVecInRange(-0.5f, 1, -0.75f, 1, -0.25, -0.05);
            sphereRadius = (float) (random() * maxRad + minRad);
            spheres[i] = new SphereObject(spherePos, sphereRadius);
        }
        return spheres;
    }

    static Vector3 randomVecInRange(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {

        Vector3 randomVec = new Vector3((float) (random() * xmax + xmin), (float) (random() * ymax + ymin), (float) (random() * zmax + zmin));
        return randomVec;
    }
}
