package mylie.core.async;

public class TestResult {
	public static <T> Result<T> result(T result) {
		Result.Fixed<T> objectFixed = new Result.Fixed<>(null, -1);
		objectFixed.result(result);
		return objectFixed;
	}
}
