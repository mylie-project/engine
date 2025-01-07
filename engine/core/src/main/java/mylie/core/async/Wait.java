package mylie.core.async;

import java.util.Collection;

public class Wait {
	private Wait() {
	}
	public static <R> R wait(Result<R> result) {
		return result.result();
	}

	public static void wait(Collection<Result<?>> async) {
		async.forEach(Result::result);
	}
}
