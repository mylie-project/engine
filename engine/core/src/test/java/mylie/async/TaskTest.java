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
		private Runnable onExecute;
		public MockTask(String returnValue,Runnable onExecute) {
			this.returnValue = returnValue;
			this.onExecute = onExecute;
		}

		@Override
		protected Result<String> executeTask() {
			Result.Fixed<String> fixed = Result.fixed(null, 0);
			if(onExecute != null){
				onExecute.run();
			}
			fixed.future().complete(returnValue);
			return fixed;
		}
	}


	@Test
	void testExecuteWithoutDependencies() {
		MockTask task = new MockTask("Success", () -> {});
		Result<String> result = task.execute();
		assertEquals("Success", result.result());
	}

	@Test
	void testExecuteWithSingleDependency() {

		MockTask task = new MockTask("Task Success",null);

		AtomicBoolean dependencyExecuted = new AtomicBoolean(false);
		MockTask dependency = new MockTask("Dependency Success",() -> dependencyExecuted.set(true));
		task.addDependency(dependency);

		Result<String> result = task.execute();

		assertTrue(dependencyExecuted.get());
		assertEquals("Task Success", result.result());
	}

	@Test
	void testExecuteWithMultipleDependencies() {

		MockTask task = new MockTask("Main Task",null);

		AtomicBoolean firstDependencyExecuted = new AtomicBoolean(false);
		AtomicBoolean secondDependencyExecuted = new AtomicBoolean(false);
		MockTask firstDependency = new MockTask("First Dependency",() -> firstDependencyExecuted.set(true));
		MockTask secondDependency = new MockTask("Second Dependency",() -> secondDependencyExecuted.set(true));
		task.addDependency(firstDependency);

		task.addDependency(secondDependency);

		Result<String> result = task.execute();

		assertTrue(firstDependencyExecuted.get());
		assertTrue(secondDependencyExecuted.get());
		assertEquals("Main Task", result.result());
	}

	@Test
	void testComplexDependencyExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		MockTask taskA = new MockTask("A", ()->executionOrder.add("A"));
		MockTask taskB = new MockTask("B", ()->executionOrder.add("B"));
		MockTask taskC = new MockTask("C", ()->executionOrder.add("C"));
		MockTask taskD = new MockTask("D", ()->executionOrder.add("D"));

		// Create a complex dependency graph
		taskD.addDependency(taskB);
		taskD.addDependency(taskC);
		taskB.addDependency(taskA);
		taskC.addDependency(taskA);

		taskD.execute();

		assertEquals(List.of("A", "B", "A", "C", "D"), executionOrder);
	}

	@Test
	void testExecutionOrder() {
		List<String> executionOrder = new ArrayList<>();
		MockTask taskA = new MockTask("A", ()->executionOrder.add("A"));
		MockTask taskB = new MockTask("B", ()->executionOrder.add("B"));
		MockTask taskC = new MockTask("C", ()->executionOrder.add("C"));

		taskC.addDependency(taskB);
		taskB.addDependency(taskA);

		taskC.execute();

		assertEquals(List.of("A", "B", "C"), executionOrder);
		taskB.removeDependency(taskA);
		executionOrder.clear();
		taskC.execute();
		assertEquals(List.of("B","C"), executionOrder);
	}

}
