package mylie.async;

import java.util.Collection;

public class Wait {
	private Wait() {
	}
	public static <R> R wait(Result<R> result) {
		return result.result();
	}

	public static <R> void wait(Collection<Result<R>> async) {
		async.forEach(Result::result);
	}
}
