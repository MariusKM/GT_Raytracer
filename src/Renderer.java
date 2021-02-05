import Objects.Camera;
import Objects.Ray;
import Objects.SceneObject;
import Objects.SceneSimple;
import Util.MathUtil;
import math.Vector3;

import java.awt.*;

public  interface Renderer {

     void render(int[] pixels, Camera cam, SceneSimple Scene) ;

}



