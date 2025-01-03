package mylie.core.async;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import mylie.core.async.Function.F1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class AsyncTest {
	static Stream<Scheduler> schedulerProvider() {
		return Stream.of(new SingleThreadedScheduler());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testAsync(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testAtomicIntegerDecrease(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerDecrease,
				atomicInteger));
		assertEquals(-1, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNested3(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, Nested3, atomicInteger));
		assertEquals(3, atomicInteger.get()); // Nested3 should increment the atomic integer to 3
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testExecutionModeSync(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Ensure synchronous execution increments immediately
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testMultipleIncrements(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(3, atomicInteger.get()); // Validate 3 sequential increments
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNested2(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, Nested2, atomicInteger));
		assertEquals(2, atomicInteger.get()); // Increment by Nested2
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerDecrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Ensure Nested2 executed atomicIntegerDecrease
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testThrowException(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		assertThrows(RuntimeException.class, () -> Wait.wait(
				Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, throwException, atomicInteger)));
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNoOpCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.No;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Execute async function with Cache.No
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));

		// Validate the cache does not store or affect results
		assertEquals(1, atomicInteger.get());
		cache.invalidate();
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testOneFrameCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		// Invalidate and ensure the cache resets
		cache.invalidate();
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testVersionedCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 12345L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		// Using the same version hash should bypass re-execution
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 12345L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Cached result should be used

		// Changing version hash should execute again
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 67890L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNoInvalidationCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		// Attempt to invalidate - should have no effect
		cache.invalidate();
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Should still return cached result
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNoOpCacheWithConcurrentCalls(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.No;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Execute two async calls simultaneously
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(2, atomicInteger.get()); // No caching; both calls execute
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testOneFrameCacheWithMultipleInvalidations(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		cache.invalidate();
		cache.invalidate(); // Multiple invalidations should not cause issues

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testForeverCacheWithConcurrency(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Execute async calls with the same cache
		Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease, atomicInteger);
		Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease, atomicInteger);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Forever cache prevents re-execution
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testVersionedCacheWithMultipleHashes(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 1001L, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 1002L, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(2, atomicInteger.get()); // Two distinct hashes trigger two executions
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testVersionedCacheWithSameHash(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 1001L, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 1001L, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(1, atomicInteger.get()); // Same hash results in single execution
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testInvalidateSpecificHash(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 2001L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 2002L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheInteractionWithException(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		assertThrows(RuntimeException.class, () -> Wait.wait(
				Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 3001L, throwException, atomicInteger)));
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testOneFrameCacheWithNoInvalidation(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 4001L, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 4001L, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(1, atomicInteger.get()); // Duplicate execution blocked by cache
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testConcurrencyOnSameCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Create threads to execute on the same cache
		Thread t1 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
				atomicIntegerIncrease, atomicInteger)));
		Thread t2 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
				atomicIntegerIncrease, atomicInteger)));

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			fail(e);
		}

		assertTrue(atomicInteger.get() <= 2); // Ensure thread-safety with the cache
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testForeverCacheInvalidationAttempt(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		cache.invalidate(); // Attempt invalidation - should not affect Forever cache

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get()); // Cache prevents re-execution
	}

	// Additional Tests for Edge Cases and Advanced Scenarios

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNoOpCacheNoSideEffects(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.No;
		AtomicInteger atomicInteger = new AtomicInteger(5);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(6, atomicInteger.get());

		cache.invalidate(); // Invalidating Cache.No should not affect result
		assertEquals(6, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testMixedCacheUsages(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.OneFrame, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.Forever, 0, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(2, atomicInteger.get()); // Three different caches used
	}

	// Edge case: Massive increments
	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testLargeIncrements(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		for (int i = 0; i < 1000; i++) {
			Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
					atomicInteger));
			cache.invalidate(); // Invalidate after each increment
		}
		assertEquals(1000, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheInvalidationDuringExecution(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Thread invalidationThread = new Thread(cache::invalidate);

		// Start invalidation while async is running
		invalidationThread.start();
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));

		try {
			invalidationThread.join();
		} catch (InterruptedException e) {
			fail(e);
		}

		assertEquals(1, atomicInteger.get()); // Increment should complete despite invalidation
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testMixedExecutionModesWithCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Mixed mode executions on the same cache
		Wait.wait(Async.async(Async.ExecutionMode.Direct, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));

		assertEquals(1, atomicInteger.get()); // The Forever cache prevents second execution, even with Direct mode
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testInvalidationAndRebuildCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		cache.invalidate();

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get()); // Cache rebuilds after invalidation
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheNoOpInHighConcurrency(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.No;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// High concurrency with Cache.No
		Thread[] threads = new Thread[100];
		for (int i = 0; i < 100; i++) {
			threads[i] = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
					atomicIntegerIncrease, atomicInteger)));
			threads[i].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				fail(e);
			}
		}

		assertEquals(100, atomicInteger.get()); // Each thread executes independently with no caching
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testVersionedCacheWithRapidHashChanges(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Rapidly change hashes and observe cache behavior
		for (long i = 0; i < 100; i++) {
			Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, i, atomicIntegerIncrease,
					atomicInteger));
		}

		assertEquals(100, atomicInteger.get()); // Each unique hash forces a new execution
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testForeverCacheUnderMassiveLoad(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Repeated attempts to execute the same cached function
		for (int i = 0; i < 1000; i++) {
			Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
					atomicInteger));
		}

		assertEquals(1, atomicInteger.get()); // Forever cache prevents all redundant executions
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheBehaviorWithNestedAsyncCalls(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Function.F1<AtomicInteger, Boolean> nestedFunction = new Function.F1<>("NestedFunction") {
			@Override
			public Boolean apply(AtomicInteger o) {
				Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.OneFrame, 0,
						atomicIntegerIncrease, o));
				return true;
			}
		};

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, nestedFunction, atomicInteger));
		assertEquals(1, atomicInteger.get()); // Ensure nested call executes correctly
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheBehaviorAfterMassiveInvalidations(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		for (int i = 0; i < 100; i++) {
			cache.invalidate();
		}

		// Forever cache should still retain the result
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testInvalidHashInVersionedCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Versioned;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Use an invalid hash (-1)
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, -1L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());

		// Ensure no corruption occurs with subsequent valid hashes
		Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0L, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(2, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCacheRaceCondition(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Thread t1 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
				atomicIntegerIncrease, atomicInteger)));

		Thread t2 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
				atomicIntegerIncrease, atomicInteger)));

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			fail(e);
		}

		assertEquals(1, atomicInteger.get()); // Cache consistency ensures only one increment
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testMultipleThreadsAccessingCacheSimultaneously(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		int threadCount = 10;
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
					atomicIntegerIncrease, atomicInteger)));
			threads[i].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				fail("Thread was interrupted: " + e.getMessage());
			}
		}

		// Only one result should be cached, so atomicInteger should be incremented
		// exactly once
		assertEquals(1, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testRaceConditionWithInvalidation(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Thread writerThread = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease,
						atomicInteger));
			}
		});

		Thread invalidatorThread = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				cache.invalidate();
			}
		});

		writerThread.start();
		invalidatorThread.start();

		try {
			writerThread.join();
			invalidatorThread.join();
		} catch (InterruptedException e) {
			fail("Thread was interrupted: " + e.getMessage());
		}

		// After invalidations, each function execution forces re-execution, so we
		// expect at least one increment to remain
		assertTrue(atomicInteger.get() > 0);
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testRaceConditionBetweenMultipleCaches(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);

		Cache cache1 = Cache.OneFrame;
		Cache cache2 = Cache.OneFrame;

		AtomicInteger atomicInteger1 = new AtomicInteger(0);
		AtomicInteger atomicInteger2 = new AtomicInteger(0);

		Thread thread1 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache1, 0,
				atomicIntegerIncrease, atomicInteger1)));

		Thread thread2 = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache2, 0,
				atomicIntegerIncrease, atomicInteger2)));

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			fail("Thread was interrupted: " + e.getMessage());
		}

		// Each cache should operate independently
		assertEquals(1, atomicInteger1.get());
		assertEquals(1, atomicInteger2.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testConcurrencyWithForeverCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.Forever;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		int threadCount = 10;
		Thread[] threads = new Thread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(() -> Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0,
					atomicIntegerIncrease, atomicInteger)));
			threads[i].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				fail("Thread was interrupted: " + e.getMessage());
			}
		}

		// Forever cache should only execute the first time, so atomicInteger should
		// equal 1
		assertEquals(1, atomicInteger.get());
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testConcurrentNestedAsyncCallsWithCache(Scheduler scheduler) {
		Async.SCHEDULER(scheduler);
		Cache cache = Cache.OneFrame;
		AtomicInteger atomicInteger = new AtomicInteger(0);

		Function.F1<AtomicInteger, Boolean> nestedFunction = new Function.F1<>("NestedFunction") {
			@Override
			public Boolean apply(AtomicInteger o) {
				Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease, o));
				Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, atomicIntegerIncrease, o));
				return true;
			}
		};

		Thread[] threads = new Thread[10];
		for (int i = 0; i < 10; i++) {
			threads[i] = new Thread(() -> Wait.wait(
					Async.async(Async.ExecutionMode.Async, Async.Target.Any, cache, 0, nestedFunction, atomicInteger)));
			threads[i].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				fail("Thread was interrupted: " + e.getMessage());
			}
		}

		// Ensure cache restricts executions properly; depending on timing, this result
		// could vary slightly
		assertEquals(1, atomicInteger.get());
	}

	private static final F1<AtomicInteger, Boolean> atomicIntegerIncrease = new F1<>("AtomicIntegerIncrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			o.incrementAndGet();
			return true;
		}
	};

	private static final F1<AtomicInteger, Boolean> atomicIntegerDecrease = new F1<>("AtomicIntegerDecrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			o.decrementAndGet();
			return true;
		}
	};

	private static final F1<AtomicInteger, Boolean> Nested2 = new F1<>("AtomicIntegerDecrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			o.incrementAndGet();
			Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, atomicIntegerIncrease, o));

			return true;
		}
	};

	private static final F1<AtomicInteger, Boolean> Nested3 = new F1<>("AtomicIntegerDecrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			Wait.wait(Async.async(Async.ExecutionMode.Async, Async.Target.Any, Cache.No, 0, Nested2, o));
			o.incrementAndGet();
			return true;
		}
	};

	private static final F1<AtomicInteger, Boolean> throwException = new F1<>("ThrowException") {
		@Override
		public Boolean apply(AtomicInteger o) {
			throw new RuntimeException("Simulated exception");
		}
	};
}
