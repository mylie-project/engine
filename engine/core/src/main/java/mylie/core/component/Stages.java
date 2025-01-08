package mylie.core.component;

public class Stages {
	private Stages() {
	}
	public static final Stage PreUpdateLogic = new Stage("PreUpdateLogic");
	public static final Stage UpdateLogic = new Stage("UpdateLogic", PreUpdateLogic::execute);
	public static final Stage Render = new Stage("Render", UpdateLogic::execute);
	public static final Stage PostRender = new Stage("PostRender", Render::execute);
}
