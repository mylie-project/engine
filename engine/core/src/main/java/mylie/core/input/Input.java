package mylie.core.input;

import java.util.Collection;
import mylie.core.async.Result;

public class Input {
	public record Event<D extends InputDevice<D>, T extends InputType.Type<V, D>, V>(Class<? extends Provider> provider,
			D device, T type, V value) {
	}

	public interface Provider {
		Result<Collection<Event<?, ?, ?>>> events();
	}
}
