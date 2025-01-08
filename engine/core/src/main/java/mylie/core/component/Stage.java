package mylie.core.component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.async.Async;
import mylie.core.async.Caches;
import mylie.core.async.Function;
import mylie.core.async.Result;
import mylie.util.Void;

@Getter(AccessLevel.PACKAGE)
public class Stage {
	private final String name;
	private final List<Supplier<Result<Void>>> dependencies = new CopyOnWriteArrayList<>();

	@SafeVarargs
	public Stage(String name, Supplier<Result<Void>>... dependencies) {
		this.name = name;
		Collections.addAll(this.dependencies, dependencies);
	}

	public Stage updateDependency(Supplier<Result<Void>> dependency) {
		dependencies.add(dependency);
		return this;
	}

	public void removeDependency(Supplier<Result<Void>> dependency) {
		dependencies.remove(dependency);
	}

	public Result<Void> execute() {
		return Async.async(Async.ExecutionMode.DIRECT, Async.Target.Any, Caches.OneFrame, -1, ExecuteStage, this);
	}

	private static final Function.F1<Stage, Void> ExecuteStage = new Function.F1<>("ExecuteStage") {
		@Override
		protected Void apply(Stage stage) {
			List<Result<?>> results = new LinkedList<>();
			for (Supplier<Result<Void>> dependency : stage.dependencies) {
				results.add(dependency.get());
			}
			for (Result<?> result : results) {
				result.result();
			}
			return Void.VOID;
		}
	};

	@Override
	public String toString() {
		return String.format("Stage %s", name);
	}
}
