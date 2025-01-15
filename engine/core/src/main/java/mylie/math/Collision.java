package mylie.math;

import mylie.math.primitives.AlignedBox;
import mylie.math.primitives.Ray;
import mylie.math.primitives.Shape;
import mylie.math.primitives.Sphere;

import java.lang.Math;

public class Collision {
    public static boolean collides(Shape a, Shape b) {
        return switch (a) {
            case AlignedBox aba -> switch (b) {
                case AlignedBox abb -> collidesABAB(aba, abb);
                case Ray rayb -> collidesRayAbox(rayb, aba);
                case Sphere sphb -> collidesABSphere(aba, sphb);
                default -> unsupported(a, b);
            };
            case Ray raya -> switch (b){
                case AlignedBox abb -> collidesRayAbox(raya, abb);
                case Sphere sphere -> collidesRaySphere(raya, sphere);
                default -> unsupported(a, b);
            };
            case Sphere sphere -> switch (b){
                case AlignedBox abb -> collidesABSphere(abb, sphere);
                case Ray rayb -> collidesRaySphere(rayb, sphere);
                case Sphere sphb -> collidesSphereSphere(sphere, sphb);
                default -> unsupported(a, b);
            };
            default -> unsupported(a, b);
        };
    }

    public static boolean unsupported(Shape a, Shape b){
        throw new UnsupportedOperationException("Unsupported shape combination: " + a + " " + b);
    }

    public static boolean collidesSphereSphere(Sphere a,Sphere b){
        float distanceSquared = a.center().distanceSquared(b.center());
        float radiusSum = a.radius() + b.radius();
        radiusSum*=radiusSum;
        return distanceSquared <= radiusSum;
    }

    public static boolean collidesRaySphere(Ray ray, Sphere sphere) {
        // Extract sphere data
        float cx = sphere.center().x();
        float cy = sphere.center().y();
        float cz = sphere.center().z();
        float radiusSquared = sphere.radiusSquarred();

        // Extract ray data
        float ox = ray.origin().x();
        float oy = ray.origin().y();
        float oz = ray.origin().z();
        float dx = ray.direction().x();
        float dy = ray.direction().y();
        float dz = ray.direction().z();

        // Vector from ray origin to sphere center
        float ocx = cx - ox;
        float ocy = cy - oy;
        float ocz = cz - oz;

        // Compute projection of `oc` onto the ray direction (`tClosest`)
        float tClosest = ocx * dx + ocy * dy + ocz * dz;

        // Compute the squared distance from sphere center to the ray
        float distanceSquaredToRay = ocx * ocx + ocy * ocy + ocz * ocz - tClosest * tClosest;

        // Check if the ray misses the sphere
        return distanceSquaredToRay <= radiusSquared;
    }

    public static boolean collidesABAB(AlignedBox a, AlignedBox b) {
        // Return false as soon as any axis shows no overlap
        return !(a.max().x() < b.min().x() || a.min().x() > b.max().x() ||
                a.max().y() < b.min().y() || a.min().y() > b.max().y() ||
                a.max().z() < b.min().z() || a.min().z() > b.max().z());
    }

    public static boolean collidesABSphere(AlignedBox box, Sphere sphere) {
        // Get sphere center and radius squared
        float cx = sphere.center().x();
        float cy = sphere.center().y();
        float cz = sphere.center().z();
        float radiusSquared = sphere.radiusSquarred();

        // Initialize squared distance
        float distanceSquared = 0;

        // Check X axis
        if (cx < box.min().x()) { // Sphere center is to the left of the box
            float dx = box.min().x() - cx;
            distanceSquared += dx * dx;
            if (distanceSquared > radiusSquared) return false; // Early exit
        } else if (cx > box.max().x()) { // Sphere center is to the right of the box
            float dx = cx - box.max().x();
            distanceSquared += dx * dx;
            if (distanceSquared > radiusSquared) return false; // Early exit
        }

        // Check Y axis
        if (cy < box.min().y()) { // Sphere center is below the box
            float dy = box.min().y() - cy;
            distanceSquared += dy * dy;
            if (distanceSquared > radiusSquared) return false; // Early exit
        } else if (cy > box.max().y()) { // Sphere center is above the box
            float dy = cy - box.max().y();
            distanceSquared += dy * dy;
            if (distanceSquared > radiusSquared) return false; // Early exit
        }

        // Check Z axis
        if (cz < box.min().z()) { // Sphere center is in front of the box
            float dz = box.min().z() - cz;
            distanceSquared += dz * dz;
            return !(distanceSquared > radiusSquared); // Early exit
        } else if (cz > box.max().z()) { // Sphere center is behind the box
            float dz = cz - box.max().z();
            distanceSquared += dz * dz;
            return !(distanceSquared > radiusSquared); // Early exit
        }

        // If we get here, the distance squared is less than or equal to the squared radius
        return true;
    }

    public static boolean collidesRayAbox(Ray ray, AlignedBox box) {
        Vector3f tMin = box.min().sub(ray.origin()).div(ray.direction());
        Vector3f tMax = box.max().sub(ray.origin()).div(ray.direction());
        Vector3f t1 = tMin.min(tMax);
        Vector3f t2 = tMin.max(tMax);
        float tNear = Math.max(Math.max(t1.x(), t1.y()), t1.z());
        float tFar = Math.min(Math.min(t2.x(), t2.y()), t2.z());
        return tNear<=tFar;
    }
}
