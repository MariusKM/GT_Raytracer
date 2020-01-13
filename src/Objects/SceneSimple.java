package Objects;

import Objects.Camera;
import Objects.SceneObject;

import java.awt.*;
import java.util.ArrayList;

public class SceneSimple{

    private ArrayList<SceneObject> sceneObjects;
    private ArrayList<Light> sceneLights = new ArrayList<>();
    private Camera sceneCam;
    private Color bgCol;
    public SceneSimple(){
        sceneObjects = new ArrayList<SceneObject>();
    }

    public ArrayList<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    public void setSceneObjects(ArrayList<SceneObject> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }

    public ArrayList<Light> getSceneLight() {
        return sceneLights;
    }

    public void setSceneLight(ArrayList<Light> sceneLight) {
        this.sceneLights = sceneLight;
    }

    public Camera getSceneCam() {
        return sceneCam;
    }

    public void setSceneCam(Camera sceneCam) {
        this.sceneCam = sceneCam;
    }

    public Color getBgCol() {
        return bgCol;
    }

    public void setBgCol(Color bgCol) {
        this.bgCol = bgCol;
    }


}
