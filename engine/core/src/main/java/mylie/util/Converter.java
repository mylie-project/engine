package mylie.util;

public interface Converter<A, B> {
	B convert(A a);
}
