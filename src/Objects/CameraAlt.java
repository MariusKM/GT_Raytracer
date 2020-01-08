package Objects;

import math.Vec3;
import math.Vector3;

public class CameraAlt {
    Vec3 origin;
    Vec3 lower_left_corner;
    Vec3 horizontal;
    Vec3 vertical;
    Vec3 u, v, w;
    double time0, time1;
    double lens_radius;

    /**
     * @param lookfrom the point in space where the camera is
     * @param lookat the point in space where the camera is looking at
     * @param vup the upward direction
     * @param hfov the horizonal field of view in degrees
     * @param aspect the aspect ratio of the output
     */
    public CameraAlt(Vec3 lookfrom, Vec3 lookat, Vec3 vup, double hfov, double aspect){

        double theta = hfov*Math.PI/180;
        double half_width = Math.tan(theta/2);
        double half_height = half_width/aspect;
        origin = lookfrom;
        w = Vec3.unit_vector(lookfrom.sub(lookat)); //vector pointing into of camera
        u = Vec3.unit_vector(Vec3.cross(vup,w)); //vector pointing out of side of camera, orthogonal to both view and up direction
        v = Vec3.cross(w,u); //vector pointing out top of camera
        //lower_left_corner = new Vec3(-half_width,-half_height,-1.0);
        lower_left_corner = origin.sub(u.mul(half_width)).sub(v.mul(half_height));
        horizontal = u.mul(2*half_width);
        vertical = v.mul(2*half_height);
    }

    /**
     * @param x horizontal position on the screen
     * @param y vertical position on the screen
     */
    public Ray get_ray(int x, int y){

        Vec3 or = origin;
        Vec3 dir = lower_left_corner.add(horizontal.mul(x)).add(vertical.mul(y)).sub(origin);
        return new Ray( new Vector3((float)or.x(),(float)or.y(),(float)or.z()),new Vector3((float)dir.x(),(float)dir.y(),(float)dir.z()) );
    }

    private Vec3 random_in_unit_disk(){
        Vec3 p;
        do{
            p = new Vec3(Math.random(),Math.random(),0).mul(2).sub(new Vec3(1,1,0));
        } while(Vec3.dot(p,p) >= 1.0);
        return p;
    }
}