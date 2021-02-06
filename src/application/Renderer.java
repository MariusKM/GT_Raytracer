package application;

import components.Camera;
import components.Scene;

public  interface Renderer {

     void render(int[] pixels, Camera cam, Scene Scene) ;

}



