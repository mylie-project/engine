package mylie.core.async;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SingleThreadedScheduler extends Scheduler {
	public SingleThreadedScheduler() {
		super(new MapCache());
		target(Async.Target.Any, taskExecutor);
	}

	private static final TaskExecutor taskExecutor=new TaskExecutor() {
		@Override
		<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
									  Supplier<R> function) {
			Result.Fixed<R> result = Result.fixed(hash, version);
			cache.result(result);
			result.result(function.get());
			return result;
		}
	};

	@Override
	protected void target(Async.Target target, Consumer<Runnable> drain) {
		target(target, taskExecutor);
	}



	protected static class MapCache extends Cache {
		private final Map<Async.Hash, Result<?>> store = new java.util.concurrent.ConcurrentHashMap<>();
		public MapCache() {
			super("MapCache", null);
		}

		@SuppressWarnings("unchecked")
		@Override
		<R> Result<R> result(Async.Hash hash, long version) {
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
