package mylie.util.filter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FilterTest {

	/**
	 * Test class for the {@link Filter} interface. Verifies the behavior of the
	 * {@code apply} method in different scenarios using various provided static
	 * factory methods.
	 */

	@Test
	void testAlwaysTrue() {
		// Arrange
		Filter<Integer> alwaysTrueFilter = Filter.alwaysTrue();

		// Act & Assert
		assertTrue(alwaysTrueFilter.apply(1));
		assertTrue(alwaysTrueFilter.apply(10, 20));
		assertTrue(alwaysTrueFilter.apply());
	}

	@Test
	void testAlwaysFalse() {
		// Arrange
		Filter<Integer> alwaysFalseFilter = Filter.alwaysFalse();

		// Act & Assert
		assertFalse(alwaysFalseFilter.apply(1));
		assertFalse(alwaysFalseFilter.apply(10, 20));
		assertFalse(alwaysFalseFilter.apply());
	}

	@Test
	void testNotFilter() {
		// Arrange
		Filter<Integer> alwaysTrueFilter = Filter.alwaysTrue();
		Filter<Integer> notFilter = Filter.not(alwaysTrueFilter);

		// Act & Assert
		assertFalse(notFilter.apply(1));
		assertFalse(notFilter.apply(10, 20));
		assertFalse(notFilter.apply());

		// Test NOT with alwaysFalse
		Filter<Integer> notAlwaysFalse = Filter.not(Filter.alwaysFalse());
		assertTrue(notAlwaysFalse.apply(1));
		assertTrue(notAlwaysFalse.apply(10, 20));
		assertTrue(notAlwaysFalse.apply());
	}

	@Test
	void testAndFilter() {
		// Arrange
		Filter<Integer> alwaysTrueFilter = Filter.alwaysTrue();
		Filter<Integer> alwaysFalseFilter = Filter.alwaysFalse();

		Filter<Integer> andFilterTrue = Filter.and(alwaysTrueFilter, alwaysTrueFilter);
		Filter<Integer> andFilterFalse = Filter.and(alwaysTrueFilter, alwaysFalseFilter);

		// Act & Assert
		assertTrue(andFilterTrue.apply(1));
		assertTrue(andFilterTrue.apply(2, 3));
		assertFalse(andFilterFalse.apply(1));
		assertFalse(andFilterFalse.apply(2, 3));
	}

	@Test
	void testOrFilter() {
		// Arrange
		Filter<Integer> alwaysTrueFilter = Filter.alwaysTrue();
		Filter<Integer> alwaysFalseFilter = Filter.alwaysFalse();

		Filter<Integer> orFilterTrue = Filter.or(alwaysTrueFilter, alwaysFalseFilter);
		Filter<Integer> orFilterFalse = Filter.or(alwaysFalseFilter, alwaysFalseFilter);

		// Act & Assert
		assertTrue(orFilterTrue.apply(1));
		assertTrue(orFilterTrue.apply(2, 3));
		assertFalse(orFilterFalse.apply(1));
		assertFalse(orFilterFalse.apply(2, 3));
	}

	@Test
	void testXorFilter() {
		// Arrange
		Filter<Integer> alwaysTrueFilter = Filter.alwaysTrue();
		Filter<Integer> alwaysFalseFilter = Filter.alwaysFalse();

		Filter<Integer> xorFilterTrue = Filter.xor(alwaysTrueFilter, alwaysFalseFilter);
		Filter<Integer> xorFilterFalse = Filter.xor(alwaysTrueFilter, alwaysTrueFilter);

		// Act & Assert
		assertTrue(xorFilterTrue.apply(1));
		assertTrue(xorFilterTrue.apply(2, 3));
		assertFalse(xorFilterFalse.apply(1));
		assertFalse(xorFilterFalse.apply(2, 3));
	}

	@Test
	void testEqFilter() {
		// Arrange
		Filter<Integer> valueEqualsFive = Filter.eq(5);

		// Act & Assert
		assertTrue(valueEqualsFive.apply(5));
		assertFalse(valueEqualsFive.apply(10));
		assertFalse(valueEqualsFive.apply());

		// Test with other types
		Filter<String> valueEqualsHello = Filter.eq("Hello");
		assertTrue(valueEqualsHello.apply("Hello"));
		assertFalse(valueEqualsHello.apply("hello"));
		assertFalse(valueEqualsHello.apply("world"));
		assertFalse(valueEqualsHello.apply());
	}

}
