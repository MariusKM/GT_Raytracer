//
// RayTracer: A simple ray tracer for spheres and polygons.
//
// (This was a port from one of my graphics classes.)
//
// Author: Will Luo
// Last Modified: Sat Dec 16 15:01:35 EST 1995
//

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Vector;   // use java vector as a list
import java.lang.Thread;

class SceneConsts {
    static double GIa;   /* ambient light intensity (assume it is white) */
    static double GKa;   /* ambient light reflectance */
    static double GKd;   /* global object reflectance */
    static double Gs;    /* global "shadow reflectance"  This prevents a
			    flat looking shadow */
    static double Ip;

    public SceneConsts() {
        GIa = 0.1;
        GKa = 1.0;
        GKd = 1.0;
        Gs = 0.0005;
        Ip = 1.0;
    }
}

/*
 * These constants describe the environment of a scene.
 */
class Scene {
    static SceneConsts sConsts;
    Vec VRP;
    Vec light;
    Vector targetList; // objects in the scene
}


public class RayTracer extends java.applet.Applet implements Runnable {

    Thread kicker;

    DrawPanel dp;
    static Scene scene;

    public RayTracer() {
        scene = new Scene();
        scene.targetList = new Vector();
    }

    public void init() {
        // set up the lights and view reference point
        scene.VRP = new Vec(0.0, 0.0, -1.0);
        scene.light = new Vec(16.0, 8.0, -16.0);

        // Build targetList by hand for now. We should eventually let the
        // users build their objects interactively.
        scene.targetList.addElement(new SphereTarget(0.05, -0.3, 0.0, 0.3));
        scene.targetList.addElement(new SphereTarget(0.05, 0.06, 0.0, 0.24));
        scene.targetList.addElement(new SphereTarget(0.05, 0.19, -0.205, 0.01));
        scene.targetList.addElement(new SphereTarget(0.05, 0.09, -0.237, 0.01));
        scene.targetList.addElement(new SphereTarget(0.05, 0.0, -0.230, 0.01));
        scene.targetList.addElement(new SphereTarget(0.05, 0.32, -0.205, 0.018));
        scene.targetList.addElement(new SphereTarget(0.05, 0.34, 0.0, 0.2));
        scene.targetList.addElement(new SphereTarget(0.1, 0.4, -0.195, 0.02));
        scene.targetList.addElement(new SphereTarget(-0.02, 0.393, -0.185, 0.02));

        scene.targetList.addElement(new SphereTarget(0.0, 0.0, 10, 10));

        for(int i=0; i<scene.targetList.size(); i++)
            if (!((Target)scene.targetList.elementAt(i)).initScene())
                ((Target)scene.targetList.elementAt(i)).initScene(scene);

        setLayout(new BorderLayout());
        add("Center", dp = new DrawPanel(scene.targetList));
    }

    public synchronized void start() {
        kicker = new Thread(this);
        kicker.start();
    }

    public synchronized void stop() {
        try {
            if (kicker != null) {
                kicker.stop();
            }
        } catch (Exception e) {
        }
        kicker = null;
    }

    public void restart() {
        stop();
        dp.setImage(null);
        run();
    }

    public static void main(String args []) {
        GraphicsFrame f = new GraphicsFrame();
        RayTracer rt = new RayTracer();

        rt.init();

        f.add("Center", rt);
        f.resize(200, 200);
        f.show();

        rt.start();
    }

    int pixels[];
    public void run() {
        Thread me = Thread.currentThread();
        me.setPriority(3);

        int w = dp.size().width, h = dp.size().height;
        int pixY=0, pixX=0;
        double ystep = 1.0/h, xstep = 1.0/w;
        double tmpy = 1.0, tmpz = 0.0; // tmpz is the projection plane
        Graphics g = dp.getGraphics(); // show user the progress.

        pixels = new int[w * h];
        System.out.println(w + " x " + h);

        // for (pixY=0; pixY < 2*h; pixY++){
        // for (pixY=2*h; pixY >= 0; pixY--){
        for (pixY=0; pixY < 2*h; pixY++){
            double tmpx = -1.0;
            tmpy -= ystep;
            // for(pixX=0; pixX < 2*w; pixX++){
            for(pixX=0; pixX < 2*w; pixX++){
                tmpx += xstep;
                Vec projP = new Vec(tmpx, tmpy, tmpz);
                int c = paintPix(projP);

                int realX = pixX/2, realY = pixY/2;

                pixels[realX + w*realY] = (255<<24)|(c<<16)|(c<<8)|(c<<0);
                // visual feedback on progress
                g.setColor(new Color(c,c,c));
                g.drawLine(realX, realY, realX, realY);
            }
        }

        System.out.println(pixX + " x " + pixY);

        newImage(me, w, h, pixels);
    }

    synchronized void newImage(Thread me, int width, int height,
                               int pixels[]) {
        if (kicker != me) {
            return;
        }
        Image img;
        img = createImage(new MemoryImageSource(width, height,
                ColorModel.getRGBdefault(),
                pixels, 0, width));
        dp.setImage(img);
        kicker = null;
    }

    int paintPix(Vec projP) {
        double t[] = new double[1];
        int color=0;
        Vec R1 = new Vec(projP);
        R1.sub(scene.VRP);
        R1.normalize();

        t[0] = 0.0;

        int obj = intersectObjects(scene.VRP, R1, t, 0, false);

        /* if it does then find out how to paint the pixel */
        if(t[0] > 0.0){
            color = ((Target) scene.targetList.elementAt(obj)).shade(obj,
                    R1, t);
        }

        color += 16;  // ambient light

        if(color > 255)
            color = 255;

        return color;
    }

    int intersectObjects(Vec R0, Vec R1, double result[], int object,
                         boolean shadowCheck) {
        double minDist=0.0, dist;
        int hit=-1;

        for(int i=0; i<scene.targetList.size(); i++) {
            if((shadowCheck == true) && (object == i))
                continue;
            dist = ((Target)scene.targetList.elementAt(i)).intersectTest(R0,
                    R1, i);

            if(dist == 0.0)
                continue;

            /* save the first t */
            if((minDist==0.0) && (dist>0.0)){
                minDist = dist;
                hit = i;
            } else if((dist>0.0) && (dist<minDist)){
                minDist = dist;
                hit = i;
            }
        }
        result[0] = minDist;
        return hit;
    }
}


class GraphicsFrame extends Frame {

    public GraphicsFrame() {
        super("Ray Tracer");
    }

    public boolean handleEvent(Event e) {
        if (e.id == Event.WINDOW_DESTROY) {
            System.out.println("Outta here!");
            System.exit(0);
        }
        return false;
    }
}


class DrawPanel extends Panel {

    Vector targetList;
    Image img;
    static String calcString = "Tracing...";

    public DrawPanel(Vector tl) {
        this.targetList = tl;
        setBackground(Color.red);
    }

    public void paint(Graphics g) {
        System.out.println("in paint");
        int w = size().width;
        int h = size().height;

        if (img == null) {
            super.paint(g);
            g.setColor(Color.green);
            FontMetrics fm = g.getFontMetrics();
            int x = (w - fm.stringWidth(calcString))/2;
            int y = h-1;
            g.drawString(calcString, x, y);

        } else {
            g.drawImage(img, 0, 0, w, h, this);
        }
    }

    public Image getImage() {
        return img;
    }

    public void setImage(Image img) {
        this.img = img;
        repaint();
    }
}

//
// Generic target object to be traced
//
abstract class Target {
    public abstract double intersectTest(Vec R0, Vec R1, int object);
    public abstract int shade(int object, Vec R1, double t[]);
    public abstract void initScene(Scene s);
    public abstract boolean initScene();
}

class SphereTarget extends Target {
    Vec center;
    double radius, radiusSq; // precompute radiusSq since we use it a lot
    static Scene scene;
    static SceneConsts sConsts;

    public SphereTarget(double x, double y, double z, double r) {
        center = new Vec(x, y, z);
        radius = r;
        radiusSq = r*r;

        if (sConsts == null)
            sConsts = new SceneConsts();
    }

    public SphereTarget(Vec v, double r) {
        center = new Vec(v);
        radius = r;
        radiusSq = r*r;

        if (sConsts == null)
            sConsts = new SceneConsts();
    }

    public SphereTarget() {
        center = new Vec(0,0,0);
        radius = radiusSq = 1;

        if (sConsts == null)
            sConsts = new SceneConsts();
    }

    public void initScene(Scene s) {
        scene = s;
    }

    public boolean initScene() {
        return ((scene == null) ? false : true);
    }

    public double intersectTest(Vec R0, Vec R1, int object) {

        double t,          /* where the ray intersects */
                loc,        /* square distance from center of sphere to projP */
                tca,        /* how far is the 'closest approach' from VRP */
                thc;        /* length sqare of the half chord */
        Vec vecoc;      /* vector to the center of the sphere from VRP */
        boolean inside=false;

        /* use the closest approach algorithm */
        vecoc = new Vec(center);
        vecoc.sub(R0);
        loc = vecoc.dotProduct(vecoc);

        if(loc <= radiusSq)
            inside = true;

        tca = vecoc.dotProduct(R1);   /* find the closest approach */

        if ((inside != true) && (tca <= 0.0))
            return(0.0);  /* object is behind the VRP */

	/* compute the half chord square from the ray intersection to the 
	   intersection normal. */
        thc = (tca * tca) + radiusSq - loc;
        if (thc < 0.0)
            return(0.0);   /* ray misses the sphere */

        /* find the ray intersection */
        if (inside == true)
            t = tca + Math.sqrt(thc);
        else
            t = tca - Math.sqrt(thc);

        return(t);
    }

    public int shade(int object, Vec R1, double t[]){

        Vec intersection, normal, lightSource;
        double intensity;
        double tShadow[] = new double[1];
        tShadow[0] = 0;

        /* calculate the intersection POINT on the object */
        intersection = new Vec(R1);
        intersection.mult(t[0]);
        intersection.add(scene.VRP);

        /* find the normal vector from sphere's center to the intersection */
        normal = new Vec(intersection);
        normal.sub(center);
        //normal.mult(-1);
        normal.normalize();

        /* locate the light source from intersection */
        lightSource = new Vec(scene.light);
        lightSource.sub(intersection);

        lightSource.normalize();


        /* check if the light can be "seen" by the intersection point */
        intersectObjects(intersection, lightSource, tShadow, object, true);

        intensity = lightSource.dotProduct(normal);
        if(intensity < 0.0)
            intensity = 0.0;

        if(tShadow[0] > 0.0) /* something is in the way */
            intensity = sConsts.Gs * intensity;  /* pixel gets ambient
						      light only */
        else{   /* pixel gets all kinds of light */
            intensity = intensity * sConsts.Ip * sConsts.GKd;
        }

        intensity = intensity + sConsts.GIa*sConsts.GKa;
        if(intensity > 1.0)
            intensity = 1.0;

        /* find the corresponding color in the color lookup table */
        intensity = intensity * 255;

        return((int)intensity);
    }

    int intersectObjects(Vec R0, Vec R1, double result[], int object,
                         boolean shadowCheck) {
        double minDist=0.0, dist;
        int hit=-1;

        for(int i=0; i<scene.targetList.size(); i++) {
            if((shadowCheck == true) && (object == i))
                continue;
            dist = ((Target)scene.targetList.elementAt(i)).intersectTest(R0,
                    R1, i);

            if(dist == 0.0)
                continue;

            /* save the first t */
            if((minDist==0.0) && (dist>0.0)){
                minDist = dist;
                hit = i;
            } else if((dist>0.0) && (dist<minDist)){
                minDist = dist;
                hit = i;
            }
        }
        result[0] = minDist;
        return hit;
    }

    public void debug_test() {
        System.out.println("SphereTarget.debug_test(): center = " +
                center.toString() + ", r = " + radius + ", r^2 = "
                + radiusSq);
    }
}

class PolygonTarget extends Target {
    Vector vertexList;
    Vec planeNormal;
    static Scene scene;

    public PolygonTarget() {
    }

    public PolygonTarget(Vector vl, Vec pn) {
        planeNormal = new Vec(pn);
        vertexList = (Vector) vl.clone();
    }

    public double intersectTest(Vec R0, Vec R1, int object) {
        System.out.println("Polygon intersect Test");
        return 0.0;
    }

    public int shade(int object, Vec R1, double t[]) {
        return 0;
    }

    public void debug_test() {
        System.out.println("PolygonTarget.debug_test():");
    }

    public void initScene(Scene s) {}
    public boolean initScene() { return false; }
}

//
// note: None of the methods return a vector. They will modify the current
// vector object.
// 
class Vec {
    double x, y, z;

    public Vec() {
        x=0; y=0; z=0;
    }

    public Vec(double ix, double iy, double iz) {
        x=ix; y=iy; z=iz;
    }

    public Vec(Vec v) {
        x=v.x; y=v.y; z=v.z;
    }

    public void set(double nx, double ny, double nz) {
        x = nx; ny = ny; z = nz;
    }

    public void normalize() {
        double length;

        length = Math.sqrt(x*x + y*y + z*z);

        try {
            x = x / length;
            y = y / length;
            z = z / length;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public double dotProduct(Vec v) {
        return((x*v.x + y*v.y + z*v.z));
    }

    // this = this crosses v
    public void crossProduct(Vec v) {
        double tmpx = y*v.z - z*v.y,
                tmpy = z*v.x - x*v.z,
                tmpz = x*v.y - y*v.x;
        x = tmpx; y = tmpy; z = tmpz;
    }

    public void mult(double factor) {
        x=x*factor; y=y*factor; z=z*factor;
    }

    public void add(Vec v) {
        x=x+v.x; y=y+v.y; z=z+v.z;
    }

    // subtracts v from this vector
    public void sub(Vec v) {
        x = x - v.x;
        y = y - v.y;
        z = z - v.z;
    }

    public String toString() {
        String res = new String("["+ x + ", " + y + ", " + z + "]");
        return res;
    }
}