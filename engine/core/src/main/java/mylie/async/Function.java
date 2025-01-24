package mylie.async;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class provides a set of abstract functional representations (F0, F1, F2, F3)
 * that can be used for defining generic functional operations with varying arity (0-3).
 */
public class Function {
    private Function() {}

    /**
     * Base abstract class representing a function with a unique identifier.
     */
    @AllArgsConstructor
    abstract static class F {
        /**
         * The unique identifier for the function.
         */
        @Getter(AccessLevel.PACKAGE)
        private final String id;
    }

    /**
     * Represents a function with no arguments (arity 0) and a return value of type R.
     *
     * @param <R> the return type of the function
     */
    public abstract static class F0<R> extends F {
        protected F0(String id) {
            super(id);
        }

        /**
         * Executes the function and returns the result.
         *
         * @return the result of the function
         */
        protected abstract R apply();
    }

    /**
     * Represents a function with one argument (arity 1) and a return value of type R.
     *
     * @param <A> the type of the function's argument
     * @param <R> the return type of the function
     */
    public abstract static class F1<A, R> extends F {
        protected F1(String id) {
            super(id);
        }

        /**
         * Executes the function using the provided argument and returns the result.
         *
         * @param a the input to the function
         * @return the result of the function
         */
        protected abstract R apply(A a);
    }

    /**
     * Represents a function with two arguments (arity 2) and a return value of type R.
     *
     * @param <A> the type of the first argument
     * @param <B> the type of the second argument
     * @param <R> the return type of the function
     */
    public abstract static class F2<A, B, R> extends F {
        protected F2(String id) {
            super(id);
        }

        /**
         * Executes the function using the provided arguments and returns the result.
         *
         * @param a the first input to the function
         * @param b the second input to the function
         * @return the result of the function
         */
        protected abstract R apply(A a, B b);
    }

    /**
     * Represents a function with three arguments (arity 3) and a return value of type R.
     *
     * @param <A> the type of the first argument
     * @param <B> the type of the second argument
     * @param <C> the type of the third argument
     * @param <R> the return type of the function
     */
    public static abstract class F3<A, B, C, R> extends F {
        protected F3(String id) {
            super(id);
        }

        /**
         * Executes the function using the provided arguments and returns the result.
         *
         * @param a the first input to the function
         * @param b the second input to the function
         * @param c the third input to the function
         * @return the result of the function
         */
        protected abstract R apply(A a, B b, C c);
    }
}
