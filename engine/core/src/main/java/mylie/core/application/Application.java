package mylie.core.application;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.component.ComponentManager;
import mylie.core.component.Components;
import mylie.core.components.time.Timer;

@Slf4j
public abstract class Application {
	public static final Async.Target TARGET = new Async.Target("Application");
	@Getter(AccessLevel.PRIVATE)
	private ComponentManager componentManager;

	void onInit(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	protected <T extends Components.AppComponent> T component(Class<T> type) {
		return componentManager().component(type);
	}

	protected <T extends Components.AppComponent> void component(T component) {
		componentManager().component(component);
	}

	protected <T extends Components.AppComponent> void removeComponent(T component) {
		if (!(component instanceof Components.CoreComponent)) {
			componentManager().removeComponent(component);
			return;
		}
		log.warn("Removing Core component {} from Application", component.getClass().getSimpleName());
	}

	protected abstract void onInitialize();

	protected abstract void onUpdate(Timer.Time time);

	protected abstract void onShutdown();
}
