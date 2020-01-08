package Util;

import java.awt.*;

import static java.lang.Math.sqrt;

public class MathUtil {

    public static float[] solveQuadratic(double a, double b, double c) {
        float x0, x1;
        float[] results = new float[3];
        float discr = (float) (b * b - 4 * a * c); // Diskriminanter Term in der PQ formel, Term unter der Wurzel)
        if (discr < 0) {
            // wenn dieser kleiner Null ist, dann gibt es keine Schnittpunkte
            results[0] = -1;
            return results;
        } else if (discr == 0) {
            // wenn dieser gleich Null ist, dann gibt es einen Schnittpunkt (Tangente)
            x0 = x1 = (float) (-0.5 * b / a);
        } else {
            // Ergebnis für 2 Schnittpunkte
            float q = (float) ((b > 0) ? -0.5 * (b + sqrt(discr)) : -0.5 * (b - sqrt(discr)));
            x0 = (float) (q / a);
            x1 = (float) (c / q);

        }

        if (x0 > x1) {
            // siehe zu, dass x0 kleiner als x1
            float tempx0 = x0;
            float tempx1 = x1;
            x0 = tempx1;
            x1 = tempx0;

        }
        ;
        results[0] = 1; // identfikator
        results[1] = x0;
        results[2] = x1;


        return results;
    }


    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float clampF(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    static Color blend(Color a, Color b) {

        double r = (a.getRed() * b.getRed()) / 255;
        double gr = (a.getGreen() * b.getGreen()) / 255;
        double bl = (a.getBlue() * b.getBlue()) / 255;
        double al = (a.getAlpha() * b.getAlpha()) / 255;

        Color blendedCol = new Color((int) r, (int) gr, (int) bl, (int) al);
        return blendedCol;


    }

}
