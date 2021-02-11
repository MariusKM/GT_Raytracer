import components.GeneratedScene;
import components.Scene;
import components.TestScene;
import application.AnimationManager;
import application.*;
import render.RayTracer;
import render.Renderer;

public class RayTracerApplication extends java.applet.Applet {

    private static boolean exit;
    static int[] pixels;
    static Scene currentScene;
    static ApplicationSettings applicationSettings;
    static Renderer renderer;
    static GaussFilter filter;
    static ImageWriter imageWriter;

    public static void main(String[] args) {
        initialize();
        do {
            renderer.render(pixels, currentScene);
            pixels = filter.applyFilter(pixels);
            GUI.drawGUI(exit, pixels);
            TimeHandler.update();
            AnimationManager.getInstance().animate();
            if (AnimationManager.getInstance().isFinished())
                ImageWriter.saveImage(applicationSettings, TimeHandler.getFrameCounter());
            else exit = true;
        }
        while (!exit);

    }

    static void initialize() {
        applicationSettings = new DefaultApplicationSettings();
        renderer = new RayTracer(applicationSettings);
        filter = new GaussFilter(applicationSettings.getResX(), applicationSettings.getResY());
        AnimationManager.getInstance().setAnimationLength(applicationSettings.getAnimationLength());
        GUI.initialize(applicationSettings);
        imageWriter = new ImageWriter("D:/Uni/GT2A2 Raytracer/GT_Raytracer/render/Anim", "jpeg");
        pixels = new int[applicationSettings.getResX() * applicationSettings.getResY()];
        currentScene = new GeneratedScene(applicationSettings,25);
        TimeHandler.setLast_time(System.nanoTime());
    }


}




