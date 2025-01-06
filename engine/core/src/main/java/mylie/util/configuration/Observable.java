package mylie.util.configuration;

public class Observable<T, V> extends Configuration.Option<T, V> {
	public Observable(V defaultValue) {
		super(defaultValue);
	}
}
