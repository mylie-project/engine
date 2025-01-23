package mylie.async;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/// A utility class that defines a hierarchy of abstract function types, represented
/// as nested static classes. This class provides foundational abstractions for working
/// with functions that take a variable number of parameters and return a result.
/// The functions defined in this class are identified uniquely by an ID, which allows
/// them to be used and referenced in various contexts, including asynchronous execution
/// scenarios using related classes such as `Async`.
/// The hierarchy includes:
/// - `F`: The base abstract class for all function definitions, containing a unique identifier.
/// - `F0<R>`: A function with no arguments returning a result of type `R`.
/// - `F1<A, R>`: A function with one argument of type `A` returning a result of type `R`.
/// - `F2<A, B, R>`: A function with two arguments of types `A` and `B` returning a result of type `R`.
/// - `F3<A, B, C, R>`: A function with three arguments of types `A`, `B`, and `C` returning a result of type `R`.
/// Subclasses are expected to provide concrete implementations of the `apply` methods
/// corresponding to the respective function signatures.
public class Function {
	private Function() {
	}
	/// Represents a base abstract class for various types of functions.
	/// This class serves as a common foundation, holding a unique identifier for
	/// each function.
	/// Subclasses of this class are typically used to define specific function
	/// signatures with
	/// varying numbers of parameters and result types.
	@AllArgsConstructor
	abstract static class F {
		@Getter(AccessLevel.PACKAGE)
		private final String id;
	}

	/// Represents a function with no arguments that produces a result of type `R`.
	/// This is an abstract class extending [F], requiring an implementation of the
	/// [#apply()] method.
	///
	/// @param <R> the result type of the function
	public abstract static class F0<R> extends F {
		protected F0(String id) {
			super(id);
		}
		protected abstract R apply();
	}

	/// Represents a function that takes one argument of type `A` and produces a
	/// result of type `R`.
	/// This is an abstract class extending [F], requiring an
	/// implementation of the
	/// 'apply(A)' method to define the function's behavior.
	///
	/// @param <A> the type of the input to the function
	/// @param <R> the result type of the function
	public abstract static class F1<A, R> extends F {
		protected F1(String id) {
			super(id);
		}
		protected abstract R apply(A a);
	}

	/// Represents a function that takes two arguments of types `A` and `B`,
	/// and produces a result of type `R`. This is an abstract class extending
	/// [F], requiring an implementation of the `apply(A, B)`
	/// method to define the function's behavior.
	///
	/// @param <A> the type of the first input to the function
	/// @param <B> the type of the second input to the function
	/// @param <R> the result type of the function
	public abstract static class F2<A, B, R> extends F {
		protected F2(String id) {
			super(id);
		}
		protected abstract R apply(A a, B b);
	}

	/// Represents a function that takes three arguments of types `A`, `B`, and `C`
	/// and produces a result of type `R`. This is an abstract class extending
	/// [F],
	/// requiring an implementation of the `apply(A, B, C)` method to define
	/// the function's behavior.
	///
	/// @param <A> the type of the first input to the function
	/// @param <B> the type of the second input to the function
	/// @param <C> the type of the third input to the function
	/// @param <R> the result type of the function
	public static abstract class F3<A, B, C, R> extends F {
		protected F3(String id) {
			super(id);
		}
		protected abstract R apply(A a, B b, C c);
	}
}
