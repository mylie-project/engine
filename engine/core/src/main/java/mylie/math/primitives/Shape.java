package mylie.math.primitives;

import mylie.math.Collision;

public interface Shape {
	default boolean collidesWith(Shape other) {
		return Collision.collides(this,other);
	}
}
