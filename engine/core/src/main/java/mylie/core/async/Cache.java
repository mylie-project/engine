package mylie.core.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
	public static final Cache No = new NoOpCache();
	public static final Cache OneFrame = new InvalidateAll("OneFrame");
	public static final Cache Versioned = new InvalidateHash("Versioned");
	public static final Cache Forever = new NoInvalidation("Forever");

	final String id;
	@Setter(AccessLevel.PACKAGE)
	Cache parent;

	abstract <R> Result<R> result(int hash, long version);

	abstract <R> void result(Result<R> result);

	abstract void invalidate();

	abstract void remove(int hash);

	private static final class NoOpCache extends Cache {
		NoOpCache() {
			super("NoOp", null);
		}

		@Override
		<R> Result<R> result(int hash, long version) {
			return null;
		}

		@Override
		<R> void result(Result<R> result) {
		}

		@Override
		void invalidate() {
		}

		@Override
		void remove(int hash) {

		}
	}

	private static final class InvalidateAll extends Cache {
		private final Map<Integer, Result<?>> store = new ConcurrentHashMap<>();
		public InvalidateAll(String id) {
			super(id, null);
		}

		@SuppressWarnings("unchecked")
		@Override
		<R> Result<R> result(int hash, long version) {
			Result<R> result = (Result<R>) store.get(hash);
			if (result == null) {
				result = parent().result(hash, version);
			}
			return result;
		}

		@Override
		<R> void result(Result<R> result) {
			store.put(result.hash(), result);
			parent().result(result);
		}

		@Override
		void invalidate() {
			store.keySet().forEach(parent::remove);
			store.clear();
		}

		@Override
		void remove(int hash) {

		}
	}

	private static final class InvalidateHash extends Cache {

		public InvalidateHash(String id) {
			super(id, null);
		}

		@Override
		<R> Result<R> result(int hash, long version) {
			Result<R> result = parent().result(hash, version);
			if (result != null && result.version() < version) {
				parent().remove(hash);
				return null;
			}
			return result;
		}

		@Override
		<R> void result(Result<R> result) {
			parent().result(result);
		}

		@Override
		void invalidate() {

		}

		@Override
		void remove(int hash) {

		}
	}

	private static final class NoInvalidation extends Cache {
		public NoInvalidation(String id) {
			super(id, null);
		}

		@Override
		<R> Result<R> result(int hash, long version) {
			return parent().result(hash, version);
		}

		@Override
		<R> void result(Result<R> result) {
			parent().result(result);
		}

		@Override
		void invalidate() {

		}

		@Override
		void remove(int hash) {

		}
	}

}
