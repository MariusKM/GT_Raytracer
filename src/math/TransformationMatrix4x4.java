package math;

/**
 * a transformation matrix, which also contains the inverse matrix 
 */
public class TransformationMatrix4x4 {
	private Matrix4x4 m; // matrix
	private Matrix4x4 im;// inverse matrix
	
	/**
	 * constructor (fills with unit matrix)
	 */
	public TransformationMatrix4x4() {
		m = new Matrix4x4();
		im = new Matrix4x4();
	}
	
	/**
	 * manually sets the matrix and its inverse
	 * @param m [in] 4x4 matrix
	 * @param im [in] 4x4 inverse matrix
	 */
	public TransformationMatrix4x4(Matrix4x4 m, Matrix4x4 im) {
		this.m = m;
		this.im = im;
	}
	
	public TransformationMatrix4x4(TransformationMatrix4x4 tm) {
		m = tm.m;
		im = tm.im;
	}
	
	/**
	 * @return the transformation matrix
	 */
	public Matrix4x4 getMatrix() { return m; }

	/**
	 * @return the inverse transformation matrix
	 */
	public Matrix4x4 getInverseMatrix() { return im; }
	
	/**
	 * creates the unit matrix
	 */
	public void createUnitMatrix() {	
		m.createUnitMatrix();
		im.createUnitMatrix();
	}
	
	// creates the transformation matrix as in math.Matrix4x4 and its inverse
	
	/**
	 * rotation around x-axis
	 * @param alpha [in] angle in radians
	 */
	public void createXRotationMatrix(double alpha) {
		m.createXRotationMatrix(alpha);
		im.createXRotationMatrix(-alpha);
	}

	/**
	 * rotation around y-axis
	 * @param beta [in] angle in radians
	 */
	public void createYRotationMatrix(double beta) {	
		m.createYRotationMatrix(beta);
		im.createYRotationMatrix(-beta);
	}

	/**
	 * rotation around z-axis
	 * @param gamma [in] angle in radians
	 */
	public void createZRotationMatrix(double gamma) {	
		m.createZRotationMatrix(gamma);
		im.createZRotationMatrix(-gamma);
	}

	
	/**
	 * creates a rotation matrix
	 * order: 1. z-rotation, 2. y-rotation, 3. x-rotation
	 * @param alpha [in] x-angle in radians
	 * @param beta [in] y-angle in radians
	 * @param gamma [in] z-angle in radians
	 */
	public void createRotationMatrix(double alpha, double beta, double gamma) {	
		m.createRotationMatrix(alpha, beta, gamma);
		Matrix4x4 im1 = new Matrix4x4();
		Matrix4x4 im2 = new Matrix4x4();
		Matrix4x4 im3 = new Matrix4x4();
		im1.createXRotationMatrix(-alpha);
		im2.createYRotationMatrix(-beta);
		im3.createZRotationMatrix(-gamma);
		im = MatrixOps.multiply(MatrixOps.multiply(im1, im2),im3);
	}

	/**
	 * creates a translation matrix from a vector
	 * @param v [in] translation vector
	 */
	public void createTranslationMatrix(Vector3D v) {	
		m.createTranslationMatrix(v);
		im.createTranslationMatrix(VectorOps.scale(-1, v));
	}

	/**
	 * creates a scale matrix in x-direction 
	 * @param sx [in] scaling
	 */

	public void createXScaleMatrix(double sx) {	
		m.createXScaleMatrix(sx);
		im.createXScaleMatrix(1.0/sx);
	}

	/**
	 * creates a scale matrix in y-direction 
	 * @param sy [in] scaling
	 */
	public void createYScaleMatrix(double sy) {	
		m.createYScaleMatrix(sy);
		im.createYScaleMatrix(1.0/sy);
	}

	/**
	 * creates a scale matrix in z-direction 
	 * @param sz [in] scaling
	 */
	public void createZScaleMatrix(double sz) {	
		m.createZScaleMatrix(sz);
		im.createZScaleMatrix(1.0/sz);
	}

	/**
	 * creates a scale matrix in all directions
	 * @param sx [in] x-scaling
	 * @param sy [in] y-scaling
	 * @param sz [in] z-scaling
	 */
	public void createScaleMatrix(double sx, double sy, double sz) {	
		m.createScaleMatrix(sx, sy, sz);
		im.createScaleMatrix(1.0/sx, 1.0/sy, 1.0/sz);
	}

	/**
	 * creates a scale matrix in all directions
	 * @param mat 4x4 transformation matrix
	 */
	public void transform(TransformationMatrix4x4 mat) {
		m = MatrixOps.multiply(m,mat.m);
		im = MatrixOps.multiply(mat.im,im);
	}
}
