package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;

public class Schedulers {
	private Schedulers() {
	}
	public static Scheduler forkJoin() {
		return new ExecutorScheduler(ForkJoinPool.commonPool());
	}

	public static Scheduler workStealing(int threads) {
		return new ExecutorScheduler(Executors.newWorkStealingPool(threads));
	}

	public static Scheduler threadPool(int threads) {
		return new ExecutorScheduler(Executors.newFixedThreadPool(threads));
	}

	public static Scheduler virtualThreads() {
		return new ExecutorScheduler(Executors.newVirtualThreadPerTaskExecutor());
	}

	public static Scheduler singleThreaded() {
		return new SingleThreadedScheduler();
	}

	static final class ExecutorScheduler extends MultiThreadedScheduler {
		final ExecutorService executorService;
		final TaskExecutor backgroundTaskExecutor = new TaskExecutor() {
			@Override
			<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
					Supplier<R> function) {
				Result.Completable<R> result = Result.completable(hash, version, new CompletableFuture<>(), function,
						target);
				cache.result(result);
				Async.unlock();
				executorService.execute(() -> {
					if (result.running().compareAndSet(false, true)) {
						result.future().complete(function.get());
					}
				});
				return result;
			}
		};

		public ExecutorScheduler(ExecutorService executorService) {
			this.executorService = executorService;
			target(Async.Target.Any, backgroundTaskExecutor);
		}

		@Override
		public void onShutdown() {
			executorService.shutdown();
		}
	}

	static abstract non-sealed class MultiThreadedScheduler extends Scheduler {
		protected MultiThreadedScheduler() {
			super(true, new MapCache());
		}

		@Override
		public void target(Async.Target target, Consumer<Runnable> drain) {
			target(target, new MultiThreadedTaskExecutor(drain));
		}

		@AllArgsConstructor
		private static class MultiThreadedTaskExecutor extends TaskExecutor {
			private final Consumer<Runnable> drain;

			@Override
			<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
					Supplier<R> function) {
				Result.Completable<R> result = Result.completable(hash, version, new CompletableFuture<>(), function,
						target);
				cache.result(result);
				Async.unlock();
				drain.accept(result::result);
				return result;
			}
		}
	}

	static final class SingleThreadedScheduler extends Scheduler {
		public SingleThreadedScheduler() {
			super(false, new MapCache());
			target(Async.Target.Any, taskExecutor);
		}

		private static final TaskExecutor taskExecutor = new TaskExecutor() {
			@Override
			<R> Result<R> executeFunction(Async.Target target, Cache cache, long version, Async.Hash hash,
					Supplier<R> function) {
				Result.Fixed<R> result = Result.fixed(hash, version);
				cache.result(result);
				Async.unlock();
				result.result(function.get());
				return result;
			}
		};

		@Override
		public void target(Async.Target target, Consumer<Runnable> drain) {
			target(target, taskExecutor);
		}

		@Override
		public void onShutdown() {
			// Nothing to shut down
		}
	}
}
