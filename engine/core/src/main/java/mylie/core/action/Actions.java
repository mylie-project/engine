package mylie.core.action;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Actions {

	public static <T> Action<T> call(ActionGroup group, Function<T, Boolean> condition, Runnable action) {
		return (value) -> {
			if (group.enabled() && condition.apply(value))
				action.run();
		};
	}

	public static <T> Action<T> call(ActionGroup group, Function<T, Boolean> condition, Consumer<T> action) {
		return (value) -> {
			if (group.enabled() && condition.apply(value))
				action.accept(value);
		};
	}

	public static <T, P> Action<T> call(ActionGroup group, Function<T, Boolean> condition, Consumer<P> paramAction,
			P param) {
		return (value) -> {
			if (group.enabled() && condition.apply(value))
				paramAction.accept(param);
		};
	}

	public static <T, P> Action<T> call(ActionGroup group, Function<T, Boolean> condition, BiConsumer<T, P> paramAction,
			P param) {
		return (value) -> {
			if (group.enabled() && condition.apply(value))
				paramAction.accept(value, param);
		};
	}
}
