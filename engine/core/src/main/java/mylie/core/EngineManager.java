package mylie.core;

import mylie.core.component.Components;

public class EngineManager implements Components.AppComponent {
	private final Engine engine;

	EngineManager(Engine engine) {
		this.engine = engine;
	}

	public static void shutdown(Engine.ShutdownReason reason) {
		Engine.instance.shutdownReason(reason);
	}
}
