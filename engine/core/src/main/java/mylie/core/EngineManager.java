package mylie.core;

import mylie.core.component.Components;

public class EngineManager implements Components.AppComponent {
	private final Engine engine;

	EngineManager(Engine engine) {
		this.engine = engine;
	}

	public void shutdown(Engine.ShutdownReason reason) {
		engine.shutdownReason(reason);
	}
}
