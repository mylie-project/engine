package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;

/**
 * Utility class providing various factory methods to create Scheduler instances with different threading models.
 * Schedulers help manage task execution across multiple threads or strategies such as single-threaded, work-stealing, etc.
 * This class cannot be instantiated.
 */
public class Schedulers {
	private Schedulers() {
	}

	/**
	 * Returns a Scheduler backed by the common ForkJoinPool.
	 * This Scheduler is suitable for parallel task execution and load balancing.
	 *
	 * @return a Scheduler using ForkJoinPool.commonPool()
	 */
	public static Scheduler forkJoin() {
		return new ExecutorScheduler(ForkJoinPool.commonPool());
	}

	/**
	 * Creates a Scheduler with a work-stealing thread pool using the specified number of threads.
	 * Work-stealing pools are suitable for unbalanced parallel task processing.
	 *
	 * @param threads the number of threads in the thread pool
	 * @return a Scheduler with a work-stealing thread pool
	 */
	public static Scheduler workStealing(int threads) {
		return new ExecutorScheduler(Executors.newWorkStealingPool(threads));
	}

	/**
	 * Creates a Scheduler with a fixed thread pool, using the specified number of threads.
	 * This Scheduler is suitable for scenarios requiring a bounded number of concurrent tasks.
	 *
	 * @param threads the fixed number of threads in the thread pool
	 * @return a Scheduler backed by a fixed thread pool
	 */
	public static Scheduler threadPool(int threads) {
		return new ExecutorScheduler(Executors.newFixedThreadPool(threads));
	}

	/**
	 * Provides a Scheduler that uses virtual threads (if supported by the Java runtime).
	 * Virtual threads are lightweight threads that enable high concurrency with minimal resource usage.
	 *
	 * @return a Scheduler using virtual threads per task
	 */
	public static Scheduler virtualThreads() {
		return new ExecutorScheduler(Executors.newVirtualThreadPerTaskExecutor());
	}

	/**
	 * Returns a single-threaded Scheduler.
	 * This Scheduler guarantees that all tasks are executed sequentially on a single thread.
	 *
	 * @return a single-threaded Scheduler
	 */
	public static Scheduler singleThreaded() {
		return new SingleThreadedScheduler();
	}

	/**
	 * Scheduler implementation that uses an ExecutorService for task execution.
	 * Tasks are executed in the ExecutorService's thread pool, providing flexibility
	 * and configurability in threading behavior.
	 */
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
						try {
							result.future().complete(function.get());
						} catch (Exception e) {
							result.future().completeExceptionally(e);
						}

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

	abstract static non-sealed class MultiThreadedScheduler extends Scheduler {
		/**
		 * Constructs a MultiThreadedScheduler with default multi-threaded behavior and a MapCache for managing tasks.
		 * This class requires subclasses to provide specific threading models.
		 */
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
		/**
		 * Constructs a SingleThreadedScheduler.
		 * This Scheduler enforces single-threaded execution, ensuring that tasks are executed sequentially.
		 */
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
				result.future().complete(function.get());
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
