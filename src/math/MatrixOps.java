package math;

/**
 * a class for matrix operations
 */
public class MatrixOps {

    /**
     * multiplication of two matrices
     * @param m1 [in] 4x4-matrix #1
     * @param m2 [in] 4x4-matrix #2
     * @return m1*m2
     */
    public static Matrix4x4 multiply(Matrix4x4 m1, Matrix4x4 m2) {
        Matrix4x4 erg = new Matrix4x4();
        erg.m00=m1.m00*m2.m00+m1.m01*m2.m10+m1.m02*m2.m20+m1.m03*m2.m30;
        erg.m01=m1.m00*m2.m01+m1.m01*m2.m11+m1.m02*m2.m21+m1.m03*m2.m31;
        erg.m02=m1.m00*m2.m02+m1.m01*m2.m12+m1.m02*m2.m22+m1.m03*m2.m32;
        erg.m03=m1.m00*m2.m03+m1.m01*m2.m13+m1.m02*m2.m23+m1.m03*m2.m33;
        erg.m10=m1.m10*m2.m00+m1.m11*m2.m10+m1.m12*m2.m20+m1.m13*m2.m30;
        erg.m11=m1.m10*m2.m01+m1.m11*m2.m11+m1.m12*m2.m21+m1.m13*m2.m31;
        erg.m12=m1.m10*m2.m02+m1.m11*m2.m12+m1.m12*m2.m22+m1.m13*m2.m32;
        erg.m13=m1.m10*m2.m03+m1.m11*m2.m13+m1.m12*m2.m23+m1.m13*m2.m33;
        erg.m20=m1.m20*m2.m00+m1.m21*m2.m10+m1.m22*m2.m20+m1.m23*m2.m30;
        erg.m21=m1.m20*m2.m01+m1.m21*m2.m11+m1.m22*m2.m21+m1.m23*m2.m31;
        erg.m22=m1.m20*m2.m02+m1.m21*m2.m12+m1.m22*m2.m22+m1.m23*m2.m32;
        erg.m23=m1.m20*m2.m03+m1.m21*m2.m13+m1.m22*m2.m23+m1.m23*m2.m33;
        erg.m30=m1.m30*m2.m00+m1.m31*m2.m10+m1.m32*m2.m20+m1.m33*m2.m30;
        erg.m31=m1.m30*m2.m01+m1.m31*m2.m11+m1.m32*m2.m21+m1.m33*m2.m31;
        erg.m32=m1.m30*m2.m02+m1.m31*m2.m12+m1.m32*m2.m22+m1.m33*m2.m32;
        erg.m33=m1.m30*m2.m03+m1.m31*m2.m13+m1.m32*m2.m23+m1.m33*m2.m33;
        return erg;
    }



    /**
     * multiplies a matrix with a 3D point
     * @param m [in] 4x4-matrix
     * @param p [in] 3d-point
     * @return m*p
     */
    public static  Point3D transform(Matrix4x4 m, Point3D p) {
        return new Point3D(
                m.m00*p.x+m.m01*p.y+m.m02*p.z+m.m03,
                m.m10*p.x+m.m11*p.y+m.m12*p.z+m.m13,
                m.m20*p.x+m.m21*p.y+m.m22*p.z+m.m23 );
    }

    /**
     * multiplies a matrix with a 3-vector
     * @param m [in] 4x4-matrix
     * @param v [in] 3-vector
     * @return m*v
     */
    public static  Vector3D transform(Matrix4x4 m, Vector3D v) {
        return new Vector3D(
                m.m00*v.x+m.m01*v.y+m.m02*v.z,
                m.m10*v.x+m.m11*v.y+m.m12*v.z,
                m.m20*v.x+m.m21*v.y+m.m22*v.z );
    }

    /**
     *  multiplies a matrix from left with a vector
     * @param v [in] 4-vector
     * @param m [in] 4x4-matrix
     * @return v*m
     */
    public static  Vector multiply(Vector v, Matrix4x4 m) {
        return new Vector(
                v.x*m.m00+v.y*m.m10+v.z*m.m20+v.w*m.m30,
                v.x*m.m01+v.y*m.m11+v.z*m.m21+v.w*m.m31,
                v.x*m.m02+v.y*m.m12+v.z*m.m22+v.w*m.m32,
                v.x*m.m03+v.y*m.m13+v.z*m.m23+v.w*m.m33);
    }

    /**
     * returns a transposed matrix
     * @param m [in] 4x4 matrix
     * @return transposed m
     */
    public static Matrix4x4 transpose(Matrix4x4 m) {
        return new Matrix4x4(	m.m00, m.m10, m.m20, m.m30,
                m.m01, m.m11, m.m21, m.m31,
                m.m02, m.m12, m.m22, m.m32,
                m.m03, m.m13, m.m23, m.m33);
    }

    /**
     * multiplies two transformation matrices
     * @param m1 [in] transformation-matrix #1
     * @param m2 [in] transformation-matrix #2
     * @return m1*m2
     */
    public static TransformationMatrix4x4 multiply(TransformationMatrix4x4 m1, TransformationMatrix4x4 m2) {
        return new TransformationMatrix4x4(
                multiply(m1.getMatrix(),m2.getMatrix()),
                multiply(m2.getInverseMatrix(),m1.getInverseMatrix()));
    }

    /**
     * creates a 4x4-matrix from a quaternion number
     * @param a [in] quaternion
     * @return matrix(a)
     */
    public static Matrix4x4 mkFromQuaternion( Quaternion a )
    {
        Matrix4x4	m = new Matrix4x4();

        double _2x = 2.0 * a.v.x;
        double _2y = 2.0 * a.v.y;
        double _2z = 2.0 * a.v.z;
        double _2xx = _2x * a.v.x;
        double _2xy = _2x * a.v.y;
        double _2xz = _2x * a.v.z;
        double _2yy = _2y * a.v.y;
        double _2yz = _2y * a.v.z;
        double _2zz = _2z * a.v.z;
        double _2xs = _2x * a.s;
        double _2ys = _2y * a.s;
        double _2zs = _2z * a.s;

        m.m00 = 1.0 - _2yy - _2zz;
        m.m01 =       _2xy - _2zs;
        m.m02 =       _2xz + _2ys;
        m.m03 = 0.0;
        m.m10 =       _2xy + _2zs;
        m.m11 = 1.0 - _2xx - _2zz;
        m.m12 =       _2yz - _2xs;
        m.m13 = 0.0;
        m.m20 =       _2xz - _2ys;
        m.m21 =       _2yz + _2xs;
        m.m22 = 1.0 - _2xx - _2yy;
        m.m23 = 0.0;
        m.m30 = 0.0;
        m.m31 = 0.0;
        m.m32 = 0.0;
        m.m33 = 1.0;

        return m;
    }

}
