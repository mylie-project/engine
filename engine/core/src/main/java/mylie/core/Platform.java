package mylie.core;

public abstract class Platform {
	public EngineConfiguration initialize() {
		EngineConfiguration engineConfiguration = new EngineConfiguration(this);
		initialize(engineConfiguration);
		return engineConfiguration;
	}

	protected abstract void initialize(EngineConfiguration engineConfiguration);
}
