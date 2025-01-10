package mylie.core.action;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class ObservableAction<T> extends Actions.BaseAction<T> {
	private T value;
	public ObservableAction(ActionGroup group) {
		super(group);
	}

	@Override
	public void execute(T value) {
		if (group().enabled()) {
			this.value = value;
		}
	}
}
