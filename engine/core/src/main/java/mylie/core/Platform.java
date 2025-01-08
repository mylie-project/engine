package mylie.core;

public abstract class Platform {
	public EngineConfiguration initialize(String[] args) {
		EngineConfiguration engineConfiguration = new EngineConfiguration(new Engine.Args(args),this);
		initialize(engineConfiguration);
		return engineConfiguration;
	}

	protected abstract void initialize(EngineConfiguration engineConfiguration);
}
