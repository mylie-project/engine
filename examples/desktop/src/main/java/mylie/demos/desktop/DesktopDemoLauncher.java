package mylie.demos.desktop;

import mylie.core.EngineConfiguration;
import mylie.demos.DemoApplication;
import mylie.lwjgl3.openal.OpenAlApi;
import mylie.platform.Desktop;

public class DesktopDemoLauncher {
	public static void main(String[] args) {
		EngineConfiguration engineConfiguration = new Desktop().initialize(args);
		engineConfiguration.option(EngineConfiguration.Application, new DemoApplication());
		engineConfiguration.option(EngineConfiguration.AudioApi,new OpenAlApi());
		engineConfiguration.startEngine();
	}
}
