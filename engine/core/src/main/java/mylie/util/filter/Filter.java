package mylie.util.filter;

import lombok.EqualsAndHashCode;

public abstract class Filter<T> {

	@SuppressWarnings("unchecked")
	public abstract boolean apply(T... value);

	@EqualsAndHashCode(callSuper = false)
	private static final class NoOpFilter<T> extends Filter<T> {
		final boolean result;

		private NoOpFilter(boolean result) {
			this.result = result;
		}

		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			return result;
		}
	}

	@EqualsAndHashCode(callSuper = false)
	private static final class NotFilter<T> extends Filter<T> {
		final Filter<T> filter;

		public NotFilter(Filter<T> filter) {
			this.filter = filter;
		}

		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			return !filter.apply(values);
		}
	}

	@EqualsAndHashCode(callSuper = false)
	private static final class AndFilter<T> extends Filter<T> {
		final Filter<T>[] filters;
		@SafeVarargs
		public AndFilter(Filter<T>... filters) {
			this.filters = filters;
		}

		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			for (Filter<T> filter : filters) {
				if (!filter.apply(values)) {
					return false;
				}
			}
			return true;
		}
	}

	@EqualsAndHashCode(callSuper = false)
	private static final class OrFilter<T> extends Filter<T> {
		final Filter<T>[] filters;
		@SafeVarargs
		public OrFilter(Filter<T>... filters) {
			this.filters = filters;
		}

		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			for (Filter<T> filter : filters) {
				if (filter.apply(values)) {
					return true;
				}
			}
			return false;
		}
	}

	@EqualsAndHashCode(callSuper = false)
	private static final class XorFilter<T> extends Filter<T> {
		final Filter<T>[] filters;
		@SafeVarargs
		public XorFilter(Filter<T>... filters) {
			this.filters = filters;
		}

		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			int count = 0;
			for (Filter<T> filter : filters) {
				if (filter.apply(values)) {
					count++;
				}
			}
			return count == 1;
		}
	}

	@EqualsAndHashCode(callSuper = false)
	private static final class EqualFilter<T> extends Filter<T> {
		final T value;
		public EqualFilter(T value) {
			this.value = value;
		}
		@SafeVarargs
		@Override
		public final boolean apply(T... values) {
			if (values.length == 0) {
				return false;
			}
			for (T v : values) {
				if (!v.equals(this.value)) {
					return false;
				}
			}
			return true;
		}
	}

	public static <T> Filter<T> alwaysTrue() {
		return new NoOpFilter<>(true);
	}

	public static <T> Filter<T> alwaysFalse() {
		return new NoOpFilter<>(false);
	}

	public static <T> Filter<T> not(Filter<T> filter) {
		return new NotFilter<>(filter);
	}

	@SafeVarargs
	public static <T> Filter<T> and(Filter<T>... filters) {
		return new AndFilter<>(filters);
	}

	@SafeVarargs
	public static <T> Filter<T> or(Filter<T>... filters) {
		return new OrFilter<>(filters);
	}

	public static <T> Filter<T> eq(T value) {
		return new EqualFilter<>(value);
	}

	@SafeVarargs
	public static <T> Filter<T> xor(Filter<T>... filters) {
		return new XorFilter<>(filters);
	}
}
