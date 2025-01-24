package mylie.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/// Abstract base class for caching mechanisms that support various invalidation
/// strategies and hierarchical parent cache relationships. Each cache instance
/// is identified by a unique ID and optionally linked to a parent cache.
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
	final String id;
	@Setter(AccessLevel.PACKAGE)
	Cache parent;

	/// Retrieves a `Result` instance associated with a given hash and version.
	///
	/// @param hash the unique identifier for the cached result
	/// @param version the version used to validate the freshness of the result
	/// @return a `Result` instance if present; otherwise null if no matching or
	/// valid result is found
	abstract <R> Result<R> result(Async.Hash hash, long version);

	/// Associates the specified `Result` object with the cache.
	///
	/// @param result the `Result` instance to be stored in the cache
	/// containing a unique identifier (hash) and version.
	/// The `Result` object can either represent a completed
	/// or a pending computation.
	abstract <R> void result(Result<R> result);

	/// Invalidates the entire cache, effectively marking all entries as outdated.
	/// After this operation, no cached results remain valid, and future requests
	/// will treat previous results as non-existent or in need of recomputation.
	/// This method is typically used to enforce the complete reset of cache
	/// contents, either due to external conditions, critical updates, or general
	/// maintenance.
	abstract void invalidate();

	/// Removes a cache entry identified by the specified hash.
	///
	/// @param hash the unique identifier of the cache entry to be removed
	abstract void remove(Async.Hash hash);

	public abstract void reset();

	/// `NoOpCache` is a concrete implementation of the abstract `Cache` class
	/// that effectively performs no caching operations. Instances of `NoOpCache`
	/// are stateless and serve as a placeholder or a no-operation cache, where all
	/// operations such as storing, retrieving, invalidating, or removing cache
	/// entries
	/// have no effect.
	/// This class can be used in scenarios where caching functionality is optional
	/// or
	/// temporarily disabled without altering the rest of the system's behavior.
	public static class NoOpCache extends Cache {
		public NoOpCache(String id) {
			super(id, null);
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

	/// An implementation of the `Cache` abstract class designed to invalidate
	/// and clear all cached results on demand. This implementation creates a local
	/// `store` to manage cached `Result` objects and also synchronizes
	/// updates with a parent cache if one is defined.
	/// This class is suitable for use cases where complete invalidation of cached
	/// entries is required to maintain data consistency or enforce a fresh state.
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

	/// The `InvalidateHash` class is a concrete implementation of the `Cache`
	/// abstract class
	/// that defines caching behavior based on version validation. Entries in the
	/// cache are invalidated
	/// if their version is older than the requested version.
	/// When a `result(int hash, long version)` method call identifies an existing
	/// cache entry with
	/// an outdated version as compared to the provided version, the entry is
	/// removed from the cache, and
	/// a `null` value is returned instead of the outdated result. This ensures that
	/// only results
	/// with the latest version are retained and served by the cache.
	/// This implementation delegates most operations to its parent cache and does
	/// not manage its own
	/// storage. It relies on the parent for storing and manipulating data while
	/// providing additional
	/// version-based invalidation logic.
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

	/// A specialized implementation of the Cache class that introduces
	/// functionality
	/// to invalidate cache entries that have different versions than the specified
	/// one.
	/// This class ensures consistency by removing entries when a version mismatch
	/// is detected.
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

	/// The `NoInvalidation` class is a specific implementation of the `Cache`
	/// abstract class
	/// that maintains a cache without any form of invalidation. Entries in the
	/// cache remain indefinitely
	/// and are neither explicitly invalidated nor removed. All operations are
	/// delegated to the parent cache.
	/// This class is suitable for use cases where cached entries are intended to
	/// persist indefinitely
	/// without the need for expiration or explicit invalidation.
	/// The actual caching and retrieval of entries are handled by the parent cache,
	/// if defined.
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
