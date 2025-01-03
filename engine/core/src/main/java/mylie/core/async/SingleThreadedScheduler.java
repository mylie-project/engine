package mylie.core.async;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SingleThreadedScheduler extends Scheduler implements Scheduler.TaskExecutor {
	public SingleThreadedScheduler() {
		super(new MapCache());
	}

	@Override
	protected void target(Async.Target target, Consumer<Runnable> drain) {
		target(target, this);
	}

	@Override
	public <R> Result<R> executeFunction(Async.Target target, Cache cache, long version, int hash,
			Supplier<R> function) {
		Result.Fixed<R> result = Result.fixed(hash, version);
		cache.result(result);
		result.result(function.get());
		return result;
	}

	protected static class MapCache extends Cache {
		private final Map<Integer, Result<?>> store = new java.util.concurrent.ConcurrentHashMap<>();
		public MapCache() {
			super("MapCache", null);
		}

		@SuppressWarnings("unchecked")
        @Override
		<R> Result<R> result(int hash, long version) {
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
		void remove(int hash) {
			store.remove(hash);
		}
	}
}
