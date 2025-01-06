package mylie.util.configuration;

public class Changeable<T, V> extends Observable<T, V> {
	public Changeable(V defaultValue) {
		super(defaultValue);
	}
}
