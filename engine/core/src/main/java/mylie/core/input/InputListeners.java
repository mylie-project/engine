package mylie.core.input;

public class InputListeners {
	public interface Raw {
		void onInput(Input.Event<?, ?, ?> event);
	}
}
