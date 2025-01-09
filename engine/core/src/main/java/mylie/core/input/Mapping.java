package mylie.core.input;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.action.Action;
import mylie.util.Converter;
import mylie.util.filter.Filter;

@Getter(AccessLevel.PACKAGE)
public class Mapping<T, D extends InputDevice<D>, I> {
	private final Action<T> action;
	private final Filter<D> deviceFilter;
	private final Filter<InputType.Type<I, D>> typeFilter;
	private final Converter<I, T> converter;
	protected Mapping(Action<T> action, Filter<D> deviceFilter, Filter<InputType.Type<I, D>> typeFilter,
			Converter<I, T> converter) {
		this.action = action;
		this.deviceFilter = deviceFilter;
		this.typeFilter = typeFilter;
		this.converter = converter;
	}
	@SuppressWarnings("unchecked")
	protected void check(Input.Event<D, InputType.Type<I, D>, I> event) {
		if (deviceFilter.apply(event.device())) {
			if (typeFilter.apply(event.type())) {
				action.execute(converter.convert(event.value()));
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Mapping<?, ?, ?> mapping = (Mapping<?, ?, ?>) o;
		return Objects.equals(action, mapping.action);
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, deviceFilter, typeFilter);
	}
}
