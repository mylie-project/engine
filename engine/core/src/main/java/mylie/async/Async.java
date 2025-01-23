package mylie.async;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/// The Async class provides methods for asynchronous or synchronous function execution
/// with result caching mechanisms. This class allows executing functions with varying
/// numbers of arguments and different execution modes while optimizing performance
/// using a cache system. The execution can be either direct (synchronous) or scheduled
/// (asynchronous), based on the specified execution mode.
@Slf4j
public class Async {
	static Scheduler asyncScheduler;

	public static void initialize(Scheduler scheduler) {
		if (asyncScheduler != null)
			throw new IllegalStateException("Async scheduler already set");
		asyncScheduler = scheduler;
		log.info("Async scheduler set to {}", scheduler.getClass().getSimpleName());
	}

	private static final Lock lock = new ReentrantLock();

	static void lock() {
		lock.lock();
	}

	static void unlock() {
		lock.unlock();
	}

	/// Executes the provided function asynchronously or synchronously based on the
	/// given execution mode,
	/// and caches the result for future calls based on the function's hash and
	/// version.
	///
	/// @param <R> The type of the result returned by the function.
	/// @param executionMode The mode in which the function should be executed,
	/// either asynchronously or synchronously.
	/// @param target The target object or component relevant to the execution.
	/// @param cache The cache to retrieve and store results for optimization and
	/// reuse.
	/// @param version A specific version to check for result consistency in the
	/// cache.
	/// @param function The function to be executed, whose result is cached or
	/// recalculated if necessary.
	/// @return The result of the executed function, either retrieved from the cache
	/// or freshly executed.
	public static <R> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F0<R> function) {
		Hash hash = hash(function);
		lock();
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function);
		if (result != null) {
			unlock();
			return result;
		}
		return executeFunction(executionMode, target, cache, version, hash, function::apply);
	}

	/// Executes a given function asynchronously based on the provided execution
	/// mode and caching strategy.
	///
	/// @param <R> the result type of the function to be executed
	/// @param <A> the type of the argument passed to the function
	/// @param executionMode the mode in which execution is performed (e.g.,
	/// synchronous or asynchronous)
	/// @param target the target entity involved in the function execution
	/// @param cache the cache instance used to store and retrieve cached results
	/// @param version the version of the call, used for cache validation
	/// @param function the functional interface to be executed
	/// @param a the argument to be passed to the function
	/// @return a `Result<R>` object representing the outcome of the function
	/// execution
	public static <R, A> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F1<A, R> function, A a) {

		Hash hash = hash(function, a);
		lock();
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a);
		if (result != null) {
			unlock();
			return result;
		}
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a));
	}

	/// Executes an asynchronous function with the provided inputs, using the
	/// specified execution mode
	/// and cache mechanism. If a cached result is available for the function and
	/// inputs, it is returned.
	/// Otherwise, the function is executed, and the result is stored in the cache.
	///
	/// @param <R> the result type of the function to be executed
	/// @param <A> The type of the first argument of the function.
	/// @param <B> The type of the second argument of the function.
	/// @param executionMode the mode of execution, determining how the function is
	/// executed (e.g., synchronously or asynchronously)
	/// @param target the target object or context associated with the asynchronous
	/// execution
	/// @param cache the cache used to store and retrieve function execution results
	/// based on input parameters
	/// @param version the version of the cache or function execution, used to
	/// control cache validity
	/// @param function the function to be executed asynchronously
	/// @param a the first input parameter to the function
	/// @param b the second input parameter to the function
	/// @return a `Result<R>` containing the result of the executed function or the
	/// cached result if available
	public static <R, A, B> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F2<A, B, R> function, A a, B b) {
		Hash hash = hash(function, a, b);
		lock();
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a, b);
		if (result != null) {
			unlock();
			return result;
		}
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a, b));
	}

	/// Executes a provided function asynchronously or synchronously based on the
	/// specified execution mode,
	/// caches the result, and logs the call details.
	///
	/// @param <R> The type of the result produced by the function.
	/// @param <A> The type of the first argument of the function.
	/// @param <B> The type of the second argument of the function.
	/// @param <C> The type of the third argument of the function.
	/// @param executionMode The execution mode for running the function, which
	/// determines whether it
	/// should run asynchronously or synchronously.
	/// @param target The target to associate with the execution of the function.
	/// @param cache The cache to store and retrieve results for the function
	/// execution.
	/// @param version The version number to validate and retrieve cached results.
	/// @param function The function to execute with the provided arguments.
	/// @param a The first argument to pass to the function.
	/// @param b The second argument to pass to the function.
	/// @param c The third argument to pass to the function.
	/// @return A `Result<R>` containing the result of the function execution.
	public static <R, A, B, C> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F3<A, B, C, R> function, A a, B b, C c) {
		Hash hash = hash(function, a, b, c);
		lock();
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a, b, c);
		if (result != null) {
			unlock();
			return result;
		}
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a, b, c));
	}

	public static <R> Collection<Result<R>> async(List<Supplier<Result<R>>> asyncTasks) {
		return async(asyncTasks, new ArrayList<>(asyncTasks.size()));
	}

	public static <R, T extends Collection<Result<R>>> Collection<Result<R>> async(List<Supplier<Result<R>>> asyncTasks,
			T results) {
		for (Supplier<Result<R>> asyncTask : asyncTasks) {
			results.add(asyncTask.get());
		}
		return results;
	}

	public static <R, T> Collection<Result<R>> async(List<T> collection,
			java.util.function.Function<T, Result<R>> taskSupplier) {
		return async(collection, taskSupplier, new ArrayList<>(collection.size()));
	}

	public static <R, T, C extends Collection<Result<R>>> C async(List<T> collection,
			java.util.function.Function<T, Result<R>> taskSupplier, C results) {
		for (T t : collection) {
			results.add(taskSupplier.apply(t));
		}
		return results;
	}

	private static <R> Result<R> executeFunction(ExecutionMode executionMode, Target target, Cache cache, long version,
			Hash hash, Supplier<R> function) {
		if (executeDirect(executionMode, target)) {
			Result.Fixed<R> result = Result.fixed(hash, version);
			cache.result(result);
			unlock();
			result.result(function.get());
			return result;
		}
		return asyncScheduler.executeFunction(target, cache, version, hash, function);
	}

	private static boolean executeDirect(ExecutionMode executionMode, Target target) {
		if (executionMode == ExecutionMode.ASYNC)
			return false;
		if (Target.Any.equals(target))
			return true;
		return targetIsCurrentThread(target);
	}

	public static final ThreadLocal<Target> CURRENT_THREAD_TARGET = new ThreadLocal<>();
	private static boolean targetIsCurrentThread(Target target) {
		return CURRENT_THREAD_TARGET.get() == target;
	}

	private static Hash hash(Function.F function, Object... args) {
		return new Hash(function, args);
	}

	private static void logAsyncCall(Target target, Hash hash, Result<?> result, Function.F function, Object... args) {
		log.trace("Function.F{}<{}>Target:{} Hash:{} Cached:{} Args:{}", args.length, function.id(), target,
				hash.hash(), result != null, Arrays.toString(args));
	}

	public record Target(String id) {
		public static final Target Any = new Target("Any");
	}

	@EqualsAndHashCode
	static class Hash {
		final Function.F function;
		final Object[] args;
		@Getter
		final int hash;
		public Hash(Function.F function, Object... args) {
			this.function = function;
			this.args = args;
			int tmpHash = function.hashCode();
			for (Object arg : this.args) {
				tmpHash *= 31;
				if (arg instanceof Custom custom) {
					tmpHash += custom.hash();
				} else {
					tmpHash += Objects.hashCode(arg);
				}
			}
			this.hash = tmpHash;
		}

		interface Custom {
			int hash();
		}
	}

	/// Defines the modes of execution for processing tasks or operations.
	/// This enumeration can be used to specify whether the execution
	/// should proceed in a synchronous (direct) manner or asynchronously.
	public enum ExecutionMode {
		DIRECT, ASYNC
	}
}
