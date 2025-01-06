package mylie.core.component;

public class Stages {
	public static final Stage PreUpdateLogic = new Stage("PreUpdateLogic");
	public static final Stage UpdateLogic = new Stage("UpdateLogic", PreUpdateLogic::execute);
	public static final Stage Render = new Stage("Render", UpdateLogic::execute);
}
