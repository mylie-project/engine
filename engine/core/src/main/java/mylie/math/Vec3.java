package mylie.math;

@SuppressWarnings("unused")
public interface Vec3<V extends Vec<V,N>,V2 extends Vec2<V2,N>,V3 extends Vec3<V,V2,V3, N>, N extends Number> extends Vec<V3, N> {

	/**
	 * Calculates the cross product of this vector and the given vector.
	 *
	 * @param other the other vector
	 * @return the resulting vector after the cross product
	 */
	V cross(V other);

	/**
	 * Retrieves a 2D vector containing the x and y components of this vector.
	 *
	 * @return a 2D vector (x, y)
	 */
	V2 xy();

	/**
	 * Retrieves a 2D vector containing the x and z components of this vector.
	 *
	 * @return a 2D vector (x, z)
	 */
	V2 xz();

	/**
	 * Retrieves a 2D vector containing the y and z components of this vector.
	 *
	 * @return a 2D vector (y, z)
	 */
	V2 yz();

	/**
	 * Retrieves a 2D vector containing the y and x components of this vector.
	 *
	 * @return a 2D vector (y, x)
	 */
	V2 yx();

	/**
	 * Retrieves a 2D vector containing the z and x components of this vector.
	 *
	 * @return a 2D vector (z, x)
	 */
	V2 zx();

	/**
	 * Retrieves a 2D vector containing the z and y components of this vector.
	 *
	 * @return a 2D vector (z, y)
	 */
	V2 zy();

	/**
	 * Retrieves a new 2D vector where both components are the x component of this vector.
	 *
	 * @return a 2D vector (x, x)
	 */
	V2 xx();

	/**
	 * Retrieves a new 2D vector where both components are the y component of this vector.
	 *
	 * @return a 2D vector (y, y)
	 */
	V2 yy();

	/**
	 * Retrieves a new 2D vector where both components are the z component of this vector.
	 *
	 * @return a 2D vector (z, z)
	 */
	V2 zz();
}
