package mylie.async;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for scheduling tasks, supporting both single-threaded
 * and multi-threaded implementations. Manages task execution, caching,
 * and lifecycle events such as initialization and shutdown.
 */
@Slf4j
@Setter
public abstract sealed class Scheduler permits Schedulers.SingleThreadedScheduler, Schedulers.MultiThreadedScheduler {
	@Getter
	private final boolean multiThreaded;
	private final Cache globalCache;
	private final Set<Cache> caches = new HashSet<>();
	private final Map<Async.Target, TaskExecutor> taskExecutors = new HashMap<>();

	/**
	 * Constructs a Scheduler instance.
	 *
	 * @param multiThreaded whether the Scheduler supports multi-threading.
	 * @param globalCache   the global cache used by the Scheduler.
	 */
	protected Scheduler(boolean multiThreaded, Cache globalCache) {
		this.multiThreaded = multiThreaded;
		this.globalCache = globalCache;
		cache(Caches.No);
		cache(Caches.OneFrame);
		cache(Caches.InvalidateOlder);
		cache(Caches.Forever);
		cache(Caches.InvalidateDifferent);
	}

	/**
	 * Executes a function asynchronously with the specified target, cache, and versioning.
	 *
	 * @param target   the target on which the function executes.
	 * @param cache    the cache to store the result.
	 * @param version  the version of the execution context.
	 * @param hash     a unique hash identifying the function.
	 * @param function the function to be executed.
	 * @param <R>      the result type of the function.
	 * @return the result of the function encapsulated in a {@link Result}.
	 */
	<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
			Supplier<R> function) {
		TaskExecutor taskExecutor = taskExecutors.get(target);
		assert taskExecutor != null;
		return taskExecutor.executeFunction(target, cache, version, hash, function);
	}

	/**
	 * Registers a task executor for a specific target.
	 *
	 * @param target       the target for which the executor is registered.
	 * @param taskExecutor the executor responsible for handling tasks for the target.
	 */
	void target(Async.Target target, TaskExecutor taskExecutor) {
		log.trace("Target<{}> registered", target.id());
		taskExecutors.put(target, taskExecutor);
	}

	/**
	 * Registers a target with a custom task-drain consumer.
	 *
	 * @param target the target to register.
	 * @param drain  a consumer that processes runnable tasks.
	 */
	public abstract void target(Async.Target target, Consumer<Runnable> drain);

	/**
	 * Registers and associates a cache with the Scheduler.
	 *
	 * @param cache the cache to register.
	 */
	void cache(Cache cache) {
		log.trace("Cache<{}> registered", cache.id());
		caches.add(cache);
		cache.parent(globalCache);
	}

	/**
	 * Updates the Scheduler's state for the given frame, invalidating associated caches.
	 *
	 * @param frameId the ID of the current frame.
	 */
	public void update(long frameId) {
		for (Cache cache : caches) {
			cache.invalidate();
		}
	}

	/**
	 * Handles cleanup and shutdown logic for the Scheduler.
	 */
	public abstract void onShutdown();

	/**
	 * Initializes the Scheduler by resetting its global cache and all associated caches.
	 */
	public void initialize() {
		globalCache.reset();
		for (Cache cache : caches) {
			cache.reset();
		}
	}

	/**
	 * Abstract executor for handling and executing tasks in a Scheduler.
	 */
	abstract static class TaskExecutor {
		abstract <R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
				Supplier<R> function);
	}

	/**
	 * A thread-safe cache implementation backed by a {@link java.util.concurrent.ConcurrentHashMap}.
	 */
	protected static class MapCache extends Cache {
		private final Map<Async.Hash, Result<?>> store = new java.util.concurrent.ConcurrentHashMap<>();

		/**
		 * Constructs a MapCache instance.
		 */
		public MapCache() {
			super("MapCache", null);
		}

		@SuppressWarnings("unchecked")
		@Override
		<R> Result<R> result(Async.Hash hash, long version) {
			return (Result<R>) store.get(hash);
		}

		/**
		 * Stores the specified result in the cache.
		 *
		 * @param result the result to cache.
		 * @param <R>    the result type.
		 */
		@Override
		<R> void result(Result<R> result) {
			store.put(result.hash(), result);
		}

		@Override
		void invalidate() {

		}

		@Override
		void remove(Async.Hash hash) {
			store.remove(hash);
		}

		@Override
		public void reset() {
			store.clear();
		}
	}
}
