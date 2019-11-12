package math;

/**
 * an orthonormal basis
 */
public class OrthonormalBasis {
	
	public Vector3D u,v,w;
	private Vector3D canonicalU = new Vector3D(1.0, 0.0, 0.0);
	private Vector3D canonicalV = new Vector3D(0.0, 1.0, 0.0);
	private Vector3D canonicalW = new Vector3D(0.0, 0.0, 1.0);

	/**
	 * constructor
	 */
	public OrthonormalBasis() {
		u = new Vector3D(1.0, 0.0, 0.0);
		v = new Vector3D(0.0, 1.0, 0.0);
		w = new Vector3D(0.0, 0.0, 1.0);
	}

	/**
	 * constructor with three given vectors
	 * @param u [in] u vector
	 * @param v [in] v vector
	 * @param w [in] w vector
	 */
	public OrthonormalBasis(Vector3D u, Vector3D v, Vector3D w) {
		this.u = u;
		this.v = v;
		this.w = w;
	}

	/**
	 * copy constructor
	 * @param b [in] the onb to copy
	 */
	public OrthonormalBasis( OrthonormalBasis b ) {
		u = new Vector3D(b.u);
		v = new Vector3D(b.v);
		w = new Vector3D(b.w);
		}

	/**
	 * create the onb from form the u vector
	 * @param u [in] u vector
	 */
	public void createFromU( Vector3D u ) {
		this.u = VectorOps.normalize(u);
		v = VectorOps.perpendicular(this.u);
		v.normalize();
		w = VectorOps.cross( this.u, v );
	}
	
	/**
	 * create the onb from form the w vector
	 * @param w [in] w vector
	 */
	public void createFromW( Vector3D w )
	{
		this.w = VectorOps.normalize(w);

		if( Math.abs(VectorOps.dot(this.w,canonicalV) - 1.0)  < Constants.nearzero )
		{
			u = canonicalW;
			v = canonicalU;
		}
		else if ( Math.abs(VectorOps.dot(this.w,canonicalV) + 1.0)  < Constants.nearzero )
		{
			u = canonicalU;
			v = canonicalW;
		}
		else
		{
			u = VectorOps.cross( this.w, canonicalV );
			u.normalize();
			v = VectorOps.cross( this.w, u );
			v.normalize();
		}
	}

	/**
	 * creates the basis transformation matrix to change from the local basis
	 * to the canonical basis
	 * @return transformation matrix
	 */
	public Matrix4x4 getBasisToCanonicalMatrix( ) 
	{
		Matrix4x4	m = new Matrix4x4();

		m.m00 = u.x;
		m.m10 = u.y;
		m.m20 = u.z;

		m.m01 = v.x;
		m.m11 = v.y;
		m.m21 = v.z;

		m.m02 = w.x;
		m.m12 = w.y;
		m.m22 = w.z;

		return m;
	}

	/**
	 * creates the basis transformation matrix to change from the canonical basis
	 * to the local basis
	 * @return transformation matrix
	 */
	public Matrix4x4 getCanonicalToBasisMatrix( ) 
	{
		Matrix4x4	m = new Matrix4x4();

		m.m00 = u.x;
		m.m01 = u.y;
		m.m02 = u.z;

		m.m10 = v.x;
		m.m11 = v.y;
		m.m12 = v.z;

		m.m20 = w.x;
		m.m21 = w.y;
		m.m22 = w.z;

		return m;
	}

	/**
	 * transforms a vector
	 * @param v [in] vector to transform
	 * @return transformed vector
	 */
	public Vector3D transform( Vector3D v )
	{
		return new Vector3D(
				u.x*v.x + this.v.x*v.y + w.x*v.z, 
				u.y*v.x + this.v.y*v.y + w.y*v.z, 
				u.z*v.x + this.v.z*v.y + w.z*v.z);
	}
	
	/**
	 * flips the orthonormal basis and keeps the helicity
	 */
	public void flipW() {
		w.scale(-1.0);
		u.scale(-1.0);
	}

}

