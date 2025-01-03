package mylie.core.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;

public class Schedulers {
	public static Scheduler forkJoin() {
		return new ExecutorBaseScheduler(ForkJoinPool.commonPool());
	}

	public static Scheduler workStealing(int threads) {
		return new ExecutorBaseScheduler(Executors.newWorkStealingPool(threads));
	}

	public static Scheduler threadPool(int threads) {
		return new ExecutorBaseScheduler(Executors.newFixedThreadPool(threads));
	}

	public static Scheduler virtualThreads() {
		return new ExecutorBaseScheduler(Executors.newVirtualThreadPerTaskExecutor());
	}

	public static Scheduler singleThreaded() {
		return new SingleThreadedScheduler();
	}

	static final class ExecutorBaseScheduler extends MultiThreadedScheduler {
		final ExecutorService executorService;
		final TaskExecutor backgroundTaskExecutor = new TaskExecutor() {
			@Override
			<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
					Supplier<R> function) {
				Result.Completable<R> result = new Result.Completable<>(hash, version, new CompletableFuture<>(),
						function, target);
				cache.result(result);
				executorService.execute(result::result);
				return result;
			}
		};

		public ExecutorBaseScheduler(ExecutorService executorService) {
			this.executorService = executorService;
			target(Async.Target.Any, backgroundTaskExecutor);
		}

	}

	static non-sealed abstract class MultiThreadedScheduler extends Scheduler {
		public MultiThreadedScheduler() {
			super(new MapCache());
		}

		@Override
		protected void target(Async.Target target, Consumer<Runnable> drain) {

		}

		@AllArgsConstructor
		private static class MultiThreadedTaskExecutor extends TaskExecutor {
			private final Async.Target target;
			private final Consumer<Runnable> drain;

			@Override
			<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
					Supplier<R> function) {
				Result.Completable<R> result = new Result.Completable<>(hash, version, new CompletableFuture<>(),
						function, target);
				cache.result(result);
				drain.accept(result::result);
				return result;
			}
		}
	}

	static final class SingleThreadedScheduler extends Scheduler {
		public SingleThreadedScheduler() {
			super(new MapCache());
			target(Async.Target.Any, taskExecutor);
		}

		private static final TaskExecutor taskExecutor = new TaskExecutor() {
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
	}
}
