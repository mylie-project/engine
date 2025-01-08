package mylie.demos.desktop;

import mylie.core.EngineConfiguration;
import mylie.demos.DemoApplication;
import mylie.platform.Desktop;

public class DesktopDemoLauncher {
	public static void main(String[] args) {
		EngineConfiguration engineConfiguration = new Desktop().initialize(args);
		engineConfiguration.option(EngineConfiguration.Application, new DemoApplication());
		engineConfiguration.startEngine();
	}
}
