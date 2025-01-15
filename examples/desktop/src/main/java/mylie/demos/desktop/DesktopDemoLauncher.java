package mylie.demos.desktop;

import mylie.core.EngineConfiguration;
import mylie.demos.DemoApplication;
import mylie.lwjgl3.openal.Lwjgl3OpenAlApi;
import mylie.platform.Desktop;

public class DesktopDemoLauncher {
	public static void main(String[] args) {
		System.out.println("****" + args.length);
		EngineConfiguration engineConfiguration = new Desktop().initialize(args);
		engineConfiguration.option(EngineConfiguration.Application, new DemoApplication());
		engineConfiguration.option(EngineConfiguration.AudioApi, new Lwjgl3OpenAlApi());
		engineConfiguration.startEngine();
	}
}
