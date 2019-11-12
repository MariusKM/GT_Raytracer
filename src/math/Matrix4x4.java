package math;

/**
 * a 4x4 matrix
 */
public class Matrix4x4 {
    // m<row><column>
    public double 	m00=1,m01=0,m02=0,m03=0,
            m10=0,m11=1,m12=0,m13=0,
            m20=0,m21=0,m22=1,m23=0,
            m30=0,m31=0,m32=0,m33=1;
    /**
     * constructor, standard is the unit matrix
     */
    public Matrix4x4() {}

    /**
     * constructor<br>
     * m00 m01 m02 m03<br>
     * m10 m11 m12 m13<br>
     * m20 m21 m22 m23<br>
     * m30 m31 m32 m33<br>
     */
    public Matrix4x4(	double m00_, double m01_, double m02_, double m03_,
                         double m10_, double m11_, double m12_, double m13_,
                         double m20_, double m21_, double m22_, double m23_,
                         double m30_, double m31_, double m32_, double m33_) {
        m00=m00_; m01=m01_; m02=m02_; m03=m03_;
        m10=m10_; m11=m11_; m12=m12_; m13=m13_;
        m20=m20_; m21=m21_; m22=m22_; m23=m23_;
        m30=m30_; m31=m31_; m32=m32_; m33=m33_;
    }

    /**
     * copy constructor
     * @param m [in] matrix to copy
     */
    public Matrix4x4(Matrix4x4 m) {
        m00=m.m00; m01=m.m01; m02=m.m02; m03=m.m03;
        m10=m.m10; m11=m.m11; m12=m.m12; m13=m.m13;
        m20=m.m20; m21=m.m21; m22=m.m22; m23=m.m23;
        m30=m.m30; m31=m.m31; m32=m.m32; m33=m.m33;
    }

    /**
     * sets the unit matrix
     */
    public void createUnitMatrix() {
        m00=1.; m01=0.; m02=0.; m03=0.;
        m10=0.; m11=1.; m12=0.; m13=0.;
        m20=0.; m21=0.; m22=1.; m23=0.;
        m30=0.; m31=0.; m32=0.; m33=1.;
    }

    /**
     * creates a rotation matrix around the x-axis
     * @param alpha [in] angle in radians
     */
    public void createXRotationMatrix(double alpha) {
        createUnitMatrix();
        double c = Math.cos(alpha);
        double s = Math.sin(alpha);
        m11=c; m12=-s;
        m21=s; m22=c;
    }

    /**
     * creates a rotation matrix around the y-axis
     * @param beta [in] angle in radians
     */
    public void createYRotationMatrix(double beta) {
        createUnitMatrix();
        double c = Math.cos(beta);
        double s = Math.sin(beta);
        m00=c; m02=s;
        m20=-s; m22=c;
    }

    /**
     * creates a rotation matrix around the z-axis
     * @param gamma [in] angle in radians
     */
    public void createZRotationMatrix(double gamma) {
        createUnitMatrix();
        double c = Math.cos(gamma);
        double s = Math.sin(gamma);
        m00=c; m01=-s;
        m10=s; m11=c;
    }

    /**
     * creates a rotation matrix
     * order: 1. z-rotation, 2. y-rotation, 3. x-rotation
     * @param alpha [in] x-angle in radians
     * @param beta [in] y-angle in radians
     * @param gamma [in] z-angle in radians
     */
    public void createRotationMatrix(double alpha, double beta, double gamma) {
        createUnitMatrix();
        double sina=Math.sin(alpha);
        double sinb=Math.sin(beta);
        double sing=Math.sin(gamma);
        double cosa=Math.cos(alpha);
        double cosb=Math.cos(beta);
        double cosg=Math.cos(gamma);
        m00=cosb*cosg; m01=sina*sinb*cosg-cosa*sing; m02=cosa*sinb*cosg+sina*sing;
        m10=cosb*sing; m11=sina*sinb*sing+cosa*cosg; m12=cosa*sinb*sing-sina*sing;
        m20=-sinb; m21=sina*cosb; m22=cosa*cosb;
    }

    /**
     * creates a translation matrix from a vector
     * @param v [in] translation vector
     */
    public void createTranslationMatrix(Vector3D v) {
        createUnitMatrix();
        m03=v.x;
        m13=v.y;
        m23=v.z;
    }

    /**
     * creates a scale matrix in x-direction
     * @param sx [in] scaling
     */
    public void createXScaleMatrix(double sx) {
        createUnitMatrix();
        m00=sx;
    }

    /**
     * creates a scale matrix in y-direction
     * @param sy [in] scaling
     */
    public void createYScaleMatrix(double sy) {
        createUnitMatrix();
        m11=sy;
    }

    /**
     * creates a scale matrix in z-direction
     * @param sz [in] scaling
     */
    public void createZScaleMatrix(double sz) {
        createUnitMatrix();
        m22=sz;
    }

    /**
     * creates a scale matrix in all directions
     * @param sx [in] x-scaling
     * @param sy [in] y-scaling
     * @param sz [in] z-scaling
     */
    public void createScaleMatrix(double sx, double sy, double sz) {
        createUnitMatrix();
        m00=sx;
        m11=sy;
        m22=sz;
    }
}
