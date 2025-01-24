package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an abstract computation result state that can either be fixed or completable.
 * The class is sealed, only allowing extension by its nested subclasses.
 *
 * @param <R> The type of the computation result.
 */
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public abstract sealed class Result<R> permits Result.Fixed, Result.Completable {
	final Async.Hash hash;
	final long version;

	/**
	 * Retrieves the computation result. If the result is not yet available for a completable result,
	 * it blocks until completion.
	 *
	 * @return the computation result of type {@code R}.
	 */
	public abstract R result();

	/**
	 * Checks whether the computation result has been completed.
	 *
	 * @return {@code true} if the result is complete, {@code false} otherwise.
	 */
	abstract boolean complete();

	/**
	 * Creates a {@link Fixed} result instance with the specified hash and version.
	 *
	 * @param hash    the hash identifying the computation.
	 * @param version the version associated with the computation.
	 * @param <T>     the type of the computation result.
	 * @return a new {@link Fixed} instance.
	 */
	static <T> Fixed<T> fixed(Async.Hash hash, long version) {
		return new Fixed<>(hash, version);
	}

	/**
	 * Creates a {@link Completable} result instance with the specified parameters.
	 *
	 * @param hash     the hash identifying the computation.
	 * @param version  the version associated with the computation.
	 * @param future   the future representing the result of the computation.
	 * @param function the function to compute the result when required.
	 * @param target   the target environment for computation.
	 * @param <T>      the type of the computation result.
	 * @return a new {@link Completable} instance.
	 */
	static <T> Completable<T> completable(Async.Hash hash, long version, CompletableFuture<T> future,
			Supplier<T> function, Async.Target target) {
		return new Completable<>(hash, version, future, function, target);
	}

	static final class Fixed<R> extends Result<R> {
		@Getter(AccessLevel.PACKAGE)
		private final CompletableFuture<R> future;

		public Fixed(Async.Hash hash, long version) {
			super(hash, version);
			this.future = new CompletableFuture<>();
		}

		@Override
		boolean complete() {
			return future.isDone();
		}

		@Override
		public R result() {
			return future.join();
		}
	}

	@Getter
	@Setter(AccessLevel.PACKAGE)
	static final class Completable<R> extends Result<R> {
		private final CompletableFuture<R> future;
		private final Supplier<R> function;
		private final Async.Target target;
		private final AtomicBoolean running = new AtomicBoolean(false);

		public Completable(Async.Hash hash, long version, CompletableFuture<R> future, Supplier<R> function,
				Async.Target target) {
			super(hash, version);
			this.future = future;
			this.function = function;
			this.target = target;
		}

		@Override
		public R result() {
			try {
				if (!complete() && (target == Async.Target.Any || Async.CURRENT_THREAD_TARGET.get() == target)) {
					if (running.compareAndSet(false, true)) {
						future.complete(function.get());
					}
				}
				return future.join();
			} catch (Exception e) {
				future.completeExceptionally(e);
				throw e;
			}
		}

		@Override
		boolean complete() {
			return future.isDone();
		}
	}
}
