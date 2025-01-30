package mylie.async.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import mylie.async.Result;

/**
 * Represents an abstract task capable of executing a defined operation and managing dependencies on other tasks.
 *
 * @param <R> The type of result produced by this task after execution.
 */
public abstract class Task<R> {
	private final List<Supplier<Result<?>>> dependencies;

	/**
	 * Constructs a new instance of the Task class, initializing the list of dependencies.
	 * The dependencies are managed using a thread-safe, copy-on-write list to facilitate
	 * concurrent modifications without compromising data consistency.
	 */
	protected Task() {
		dependencies = new CopyOnWriteArrayList<>();
	}

	/**
	 * Executes the task by first resolving and processing all dependencies, then executing the actual task logic.
	 *
	 * @return The result of the task of type {@code R}.
	 */
	public Result<R> execute() {
		List<Result<?>> results = new ArrayList<>(dependencies.size());
		for (Supplier<Result<?>> dependency : dependencies) {
			results.add(dependency.get());
		}
		for (Result<?> result : results) {
			result.result();
		}
		return this.executeTask();
	}

	/**
	 * Adds a dependency to the task, which will be resolved and executed before this task.
	 *
	 * @param dependency A supplier of a {@link Result} representing a task dependency.
	 */
	public void addDependency(Supplier<Result<?>> dependency) {
		dependencies.add(dependency);
	}

	/**
	 * Adds another task as a dependency to this task, ensuring it is executed prior to this task.
	 *
	 * @param dependency A {@link Task} to be added as a dependency.
	 */
	public void addDependency(Task<?> dependency) {
		addDependency(dependency::execute);
	}

	/**
	 * Removes a previously added dependency from the task.
	 *
	 * @param dependency A supplier of a {@link Result} representing the dependency to be removed.
	 */
	public void removeDependency(Supplier<Result<?>> dependency) {
		dependencies.remove(dependency);
	}

	/**
	 * Removes a previously added task dependency from this task.
	 *
	 * @param dependency A {@link Task} to be removed from the dependencies list.
	 */
	public void removeDependency(Task<?> dependency) {
		removeDependency(dependency::execute);
	}

	/**
	 * Represents the core logic of the task to be implemented by a subclass.
	 * This method handles the actual execution of task-specific operations.
	 *
	 * @return The result of the task execution of type {@code R}.
	 */
	protected abstract Result<R> executeTask();
}
