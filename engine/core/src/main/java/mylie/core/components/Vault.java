package mylie.core.components;

import java.util.HashMap;
import java.util.Map;
import mylie.core.component.Components;

public final class Vault implements Components.AppComponent {
	private final Map<Class<?>, Object> values = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T value(Class<T> item) {
		return (T) values.get(item);
	}

	public <T> void value(Class<T> item, T value) {
		values.put(item, value);
	}
}
