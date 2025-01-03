package mylie.core.async;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Setter
public abstract class Scheduler {
	private final Cache globalCache;
	private final Set<Cache> caches = new HashSet<>();
	private final Map<Async.Target, TaskExecutor> taskExecutors = new HashMap<>();

	public Scheduler(Cache globalCache) {
		this.globalCache = globalCache;
		cache(Cache.No);
		cache(Cache.OneFrame);
		cache(Cache.InvalidateOlder);
		cache(Cache.Forever);
		cache(Cache.InvalidateDifferent);
	}

	<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash, Supplier<R> function) {
		TaskExecutor taskExecutor = taskExecutors.get(target);
		assert taskExecutor != null;
		return taskExecutor.executeFunction(target, cache, version, hash, function);
	}

	void target(Async.Target target, TaskExecutor taskExecutor) {
		log.trace("Target<{}> registered", target.id());
		taskExecutors.put(target, taskExecutor);
	}

	protected abstract void target(Async.Target target, Consumer<Runnable> drain);

	void cache(Cache cache) {
		log.trace("Cache<{}> registered", cache.id());
		caches.add(cache);
		cache.parent(globalCache);
	}

	abstract static class TaskExecutor {
		abstract <R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash, Supplier<R> function);
	}
}
