package mylie.core.components;

import java.util.HashMap;
import java.util.Map;
import mylie.core.component.Components;

public final class Vault implements Components.AppComponent {
	private final Map<Item<?>, Object> values = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T value(Item<T> item) {
		return (T) values.get(item);
	}

	public <T> void value(Item<T> item, T value) {
		values.put(item, value);
	}

	@SuppressWarnings("unused")
	public static class Item<T> {

	}
}
