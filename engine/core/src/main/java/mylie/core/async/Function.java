package mylie.core.async;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Function {
	@AllArgsConstructor
	static abstract class F<R> {
		@Getter(AccessLevel.PACKAGE)
		private final String id;
	}

	public static abstract class F0<R> extends F<R> {
		public F0(String id) {
			super(id);
		}
		abstract R apply();
	}

	public static abstract class F1<A, R> extends F<R> {
		public F1(String id) {
			super(id);
		}
		abstract R apply(A a);
	}

	public static abstract class F2<A, B, R> extends F<R> {
		public F2(String id) {
			super(id);
		}
		abstract R apply(A a, B b);
	}

	public static abstract class F3<A, B, C, R> extends F<R> {
		public F3(String id) {
			super(id);
		}
		abstract R apply(A a, B b, C c);
	}
}
