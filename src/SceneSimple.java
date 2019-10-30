import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SceneSimple{

    private ArrayList<SphereObject> sceneObjects;
    private Light sceneLight;
    private Camera sceneCam;

    public SceneSimple(){
        sceneObjects = new ArrayList<SphereObject>();
    }
    
    public ArrayList<SphereObject> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(ArrayList<SphereObject> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }

    public Light getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(Light sceneLight) {
        this.sceneLight = sceneLight;
    }

    public Camera getSceneCam() {
        return sceneCam;
    }

    public void setSceneCam(Camera sceneCam) {
        this.sceneCam = sceneCam;
    }




}
