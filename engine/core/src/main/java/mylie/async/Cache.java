package mylie.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a hierarchical cache system for storing and retrieving data.
 * Subclasses define different caching strategies, such as invalidating old entries or
 * keeping entries forever.
 */
@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
	/**
	 * Unique identifier for the cache, used for distinguishing different caches.
	 */
	final String id;
	/**
	 * Parent cache in the hierarchy, used as a fallback for retrieving results
	 * if not found in the current cache.
	 */
	@Setter(AccessLevel.PACKAGE)
	Cache parent;

	/**
	 * Constructs a new Cache instance with the specified identifier.
	 *
	 * @param id the unique identifier for the cache
	 */
	protected Cache(String id) {
		this.id = id;
	}

	/**
	 * Retrieves the cached result associated with the given hash and version.
	 *
	 * @param hash    the unique hash identifying the data
	 * @param version the version of the data
	 * @param <R>     the type of the result
	 * @return the cached result, or {@code null} if not available
	 */
	abstract <R> Result<R> result(Async.Hash hash, long version);

	/**
	 * Stores the given result in the cache.
	 *
	 * @param result the result to store
	 * @param <R>    the type of the result
	 */
	abstract <R> void result(Result<R> result);

	/**
	 * Invalidates the entire cache, removing all stored entries.
	 */
	abstract void invalidate();

	/**
	 * Removes the cached entry associated with the given hash.
	 *
	 * @param hash the unique hash to identify the entry to remove
	 */
	abstract void remove(Async.Hash hash);

	/**
	 * Resets the cache by clearing all entries and reinitializing it.
	 */
	public abstract void reset();

	/**
	 * A no-operation implementation of the cache, which does not store any data
	 * and always returns {@code null} for retrieval operations.
	 */
	public static class NoOpCache extends Cache {
		/**
		 * Constructs a new NoOpCache instance with the specified identifier.
		 * A NoOpCache is a no-operation implementation of the cache that does
		 * not store any data and always returns {@code null} for retrieval operations.
		 *
		 * @param id the unique identifier for the cache
		 */
		public NoOpCache(String id) {
			super(id);
		}

		@Override
		<R> Result<R> result(Async.Hash hash, long version) {
			return null;
		}

		@Override
		<R> void result(Result<R> result) {
			// NoOp intentional
		}

		@Override
		void invalidate() {
			// NoOp intentional
		}

		@Override
		void remove(Async.Hash hash) {
			// NoOp intentional
		}

		@Override
		public void reset() {
			// NoOp intentional
		}
	}

	/**
	 * Caching strategy that invalidates all stored entries and clears the cache completely.
	 */
	static final class InvalidateAll extends NoOpCache {
		private final Map<Async.Hash, Result<?>> store = new ConcurrentHashMap<>();

		public InvalidateAll(String id) {
			super(id);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected <R> Result<R> result(Async.Hash hash, long version) {

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
		public void reset() {
			invalidate();
		}
	}

	/**
	 * Caching strategy that invalidates entries older than a specified version.
	 */
	static final class InvalidateOlder extends NoOpCache {

		public InvalidateOlder(String id) {
			super(id);
		}

		@Override
		protected <R> Result<R> result(Async.Hash hash, long version) {
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
	}

	/**
	 * Caching strategy that invalidates entries whose versions differ from the specified version.
	 */
	static final class InvalidateDifferent extends NoOpCache {
		public InvalidateDifferent(String id) {
			super(id);
		}

		@Override
		protected <R> Result<R> result(Async.Hash hash, long version) {
			Result<R> result = parent().result(hash, version);
			if (result != null && result.version() != version) {
				parent().remove(hash);
				return null;
			}
			return result;
		}

		@Override
		<R> void result(Result<R> result) {
			parent().result(result);
		}
	}

	/**
	 * Caching strategy that retains all cached entries indefinitely without invalidation.
	 */
	static final class NoInvalidation extends NoOpCache {
		public NoInvalidation(String id) {
			super(id);
		}

		@Override
		protected <R> Result<R> result(Async.Hash hash, long version) {
			return parent().result(hash, version);
		}

		@Override
		<R> void result(Result<R> result) {
			parent().result(result);
		}
	}

}
