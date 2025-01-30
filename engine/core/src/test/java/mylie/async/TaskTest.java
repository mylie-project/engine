package mylie.async;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import mylie.async.task.Task;
import org.junit.jupiter.api.Test;

class TaskTest {

	private static class MockTask extends Task<String> {
		private final String returnValue;

		public MockTask(String returnValue) {
			this.returnValue = returnValue;
		}

		@Override
		protected Result<String> executeTask() {
			Result.Fixed<String> fixed = Result.fixed(null, 0);
			fixed.future().complete(returnValue);
			return fixed;
		}
	}

	private static class MockExecution extends Task<String> {
		private final String id;
		private final List<String> executionOrder;

		private MockExecution(String id, List<String> executionOrder) {
			this.id = id;
			this.executionOrder = executionOrder;
		}

		@Override
		protected Result<String> executeTask() {
			executionOrder.add(id);
			Result.Fixed<String> fixed = Result.fixed(null, 0);
			fixed.future().complete("Success");
			return fixed;
		}
	}

	@Test
	void testExecuteWithoutDependencies() {
		MockTask task = new MockTask("Success");
		Result<String> result = task.execute();
		assertEquals("Success", result.result());
	}

	@Test
	void testExecuteWithSingleDependency() {
		MockTask dependency = new MockTask("Dependency Success");
		MockTask task = new MockTask("Task Success");

		AtomicBoolean dependencyExecuted = new AtomicBoolean(false);

		task.addDependency(() -> {
			dependencyExecuted.set(true);
			return dependency.execute();
		});

		Result<String> result = task.execute();

		assertTrue(dependencyExecuted.get());
		assertEquals("Task Success", result.result());
	}

	@Test
	void testExecuteWithMultipleDependencies() {
		MockTask firstDependency = new MockTask("First Dependency");
		MockTask secondDependency = new MockTask("Second Dependency");
		MockTask task = new MockTask("Main Task");

		AtomicBoolean firstDependencyExecuted = new AtomicBoolean(false);
		AtomicBoolean secondDependencyExecuted = new AtomicBoolean(false);

		task.addDependency(() -> {
			firstDependencyExecuted.set(true);
			return firstDependency.execute();
		});

		task.addDependency(() -> {
			secondDependencyExecuted.set(true);
			return secondDependency.execute();
		});

		Result<String> result = task.execute();

		assertTrue(firstDependencyExecuted.get());
		assertTrue(secondDependencyExecuted.get());
		assertEquals("Main Task", result.result());
	}

	@Test
	void testComplexDependencyExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		MockExecution taskA = new MockExecution("A", executionOrder);
		MockExecution taskB = new MockExecution("B", executionOrder);
		MockExecution taskC = new MockExecution("C", executionOrder);
		MockExecution taskD = new MockExecution("D", executionOrder);

		// Create a complex dependency graph
		taskD.addDependency(taskB);
		taskD.addDependency(taskC);
		taskB.addDependency(taskA);
		taskC.addDependency(taskA);

		taskD.execute();

		assertEquals(List.of("A", "B", "A", "C", "D"), executionOrder);
	}

	@Test
	void testAddAndRemoveDependency() {
		MockTask dependency = new MockTask("Dependency Success");
		MockTask task = new MockTask("Task Success");

		AtomicBoolean dependencyExecuted = new AtomicBoolean(false);
		Supplier<Result<?>> dependencySupplier = () -> {
			dependencyExecuted.set(true);
			return dependency.execute();
		};

		task.addDependency(dependencySupplier);
		task.removeDependency(dependencySupplier);

		Result<String> result = task.execute();

		assertFalse(dependencyExecuted.get());
		assertEquals("Task Success", result.result());
	}

	@Test
	void testExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		MockExecution taskA = new MockExecution("A", executionOrder);
		MockExecution taskB = new MockExecution("B", executionOrder);
		MockExecution taskC = new MockExecution("C", executionOrder);

		taskC.addDependency(taskB);
		taskB.addDependency(taskA);

		taskC.execute();

		assertEquals(List.of("A", "B", "C"), executionOrder);
	}

}
