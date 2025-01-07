package mylie.util.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mylie.util.Versioned;

public class Configuration<T> {
	private final Class<T> configClass;
	private final Map<Option<?, ?>, Versioned<?>> store = new HashMap<>();

	public Configuration(Class<T> configClass) {
		this.configClass = configClass;
	}

	@SuppressWarnings("unchecked")
	public <V> void option(Changeable<T, V> option, V value) {
		Versioned<V> versioned = getVersioned(option);
		versioned.value(value);
	}

	protected <V> void option(Observable<T, V> option, V value) {
		getVersioned(option).value(value);
	}

	@SuppressWarnings("unchecked")
	private <V> Versioned<V> getVersioned(Observable<T, V> option) {
		store.computeIfAbsent(option, _ -> new Versioned<Object>(option.defaultValue()));
		return (Versioned<V>) store.get(option);
	}

	public <V> V option(Observable<T, V> option) {
		Versioned<V> value = getVersioned(option);
		return value.value();
	}

	public <V> Versioned.Reference<V> reference(Observable<T, V> option) {
		return getVersioned(option).reference();
	}

	@Getter(AccessLevel.PACKAGE)
	@AllArgsConstructor
	static class Option<T, V> {
		private final V defaultValue;
	}
}
