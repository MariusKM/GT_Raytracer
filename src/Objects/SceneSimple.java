package Objects;

import Objects.Camera;
import Objects.SceneObject;

import java.awt.*;
import java.util.ArrayList;

public class SceneSimple{

    private ArrayList<SceneObject> sceneObjects;
    private Light sceneLight;
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

    public Color getBgCol() {
        return bgCol;
    }

    public void setBgCol(Color bgCol) {
        this.bgCol = bgCol;
    }


}
