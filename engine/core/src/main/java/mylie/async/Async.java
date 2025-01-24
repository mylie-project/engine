package mylie.async;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code Async} class provides utility methods for asynchronous task execution.
 * It supports caching, direct or async execution modes, and handles thread safety
 * using locks. The class operates with abstractions like {@link Scheduler}, {@link Cache},
 * {@link Function}, and {@link Result}, making it possible to optimize and manage
 * task invocation in a structured and reusable way.
 *
 * <p>One key feature is the ability to deduplicate tasks by caching task results
 * against a hash, thus avoiding redundant execution.</p>
 *
 * <p>Make sure to initialize the {@link Scheduler} before using any async features
 * and ensure proper shutdown to release resources.</p>
 * <p>
 *
 * @see Scheduler
 * @see Result
 * @see ExecutionMode
 */
@Slf4j
public class Async {
    static Scheduler asyncScheduler;

    /**
     * Initializes the async scheduler with the given scheduler instance.
     *
     * @param scheduler the {@link Scheduler} to initialize and set as the active scheduler.
     * @throws IllegalStateException if the scheduler cannot be initialized.
     */
    public static void initialize(Scheduler scheduler) {
        asyncScheduler = scheduler;
        asyncScheduler.initialize();
        log.trace("Async scheduler set to {}", scheduler.getClass().getSimpleName());
    }

    /**
     * Shuts down the current async scheduler.
     *
     * @throws IllegalStateException if the async scheduler has not been set.
     */
    public static void shutdown() {
        if (asyncScheduler == null)
            throw new IllegalStateException("Async scheduler not set");
        asyncScheduler.onShutdown();
        asyncScheduler = null;
        log.trace("Async scheduler shut down");
    }

    private static final Lock lock = new ReentrantLock();

    /**
     * Acquires the global lock for thread-safe operations.
     */
    static void lock() {
        lock.lock();
    }

    /**
     * Releases the previously acquired global lock.
     *
     * @throws IllegalMonitorStateException if the current thread has not acquired the lock.
     */
    static void unlock() {
        lock.unlock();
    }

    /**-
     * Executes an asynchronous function that takes no arguments.
     *
     * @param <R>           the type of the result provided by the function.
     * @param executionMode the {@link ExecutionMode} to use (DIRECT or ASYNC).
     * @param target        the {@link Target} specifying where or how to execute the task.
     * @param cache         the {@link Cache} used to check for and store computed results.
     * @param version       the task version, used for caching validation.
     * @param function      the function to be executed asynchronously.
     * @return the {@link Result} of the function's execution, retrieved from cache if available; otherwise computed.
     */
    public static <R> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version, Function.F0<R> function) {
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

    /**
     * Executes an asynchronous function that takes one argument.
     *
     * @param <R>           the type of the result provided by the function.
     * @param <A>           the type of the argument accepted by the function.
     * @param executionMode the {@link ExecutionMode} to use (DIRECT or ASYNC).
     * @param target        the {@link Target} specifying where or how to execute the task.
     * @param cache         the {@link Cache} used to check for and store computed results.
     * @param version       the task version, used for caching validation.
     * @param function      the function to be executed asynchronously.
     * @param a             the argument to pass to the function.
     * @return the {@link Result} of the function's execution, retrieved from cache if available; otherwise computed.
     */

    public static <R, A> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version, Function.F1<A, R> function, A a) {
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

    /**
     * Executes an asynchronous function that takes two arguments.
     *
     * @param <R>           the type of the result provided by the function.
     * @param <A>           the type of the first argument accepted by the function.
     * @param <B>           the type of the second argument accepted by the function.
     * @param executionMode the {@link ExecutionMode} to use (DIRECT or ASYNC).
     * @param target        the {@link Target} specifying where or how to execute the task.
     * @param cache         the {@link Cache} used to check for and store computed results.
     * @param version       the task version, used for caching validation.
     * @param function      the function to be executed asynchronously.
     * @param a             the first argument to pass to the function.
     * @param b             the second argument to pass to the function.
     * @return the {@link Result} of the function's execution, retrieved from cache if available; otherwise computed.
     */
    public static <R, A, B> Result<R> async(ExecutionMode executionMode, Target target, Cache cache, long version, Function.F2<A, B, R> function, A a, B b) {
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

    /**
     * Executes an asynchronous function that takes three arguments.
     *
     * @param <R>           the type of the result provided by the function.
     * @param <A>           the type of the first argument accepted by the function.
     * @param <B>           the type of the second argument accepted by the function.
     * @param <C>           the type of the third argument accepted by the function.
     * @param executionMode the {@link ExecutionMode} to use (DIRECT or ASYNC).
     * @param target        the {@link Target} specifying where or how to execute the task.
     * @param cache         the {@link Cache} used to check for and store computed results.
     * @param version       the task version, used for caching validation.
     * @param function      the function to be executed asynchronously.
     * @param a             the first argument to pass to the function.
     * @param b             the second argument to pass to the function.
     * @param c             the third argument to pass to the function.
     * @return the {@link Result} of the function's execution, retrieved from cache if available; otherwise computed.
     */
    public static <R, A, B, C> Result<R>
            async(ExecutionMode executionMode, Target target, Cache cache, long version, Function.F3<A, B, C, R> function, A a, B b, C c) {
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

    /**
     * Executes a list of asynchronous tasks and returns their results.
     *
     * @param <R>        the type of the results provided by the tasks.
     * @param asyncTasks a list of asynchronous tasks represented as {@link Supplier} instances.
     * @return a collection of {@link Result} objects corresponding to the tasks' execution results.
     */
    public static <R> Collection<Result<R>> async(List<Supplier<Result<R>>> asyncTasks) {
        return async(asyncTasks, new ArrayList<>(asyncTasks.size()));
    }

    /**
     * Executes a list of asynchronous tasks and collects their results in the specified collection.
     *
     * @param <R>        the type of the results provided by the tasks.
     * @param <T>        the type of the collection to store the results.
     * @param asyncTasks a list of asynchronous tasks represented as {@link Supplier} instances.
     * @param results    the collection to store the results of the tasks' execution.
     * @return the collection containing all {@link Result} objects from the executed tasks.
     */
    public static <R, T extends Collection<Result<R>>> Collection<Result<R>> async(List<Supplier<Result<R>>> asyncTasks, T results) {
        for (Supplier<Result<R>> asyncTask : asyncTasks) {
            results.add(asyncTask.get());
        }
        return results;
    }

    /**
     * Executes a function asynchronously on each element of the specified collection and returns the results.
     *
     * @param <R>          the type of the results produced by the function.
     * @param <T>          the type of the elements in the input collection.
     * @param collection   the input collection containing elements to process.
     * @param taskSupplier the function to execute asynchronously for each element.
     * @return a collection of {@link Result} objects containing the results of the function executions.
     */
    public static <R, T> Collection<Result<R>> async(List<T> collection, java.util.function.Function<T, Result<R>> taskSupplier) {
        return async(collection, taskSupplier, new ArrayList<>(collection.size()));
    }

    /**
     * Executes a function asynchronously on each element of the specified collection and collects the results.
     *
     * @param <R>          the type of the results produced by the function.
     * @param <T>          the type of the elements in the input collection.
     * @param <C>          the type of the collection to store the results.
     * @param collection   the input collection containing elements to process.
     * @param taskSupplier the function to execute asynchronously for each element.
     * @param results      the collection used to collect the results of the function executions.
     * @return the collection containing all {@link Result} objects from the executed tasks.
     */
    public static <R, T, C extends Collection<Result<R>>> C async(List<T> collection, java.util.function.Function<T, Result<R>> taskSupplier, C results) {
        for (T t : collection) {
            results.add(taskSupplier.apply(t));
        }
        return results;
    }

    /**
     * Executes the given function either directly or asynchronously, based on the
     * specified {@link ExecutionMode}. If executed directly, the result is cached
     * and returned; otherwise, the function is passed to the configured scheduler
     * for execution.
     *
     * @param <R>           the return type of the function.
     * @param executionMode the mode of execution (async or direct).
     * @param target        the target defining where or how the task is executed.
     * @param cache         the cache for storing or fetching results.
     * @param version       the version of the task for caching validation.
     * @param hash          the calculated hash identifying the task.
     * @param function      the actual function to be executed.
     * @return a {@link Result} object containing the task's output.
     */
    private static <R> Result<R> executeFunction(ExecutionMode executionMode, Target target, Cache cache, long version, Hash hash, Supplier<R> function) {
        if (executeDirect(executionMode, target)) {
            Result.Fixed<R> result = Result.fixed(hash, version);
            cache.result(result);
            unlock();
            result.result(function.get());
            return result;
        }
        return asyncScheduler.executeFunction(target, cache, version, hash, function);
    }

    /**
     * Determines if a task should be executed directly, based on the specified
     * {@link ExecutionMode} and {@link Target}.
     *
     * @param executionMode the mode of execution (DIRECT or ASYNC).
     * @param target        the target context of execution.
     * @return {@code true} if the task is eligible for direct execution,
     * {@code false} otherwise.
     */
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

    /**
     * Generates a {@link Hash} for a given function and its arguments.
     * The hash ensures that the same function with the same arguments maps
     * to the same identifier, enabling efficient caching and deduplication.
     *
     * @param function the function whose unique hash needs to be computed.
     * @param args     the arguments passed to the function.
     * @return a {@link Hash} representing the function and its arguments.
     */
    private static Hash hash(Function.F function, Object... args) {
        return new Hash(function, args);
    }

    /**
     * Logs details about an asynchronous function call, including its target,
     * computed hash, cached result information, and the arguments passed.
     *
     * @param target   the target specifying task execution context.
     * @param hash     the hash identifying the function call.
     * @param result   the result retrieved from cache, if available.
     * @param function the function being called.
     * @param args     the arguments passed to the function.
     */
    private static void logAsyncCall(Target target, Hash hash, Result<?> result, Function.F function, Object... args) {
        log
                .trace(
                        "Function.F{}<{}>Target:{} Hash:{} Cached:{} Args:{}",
                        args.length,
                        function.id(),
                        target,
                        hash.hash(),
                        result != null,
                        Arrays.toString(args));
    }

    /**
     * Represents a target for asynchronous execution. This could signify a specific
     * execution thread, context, or identifier for task deployment.
     *
     * <p>The predefined constant {@code Any} represents a generic target without
     * constraints on execution locality.</p>
     */
    public record Target(String id) {
        public static final Target Any = new Target("Any");
    }

    /**
     * Represents a hash calculation for a function and its arguments.
     * The hash uniquely identifies a specific function invocation, enabling
     * caching and deduplication of results.
     */
    @EqualsAndHashCode
    static class Hash {
        final Function.F function;
        final Object[] args;
        @Getter
        final int hash;

        /**
         * Constructs a `Hash` instance for a given function and its arguments.
         * The hash value is computed based on the function's hash code and
         * the hash codes of its arguments. If an argument implements the
         * {@link Custom} interface, its custom hash value is used.
         *
         * @param function the function to hash.
         * @param args     the arguments for the function.
         */
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

        /**
         * Defines a custom hashing mechanism for specific objects.
         * Classes that implement this interface can provide their own hash
         * function to be used in the {@link Hash} instance.
         */
        interface Custom {
            /**
             * Returns the custom hash value for the implementing object.
             *
             * @return the custom hash value.
             */
            int hash();
        }
    }

    /**
     * Represents the mode of execution for asynchronous tasks.
     * <p>
     * DIRECT: Executes tasks synchronously on the current thread.
     * ASYNC: Executes tasks asynchronously on a separate thread or scheduler.
     * </p>
     */
    public enum ExecutionMode {

        /**
         * Executes tasks synchronously on the current thread.
         */
        DIRECT,

        /**
         * Executes tasks asynchronously on a separate thread or scheduler.
         */
        ASYNC
    }
}
