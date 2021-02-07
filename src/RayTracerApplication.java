import components.Scene;
import components.TestScene;
import application.AnimationManager;
import application.*;

public class RayTracerApplication extends java.applet.Applet {

    private static boolean exit;
    static int[] pixels;
    static Scene currentScene;
    static ApplicationSettings applicationSettings;
    static Renderer renderer;
    static GaussFilter filter;
    static ImageWriter imageWriter;

    public static void main(String args[]) {
        initialize();
        do {
            renderer.render(pixels,currentScene.getSceneCam(), currentScene);
            pixels = filter.applyFilter(pixels);
            GUI.drawGUI(exit,pixels);
            TimeHandler.update();
            AnimationManager.getInstance().animate();
            if (AnimationManager.getInstance().isFinished()) ImageWriter.saveImage(GUI.getImage(), applicationSettings);
            else exit = true;
        }
        while (!exit);
    }

    static void initialize(){
        applicationSettings = new DefaultApplicationSettings();
        renderer = new RayTracer(applicationSettings);
        filter = new GaussFilter(applicationSettings.getResX(),applicationSettings.getResY());
        AnimationManager.getInstance().setAnimationLength(applicationSettings.getAnimationLength());
        GUI.initialize(applicationSettings);
        imageWriter = new ImageWriter("D:/Uni/GT2A2 Raytracer/GT_Raytracer/render/Anim", "jpeg");
        pixels = new int[applicationSettings.getResX()* applicationSettings.getResY()];
        currentScene = new TestScene(applicationSettings);
        TimeHandler.setLast_time(System.nanoTime());
    }








}




