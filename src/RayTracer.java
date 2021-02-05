import Objects.Camera;
import Objects.Ray;
import Objects.SceneObject;
import Objects.SceneSimple;
import Util.MathUtil;
import math.Vector3;

import java.awt.*;

public class RayTracer implements Renderer {

    protected boolean isUsePerspective;
    protected int ResX, ResY;


    @Override
    public void render(int[] pixels, Camera cam, SceneSimple Scene) {
        float t = 0;

        int resX =ResX;
        int resY =ResY;
        int insideCounter = 0, outsideCounter = 0;
        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vector3 rayDir;
                if (isUsePerspective) {
                    Vector3 pixelPos = cam.pixelCenterCoordinate(x, y);

                    rayDir = pixelPos.sub(cam.getPosition());


                } else {
                    float pixelPosX = (2 * (x + 0.5f) / (float) resX - 1) * cam.getAspectRatio() * cam.getScale();
                    float pixelPosY = (1 - 2 * (y + 0.5f) / (float) resY) * cam.getScale();
                    rayDir = new Vector3(pixelPosX, pixelPosY, 0).sub(cam.getPosition());
                }
                rayDir.normalize();
                Ray myRay = new Ray(cam.getPosition(), rayDir);
                boolean intersect = false;
                SceneObject temp;
                SceneObject intersectObj;

                for (SceneObject s : Scene.getSceneObjects()) {
                    intersect = s.intersect(myRay);
                }
                int indexer = isUsePerspective ? (resY - y - 1) * resY + x : (y * resY + x);
                if (myRay.getNearest() != null) {
                    temp = myRay.getNearest();
                    intersectObj = temp;

                    Vector3 finalCol = intersectObj.shadeCookTorrance(myRay, Scene, false, 5);

                    Color finalColorRGB = new Color(MathUtil.clampF(finalCol.x, 0, 1), MathUtil.clampF(finalCol.y, 0, 1), MathUtil.clampF(finalCol.z, 0, 1));

                    int pixelColor = (intersectObj.isShade()) ? finalColorRGB.getRGB() : Color.WHITE.getRGB();
                    pixels[indexer] = pixelColor;

                } else {
                    pixels[indexer] = Scene.getBgCol().getRGB();
                }

                // System.out.println("painted pixel no " + indexer);
            }

        }
    }


    public RayTracer (ApplicationSettings applicationSettings) {
        this.ResX = applicationSettings.getResX();
        this.ResY = applicationSettings.getResY();
        this.isUsePerspective = applicationSettings.isUsePerspective();
    }
}

