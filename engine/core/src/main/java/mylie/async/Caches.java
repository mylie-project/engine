package mylie.async;

public class Caches {
	private Caches() {
	}
	/// A statically defined instance of `Cache` that represents a no-operation
	/// cache implementation.
	/// This instance is of type `NoOpCache`, which performs no actual caching.
	/// All methods invoked on this instance, such as retrieving, storing, or
	/// invalidating cache entries,
	/// will have no effect and return default values (e.g., `null` for retrieval
	/// operations).
	/// Usage of this instance is suitable in scenarios where caching is
	/// intentionally disabled
	/// or not required, ensuring minimal side effects while maintaining
	/// compatibility with
	/// the caching interface.
	public static final Cache No = new Cache.NoOpCache("NoOp");
	/// A pre-defined static cache instance that encapsulates the behavior
	/// of an [Cache.InvalidateAll] cache implementation for managing and
	/// invalidating cached results associated with a single frame of execution.
	/// The primary purpose of this cache is to store results temporarily for
	/// operations or calculations tied to a singular "frame" or execution context.
	/// This cache is designed to be invalidated and cleared completely, ensuring
	/// that its contents do not persist beyond the scope of the frame.
	/// Key characteristics of the `OneFrame` cache:
	/// - Ensures complete invalidation of cache entries when necessary.
	/// - Potentially synchronizes with a parent cache for added flexibility.
	/// - Suitable for environments where cached results are valid only within
	/// a dedicated execution frame or context and must be reset afterward.
	public static final Cache OneFrame = new Cache.InvalidateAll("OneFrame");
	/// A static, version-based invalidation cache used for managing the lifecycle
	/// of cached results.
	/// The `Versioned` cache instance uses [Cache.InvalidateOlder] as its
	/// implementation,
	/// ensuring
	/// that only the most up-to-date results are retained within the cache. Any
	/// cached entry with a version
	/// older than the requested version is automatically invalidated and removed
	/// from the cache.
	/// This cache is typically used when maintaining consistency between different
	/// versions of results is
	/// critical, such as in scenarios where asynchronous computations or frequent
	/// updates occur.
	public static final Cache InvalidateOlder = new Cache.InvalidateOlder("Versioned");
	/// A predefined, static instance of the [Cache] class that represents a cache
	/// with entries that persist indefinitely. The cache is not subject to
	/// invalidation or expiration.
	/// The `Forever` cache ensures that all entries remain valid for the lifetime
	/// of the application
	/// unless manually removed. This makes it suitable for storing long-lived or
	/// permanent data in scenarios
	/// where invalidation is not required.
	/// Internally, this instance is implemented using the [Cache.NoInvalidation]
	/// class,
	/// which delegates
	/// caching operations to its parent and does not override any stored values or
	/// actively invalidate entries.
	public static final Cache Forever = new Cache.NoInvalidation("Forever");
	/// Represents a specialized cache policy designed to invalidate cached entries
	/// that have a different version than the specified or intended version.
	/// The `InvalidateDifferent` implementation ensures consistency by removing
	/// outdated or version-mismatched entries upon access or during evaluation.
	/// This cache policy is useful in scenarios where versioning plays a critical
	/// role in determining the validity of cached data and where maintaining
	/// version-specific consistency is essential. It prevents the use of incorrect
	/// or outdated cached results while allowing version-compliant entries to
	/// remain valid.
	public static final Cache InvalidateDifferent = new Cache.InvalidateDifferent("InvalidateDifferent");
}
