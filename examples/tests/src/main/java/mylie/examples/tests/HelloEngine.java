package mylie.examples.tests;

import mylie.core.Engine;
import mylie.core.EngineConfiguration;
import mylie.platform.Desktop;

public class HelloEngine {
    public static void main(String[] args) {
        Desktop desktop = new Desktop();
        EngineConfiguration engineConfiguration = desktop.initialize();
        engineConfiguration.startEngine();
    }
}
