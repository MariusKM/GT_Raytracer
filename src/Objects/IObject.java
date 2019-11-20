package Objects;


import math.*;

// interface for an object
public interface IObject {
	
	/**
	 * @param p
	 * @return is the point inside the object
	 */
	public boolean isInside(Point3D p);

	/**
	 * calculate the value of the quadric at point p
	 * @param p point
	 * @return the value at point p, q(p)<=0 -> inside
	 */
	public double q(Point3D p);
	
	/**
	 * calculate normal via gradient
	 * @param p the point
	 * @return normal at p
	 */
	public Vector3D normal(Point3D p);
	

	
	/**
	 * transforms the object with the matrix m
	 * @param m the transformation matrix
	 */
	public void transform(TransformationMatrix4x4 m);


	
	/**
	 * translate the object with vector v
	 * @param v translation vector
	 */
	public void move(Vector3D v);


	/**
	 * rotate around vector v with angle alpha
	 * @param v vector
	 * @param alpha angle
	 */
	public void rotate(Vector3D v, double alpha);
	
	/**
	 * rotate around the x axis with angle alpha
	 * @param alpha angle
	 */
	public void rotateAroundX(double alpha);
	
	/**
	 * rotate around the y axis with angle beta
	 * @param beta angle
	 */
	public void rotateAroundY(double beta);
	
	/**
	 * rotate around the z axis with angle gamma
	 * @param gamma angle
	 */
	public void rotateAroundZ(double gamma);
	
	/**
	 * scale with factor s
	 * @param s scale factor
	 */
	public void scale(double s);
	
	/**
	 * scale each axis
	 * @param sx x scale
	 * @param sy y scale
	 * @param sz z scale
	 */
	public void scale(double sx, double sy, double sz);

	/**
	 * creates a random point on the surface of the object
	 * @param point [out] point on the surface
	 * @param normal [out] normal at the point on the surface
	 * @param prand [in] prand variables used in point generation
	 */
	public void UniformRandomPoint( Point3D point, Vector3D normal, Point3D prand);

	
	/**
	 * @return the area of the object
	 */
	public double getArea();
	
	/**
	 * @return the area of the object
	 */
	public IObject getCopy();
	
	

}
