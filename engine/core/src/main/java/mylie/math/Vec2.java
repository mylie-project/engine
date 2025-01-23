package mylie.math;

@SuppressWarnings("unused")
public interface Vec2<V extends Vec2<V, N>, N extends Number> extends Vec<V, N> {

    /**
     * Returns a new vector with both components set to the x-coordinate of this vector.
     *
     * @return A new vector with the x-coordinate repeated for both components.
     */
    V xx();

    /**
     * Returns a new vector with both components set to the y-coordinate of this vector.
     *
     * @return A new vector with the y-coordinate repeated for both components.
     */
    V yy();

    /**
     * Returns a new vector with the x-coordinate in the y-position and the y-coordinate in the x-position.
     *
     * @return A new vector with swapped x and y components.
     */
    V yx();
}
