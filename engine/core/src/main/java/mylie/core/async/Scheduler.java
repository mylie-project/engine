package mylie.core.async;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;

@Slf4j
@Setter
public abstract sealed class Scheduler implements Components.CoreComponent
		permits Schedulers.SingleThreadedScheduler, Schedulers.MultiThreadedScheduler {
	private final Cache globalCache;
	private final Set<Cache> caches = new HashSet<>();
	private final Map<Async.Target, TaskExecutor> taskExecutors = new HashMap<>();

	public Scheduler(Cache globalCache) {
		this.globalCache = globalCache;
		cache(Caches.No);
		cache(Caches.OneFrame);
		cache(Caches.InvalidateOlder);
		cache(Caches.Forever);
		cache(Caches.InvalidateDifferent);
	}

	<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
			Supplier<R> function) {
		TaskExecutor taskExecutor = taskExecutors.get(target);
		assert taskExecutor != null;
		return taskExecutor.executeFunction(target, cache, version, hash, function);
	}

	void target(Async.Target target, TaskExecutor taskExecutor) {
		log.trace("Target<{}> registered", target.id());
		taskExecutors.put(target, taskExecutor);
	}

	public abstract void target(Async.Target target, Consumer<Runnable> drain);

	void cache(Cache cache) {
		log.trace("Cache<{}> registered", cache.id());
		caches.add(cache);
		cache.parent(globalCache);
	}

	public void update(long frameId) {
		for (Cache cache : caches) {
			cache.invalidate();
		}
	}

	public abstract void onShutdown();

	abstract static class TaskExecutor {
		abstract <R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
				Supplier<R> function);
	}

	protected static class MapCache extends Cache {
		private final Map<Async.Hash, Result<?>> store = new java.util.concurrent.ConcurrentHashMap<>();
		public MapCache() {
			super("MapCache", null);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected <R> Result<R> result(Async.Hash hash, long version) {
			return (Result<R>) store.get(hash);
		}

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
	}
}
