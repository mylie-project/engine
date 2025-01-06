package mylie.util.configuration;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Configuration<T> {
	private final Class<T> configClass;
	private final Map<Option<?, ?>, Object> store = new HashMap<>();

	public Configuration(Class<T> configClass) {
		this.configClass = configClass;
	}

	public <V> void option(Changeable<T, V> option, V value) {
		store.put(option, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V option(Observable<T, V> option) {
		V value = (V) store.get(option);
		if(value==null){
			value=option.defaultValue();
		}
		return value;
	}

	@Getter(AccessLevel.PACKAGE)
	@AllArgsConstructor
	static class Option<T, V> {
		private final V defaultValue;
	}
}
