package Objects;

import Util.Material;
import Util.TransformationAnimator;
import application.ApplicationSettings;
import math.TransformationMatrix4x4;
import math.Vector3;
import math.Vector3D;

import static java.lang.Math.random;

public class GeneratedScene extends Scene{
    private int numObjects;
    public GeneratedScene(ApplicationSettings applicationSettings, int numObjects) {
        super(applicationSettings);
        this.numObjects = numObjects;
        initializeScene();
    }

    @Override
    public void initializeScene() {
        // setup Camera
        setUpCamera();
        // setup Keyhandler
        setUpKeyHandler();
        // setup Light
        setUpLight();
        // SetUpGround
        setupGround();
        // Steup manual Objects
        setupManualObjects();
        // generate Scene Objects procedurally
        SceneObject[] generatedObjects = createSceneObjects(numObjects, 0.2f, 0.01f);
        // Configure Scene Objects
        configureObjects(generatedObjects);
    }

    void  setupManualObjects(){
        // Materials
        Material defaultMat;
        Material ellipsoidMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.9f, 1.3f, false);
        // add manually placed objects
        TransformationMatrix4x4 trans = new TransformationMatrix4x4();
        trans.createTranslationMatrix(new Vector3D(1f, 0.5f, 0));
        SceneObject ellipse = new Ellipsoid(0.4, 0.7, 0.4, trans);
        getSceneObjects().add(ellipse);
        // set Scene for Object
        ellipse.setScene(this);
        //set Material
        ellipse.setMaterial(ellipsoidMat);
        //create Animator
        TransformationAnimator anim2 = new TransformationAnimator(ellipse,0.1f, TransformationAnimator.Vector3Type.position,new Vector3(0.0f, 0.11f, 0.0f),10);
        ellipse.getAnimators().add(anim2);

        TransformationMatrix4x4 trans2 = new TransformationMatrix4x4();
        trans2.createTranslationMatrix(new Vector3D(-0.5f, 0.25f, 0.5));
        SceneObject ellipse2 = new Ellipsoid(0.7, 0.4, 0.4, trans2);
        ellipse2.setScene(this);
        ellipse2.setMaterial(ellipsoidMat);
        getSceneObjects().add(ellipse2);
    }

    static SceneObject[] createSceneObjects(int numObjects, float maxRad, float minRad) {
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

    static SceneObject[] createSpheres(int numSpheres, float maxRad, float minRad) {
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
