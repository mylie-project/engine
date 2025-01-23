package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public abstract sealed class Result<R> permits Result.Fixed, Result.Completable {
	final Async.Hash hash;
	final long version;
	public abstract R result();
	abstract boolean complete();

	static <T> Fixed<T> fixed(Async.Hash hash, long version) {
		return new Fixed<>(hash, version);
	}

	static <T> Completable<T> completable(Async.Hash hash, long version, CompletableFuture<T> future,
			Supplier<T> function, Async.Target target) {
		return new Completable<>(hash, version, future, function, target);
	}

	@Getter
	static final class Fixed<R> extends Result<R> {
		@Setter(AccessLevel.PACKAGE)
		private R result;

		public Fixed(Async.Hash hash, long version) {
			super(hash, version);
		}

		@Override
		boolean complete() {
			return result != null;
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
			}
			return null;
		}

		@Override
		boolean complete() {
			return future.isDone();
		}
	}
}
