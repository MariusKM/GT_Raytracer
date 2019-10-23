class SphereObject extends TargetObject {
    Vector3 center;
    double radius, radiusSq; // precompute radiusSq since we use it a lot
    static Scene scene;
    static SceneConsts sConsts;

    public SphereObject(double x, double y, double z, double r) {
        center = new Vector3(x, y, z);
        radius = r;
        radiusSq = r*r;


    }

    public SphereObject(Vector3 v, double r) {
        center = new Vector3(v.x,v.y,v.z);
        radius = r;
        radiusSq = r*r;


    }

    public SphereObject() {
        center = new Vector3(0,0,0);
        radius = radiusSq = 1;

    }






}