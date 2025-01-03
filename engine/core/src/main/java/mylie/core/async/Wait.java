package mylie.core.async;

public class Wait {
	private Wait() {
	}
	public static <R> R wait(Result<R> result) {
		return result.result();
	}
}
