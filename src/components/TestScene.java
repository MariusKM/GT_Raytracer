package components;

import objects.SceneObject;
import objects.SphereObject;
import Util.Material;
import Util.MaterialAnimator;
import Util.TransformationAnimator;
import application.ApplicationSettings;
import math.Vector3;

import static java.lang.Math.random;

public class TestScene extends Scene {
    public TestScene(ApplicationSettings applicationSettings) {
        super(applicationSettings);
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
        // Configure Objects
        configureObjects(GenerateTestObjects());
    }

    private SceneObject[] GenerateTestObjects(){
        //add test Objects
        SceneObject testSphere = new SphereObject(new Vector3(0.5f, 0.5f, 1.05f), 0.3f);
        Material defaultMat = new Material(new Vector3((float) (0.5f), (float) (0.5f), (float) (0.5)), 0.01f, 1, 0f, 1f, true);
        testSphere.setMaterial(defaultMat);

        SphereObject testSphere1 = new SphereObject(new Vector3(0.25f, 1.25f, 0), 0.5f);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.001f, 1f, 0.8f, 1.3f, false);
        testSphere1.setMaterial(defaultMat);

        TransformationAnimator anim = new TransformationAnimator(testSphere1, 0.2f, TransformationAnimator.Vector3Type.scale, new Vector3(0, 0.0f, 0), 10);
        anim.pingPong = true;

        MaterialAnimator animMat = new MaterialAnimator(testSphere1, 0.1f, MaterialAnimator.MaterialValueType.color, new Material(new Vector3(1, 1, 1), 0, 0, 0), 10);
        animMat.pingPong = true;

        testSphere1.getAnimators().add(anim);
        testSphere1.getAnimators().add(animMat);

        SceneObject testSphere2 = new SphereObject(new Vector3(1f, 0.25f, 1.05f), 0.2f);
        SceneObject testSphere3 = new SphereObject(new Vector3(0.0f, 0.25f, 1.05f), 0.2f);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.8f, 1.3f, false);
        testSphere2.setMaterial(defaultMat);
        defaultMat = new Material(new Vector3((float) (random() * 0.5f + 0.5f), (float) (0.5f * random()), (float) (0.2 * random())), 0.01f, 1f, 0.8f, 1.3f, false);
        testSphere3.setMaterial(defaultMat);

        return  new SceneObject[]{
                testSphere,
                testSphere1,
                testSphere2,
                testSphere3
        };
    }

}
