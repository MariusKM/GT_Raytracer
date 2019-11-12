package math;
import  Objects.*;

/**
 * intersection info<br>
 * contains all information needed for shading the surface,
 * where the ray3 intersects the object
 */
public class IntersectionInfo {
	
	// info at intersection 1
	public double t1;				// distance from ray3.start to the 1. intersections
	public IObject t1object;	// object at 1. intersection

	
	// info at intersection 2
	public double t2;				// distance from ray3.start to the 2. intersections
	public IObject t2object;	// object at 2. intersection
	
	public Ray ray;					// the ray3 that intersects the object
	public boolean hit;				// does the ray3 hits the object
	
	// info at the next intersection (t>0)
	public Point3D nextPosition;
	public double nextDistance;
	public IObject nextObject;
	//public objects.IObject nextParentObject;
	public Vector3D nextNormal;
	public OrthonormalBasis nextONB;
	public Point2D nextTextureCoords;
	
	/**
	 * constructor
	 */
	public IntersectionInfo() {
		hit = false;
	}
	
	/**
	 * copy constructor
	 * @param intersection [in] intersection information
	 */
	public IntersectionInfo(IntersectionInfo intersection) {
		if (intersection==null) return;
		hit = intersection.hit;
		ray= new Ray(intersection.ray);

		t1 = intersection.t1;
		t1object = intersection.t1object.getCopy();
		
		t2 = intersection.t2;
		t2object = intersection.t2object.getCopy();
	
		nextPosition = new Point3D(intersection.nextPosition);
		nextDistance = intersection.nextDistance;
		nextObject = intersection.nextObject.getCopy();
		//nextParentObject = intersection.nextParentObject;
		nextNormal = new Vector3D(intersection.nextNormal);
		nextONB = new OrthonormalBasis(intersection.nextONB);
		nextTextureCoords = new Point2D(intersection.nextTextureCoords);
	}
	
	/**
	 * constructor
	 * @param t1_ [in] distance to first intersection
	 * @param t2_ [in] distance to second intersection
	 * @param ray [in] the ray3 which intersects with an object
	 * @param object_at_t1 [in] first object to intersect
	 * @param object_at_t2 [in] second object to intersect
	 */
	public IntersectionInfo( double t1_, double t2_, Ray ray,
			IObject object_at_t1, IObject object_at_t2 ) {
		hit = true;
		if (t2_<t1_) {
			t1 = t2_;
			t1object = object_at_t2;
			t2 = t1_;
			t2object = object_at_t1;
		} else {
			t1 = t1_;
			t1object = object_at_t1;
			t2 = t2_;
			t2object = object_at_t2;
		}
		if (t2<0.0) {
			hit = false;
			return;
		}
		t1 = Math.max(0.0, t1);
		ray = ray;
		
		if (t1>0.0) {
			nextPosition = ray.getPoint(t1);
			nextDistance = t1;
			nextObject = t1object;
		} else {
			nextPosition = ray.getPoint(t2);
			nextDistance = t2;
			nextObject = t2object;
		}
	}
	
	/**
	 * sets the normal of the intersection 1
	 */
	public void setNextIntersectionNormal() {
		nextNormal = nextObject.normal(nextPosition);
	}
	
}
