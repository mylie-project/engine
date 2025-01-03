package mylie.core.async;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Async {
	@Setter(AccessLevel.PACKAGE)
	static Scheduler SCHEDULER;
	@Synchronized
	public static <R> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F0<R> function) {
		int hash = hash(function);
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function);
		if (result != null)
			return result;
		return executeFunction(executionMode, target, cache, version, hash, function::apply);
	}

	@Synchronized
	public static <R, A> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F1<A, R> function, A a) {
		int hash = hash(function, a);
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a);
		if (result != null)
			return result;
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a));
	}

	@Synchronized
	public static <R, A, B> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F2<A, B, R> function, A a, B b) {
		int hash = hash(function, a, b);
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a, b);
		if (result != null)
			return result;
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a, b));
	}

	@Synchronized
	public static <R, A, B, C> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version,
			Function.F3<A, B, C, R> function, A a, B b, C c) {
		int hash = hash(function, a, b, c);
		Result<R> result = cache.result(hash, version);
		logAsyncCall(target, hash, result, function, a, b, c);
		if (result != null)
			return result;
		return executeFunction(executionMode, target, cache, version, hash, () -> function.apply(a, b, c));
	}

	private static <R> Result<R> executeFunction(ExecutionMode executionMode, Target target, Cache cache, long version,
			int hash, Supplier<R> function) {
		if (executeDirect(executionMode, target)) {
			Result.Fixed<R> result = Result.fixed(hash, version);
			cache.result(result);
			result.result(function.get());
			return result;
		}
		return SCHEDULER.executeFunction(target, cache, version, hash, function);
	}

	private static boolean executeDirect(ExecutionMode executionMode, Target target) {
		if (executionMode == ExecutionMode.Async)
			return false;
		if (Target.Any.equals(target))
			return true;
		return targetIsCurrentThread(target);
	}

	static final ThreadLocal<Target> CURRENT_THREAD_TARGET = new ThreadLocal<>();
	private static boolean targetIsCurrentThread(Target target) {
		return CURRENT_THREAD_TARGET.get() == target;
	}

	private static int hash(Function.F function, Object... args) {
		return Objects.hash(function, Arrays.hashCode(args));
	}

	private static void logAsyncCall(Target target, int hash, Result<?> result, Function.F function, Object... args) {
		log.trace("Function.F{}<{}>Target:{} Hash:{} Cached:{} Args:{}", args.length, function.id(), target, hash,
				result != null, Arrays.toString(args));
	}

	public record Target(String id) {
		public static final Target Any = new Target("Any");
	}

	public enum ExecutionMode {
		Direct, Async
	}
}
