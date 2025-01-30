package mylie.async;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import mylie.async.Function.F1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class AsyncTest {
	static Stream<Scheduler> schedulerProvider() {
		return Stream.of(Schedulers.singleThreaded(), Schedulers.virtualThreads(), Schedulers.forkJoin(),
				Schedulers.threadPool(1), Schedulers.workStealing(1));
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testDifferentCaches(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);

		// Test No Cache
		List<Result<Boolean>> resultsNoCache = new ArrayList<>();
		resultsNoCache.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerIncrease,
				atomicInteger));
		resultsNoCache.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(resultsNoCache);
		assertEquals(2, atomicInteger.get(), "Caches.No should not cache results, both tasks should execute.");

		// Reset atomic integer for next test
		atomicInteger.set(0);

		// Test One Frame Cache
		List<Result<Boolean>> resultsOneFrame = new ArrayList<>();
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		Wait.wait(resultsOneFrame);
		assertEquals(1, atomicInteger.get(),
				"Caches.OneFrame should cache results, only one unique task should execute.");

		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testOneFrameCache(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		// Test One Frame Cache
		List<Result<Boolean>> resultsOneFrame = new ArrayList<>();
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		Wait.wait(resultsOneFrame);
		assertEquals(1, atomicInteger.get(),
				"Caches.OneFrame should cache results, only one unique task should execute.");
		Caches.OneFrame.invalidate();
		atomicInteger.set(0);
		resultsOneFrame = new ArrayList<>();
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		Caches.OneFrame.invalidate();
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		Wait.wait(resultsOneFrame);
		assertEquals(2, atomicInteger.get());
		Caches.OneFrame.invalidate();
		atomicInteger.set(0);
		resultsOneFrame = new ArrayList<>();
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0,
				atomicIntegerIncrease, atomicInteger));
		resultsOneFrame.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 1,
				atomicIntegerIncrease, atomicInteger));
		Wait.wait(resultsOneFrame);
		assertEquals(1, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void simpleAsync(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerIncrease,
				atomicInteger));
		assertEquals(1, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testComplete(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Result<Boolean> async = Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0,
				atomicIntegerIncrease, atomicInteger);
		if (!async.complete()) {
			Wait.wait(async);
		}
		assertEquals(1, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testCache(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		List<Result<Boolean>> results = new ArrayList<>();
		results.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0, atomicIntegerIncrease,
				atomicInteger));
		results.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.OneFrame, 0, atomicIntegerIncrease,
				atomicInteger));
		Wait.wait(results);
		assertEquals(1, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testNestedAsync(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, Nested3, atomicInteger));
		assertEquals(3, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testResult(Scheduler scheduler) {
		Async.initialize(scheduler);
		Result<Integer> async = Async.async(Async.ExecutionMode.DIRECT, Async.Target.Any, Caches.No, 0, add, 1, 2);
		assertEquals(3, Wait.wait(async));

		async = Async.async(Async.ExecutionMode.DIRECT, Async.Target.Any, Caches.No, 0, subtract, 2, 2);
		assertEquals(0, Wait.wait(async));
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testAtomicIntegerDecrease(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Wait.wait(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerDecrease,
				atomicInteger));
		assertEquals(-1, atomicInteger.get());
		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testThrowException(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Result<Boolean> async = Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, throwException,
				atomicInteger);
		assertThrows(RuntimeException.class, () -> Wait.wait(async));

		Async.shutdown();
	}

	@ParameterizedTest
	@MethodSource("schedulerProvider")
	void testSequentialAsync(Scheduler scheduler) {
		Async.initialize(scheduler);
		AtomicInteger atomicInteger = new AtomicInteger(0);
		List<Result<Boolean>> results = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			results.add(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerIncrease,
					atomicInteger));
		}
		Wait.wait(results);
		assertEquals(5, atomicInteger.get());
		Async.shutdown();
	}

	private static final Function.F1<AtomicInteger, Boolean> atomicIntegerIncrease = new Function.F1<>(
			"AtomicIntegerIncrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			o.incrementAndGet();
			return true;
		}
	};

	private static final Function.F1<AtomicInteger, Boolean> atomicIntegerDecrease = new F1<>("AtomicIntegerDecrease") {
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
			Wait.wait(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, atomicIntegerIncrease, o));

			return true;
		}
	};

	private static final F1<AtomicInteger, Boolean> Nested3 = new F1<>("AtomicIntegerDecrease") {
		@Override
		public Boolean apply(AtomicInteger o) {
			Wait.wait(Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, 0, Nested2, o));
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

	private static final Function.F2<Integer, Integer, Integer> add = new Function.F2<>("Add") {
		@Override
		public Integer apply(Integer a, Integer b) {
			return a + b;
		}
	};

	private static final Function.F2<Integer, Integer, Integer> subtract = new Function.F2<>("Subtract") {
		@Override
		public Integer apply(Integer a, Integer b) {
			return a - b;
		}
	};
}
