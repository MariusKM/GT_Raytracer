package Objects;


import math.*;

public abstract class Object implements IObject {


	public TransformationMatrix4x4 trans;

	
	/**
	 * constructor
	 */
	public Object() {
		trans = new TransformationMatrix4x4();

	}

	/* (non-Javadoc)
	 * @see objects.IObject#assignMaterial(material.IMaterial)
	 */


	/* (non-Javadoc)
	 * @see objects.IObject#getMaterial()


	/* (non-Javadoc)
	 * @see objects.IObject#move(math.Vector3D)
	 */
	public void move(Vector3D v) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createTranslationMatrix(v);
		transform(m);
		trans.transform(m);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#rotate(math.Vector3D, double)
	 */
	public void rotate(Vector3D v, double alpha) {
		v.normalize();
		if (alpha==Math.PI) {
			scale(-1);
			return;
		}
		double	t = alpha * 0.5;
		double	S =  Math.sin( t ) ;
		double	C =  Math.cos( t ) ;

		v.scale(S);
		Matrix4x4 m = MatrixOps.mkFromQuaternion(new Quaternion( C, v ));
		v.scale(-1.0);
		Matrix4x4 im = MatrixOps.mkFromQuaternion(new Quaternion( C, v ));
		TransformationMatrix4x4 tm = new TransformationMatrix4x4(m,im);
		transform(tm);
		trans.transform(tm);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#rotateAroundX(double)
	 */
	public void rotateAroundX(double alpha) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createXRotationMatrix(alpha);
		transform(m);
		trans.transform(m);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#rotateAroundY(double)
	 */
	public void rotateAroundY(double beta) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createYRotationMatrix(beta);
		transform(m);
		trans.transform(m);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#rotateAroundZ(double)
	 */
	public void rotateAroundZ(double gamma) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createZRotationMatrix(gamma);
		transform(m);
		trans.transform(m);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#scale(double)
	 */
	public void scale(double s) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createScaleMatrix(s, s, s);
		transform(m);
		trans.transform(m);
	}

	/* (non-Javadoc)
	 * @see objects.IObject#scale(double, double, double)
	 */
	public void scale(double sx, double sy, double sz) {
		TransformationMatrix4x4 m = new TransformationMatrix4x4();
		m.createScaleMatrix(sx, sy, sz);
		transform(m);
		trans.transform(m);
	}


	/* (non-Javadoc)
	 * @see objects.IObject#getArea()
	 */
	public double getArea() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public TransformationMatrix4x4 getTransformationMatrix() {
		return trans;
	}

	public void setTransformationMatrix(TransformationMatrix4x4 m) {
		trans = m;
	}


	
	

}
