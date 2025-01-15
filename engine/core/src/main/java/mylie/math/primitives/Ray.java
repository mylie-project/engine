package mylie.math.primitives;

import mylie.math.Vector3f;

public record Ray(Vector3f origin, Vector3f direction) implements Shape {

}
