package mylie.async;

import java.util.Collection;

/**
 * Utility class that provides synchronous blocking methods for working with asynchronous computations.
 * Allows waiting for individual or collections of results to complete and retrieve their values.
 */
public class Wait {
	private Wait() {
	}

	/**
	 * Waits for the provided {@link Result} to complete and returns its computed value.
	 *
	 * @param result the {@link Result} to wait for and retrieve its value
	 * @param <R>    the type of the result
	 * @return the value of the completed result
	 */
	public static <R> R wait(Result<R> result) {
		return result.result();
	}

	/**
	 * Waits for a collection of {@link Result} objects to complete. Each result in the collection
	 * is waited on sequentially to ensure they complete.
	 *
	 * @param async the collection of {@link Result} objects to wait for
	 * @param <R>   the type of the results in the collection
	 */
	public static <R> void wait(Collection<Result<R>> async) {
		async.forEach(Result::result);
	}
}
