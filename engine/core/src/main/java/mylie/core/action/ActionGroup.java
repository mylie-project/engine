package mylie.core.action;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class ActionGroup {
	public static final ActionGroup GLOBAL_INPUT = new ActionGroup("ROOT", null);
	final String name;
	final ActionGroup parent;
	@Setter
	boolean enabled;

	public ActionGroup(String name, ActionGroup parent) {
		this.name = name;
		this.parent = parent;

	}

	boolean enabled() {
		return enabled && (parent == null || parent.enabled());
	}
}
