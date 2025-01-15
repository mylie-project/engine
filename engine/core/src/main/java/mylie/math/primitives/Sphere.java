package mylie.math.primitives;

import mylie.math.Vector3f;

public record Sphere(Vector3f center, float radius,float radiusSquarred) implements Shape {
    public Sphere(Vector3f center, float radius) {
        this(center,radius,radius*radius);
    }
}
