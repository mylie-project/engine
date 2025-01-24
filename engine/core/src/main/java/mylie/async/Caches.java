package mylie.async;

/**
 * The {@code Caches} class provides a collection of commonly used caching strategies.
 * These strategies define how cached data is managed, invalidated, or retained.
 */
public class Caches {
    private Caches() {}

    /**
     * Represents a no-operation caching strategy where no data is cached.
     */
    public static final Cache No = new Cache.NoOpCache("NoOp");

    /**
     * Represents a caching strategy that invalidates all data after one frame or cycle.
     */
    public static final Cache OneFrame = new Cache.InvalidateAll("OneFrame");

    /**
     * Represents a caching strategy that invalidates data older than a specific version.
     */
    public static final Cache InvalidateOlder = new Cache.InvalidateOlder("Versioned");

    /**
     * Represents a caching strategy where data is cached indefinitely.
     */
    public static final Cache Forever = new Cache.NoInvalidation("Forever");

    /**
     * Represents a caching strategy that invalidates cache entries when their version differ.
     */
    public static final Cache InvalidateDifferent = new Cache.InvalidateDifferent("InvalidateDifferent");
}
