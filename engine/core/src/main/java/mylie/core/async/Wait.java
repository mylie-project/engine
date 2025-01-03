package mylie.core.async;

public class Wait {
	public static <R> R wait(Result<R> result) {
		return result.result();
	}
}
