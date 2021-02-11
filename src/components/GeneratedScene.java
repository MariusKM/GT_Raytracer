package components;

import application.ApplicationSettings;
import objects.ObjectGenerator;
import objects.SceneObject;

public class GeneratedScene extends Scene {
    private int numObjects;
    public GeneratedScene(ApplicationSettings applicationSettings, int numObjects) {
        super(applicationSettings);
        this.numObjects = numObjects;
        initializeScene();
    }

    @Override
    public void initializeScene() {
        setUpEnvironment();
        // Steup manual Objects
        // setupManualObjects();
        // generate Scene Objects procedurally
        SceneObject[] generatedObjects = ObjectGenerator.generateSceneObjects(numObjects, 0.2f, 0.01f);
        // Configure Scene Objects
        configureObjects(generatedObjects);
    }

   /* void setupManualObjects(){
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
    }*/

   
}
