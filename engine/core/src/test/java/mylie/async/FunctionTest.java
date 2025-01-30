// File: src/test/java/mylie/async/FunctionTest.java

package mylie.async;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FunctionTest {

	/**
	 * Concrete implementation of F0 for testing.
	 */
	private static class TestF0 extends Function.F0<String> {
		protected TestF0(String id) {
			super(id);
		}
		@Override
		protected String apply() {
			return "Hello F0";
		}
	}

	/**
	 * Concrete implementation of F1 for testing.
	 */
	private static class TestF1 extends Function.F1<Integer, Integer> {
		protected TestF1(String id) {
			super(id);
		}
		@Override
		protected Integer apply(Integer a) {
			return a * 2;
		}
	}

	/**
	 * Concrete implementation of F2 for testing.
	 */
	private static class TestF2 extends Function.F2<Integer, Integer, Integer> {
		protected TestF2(String id) {
			super(id);
		}
		@Override
		protected Integer apply(Integer a, Integer b) {
			return a + b;
		}
	}

	/**
	 * Concrete implementation of F3 for testing.
	 */
	private static class TestF3 extends Function.F3<Integer, Integer, Integer, String> {
		protected TestF3(String id) {
			super(id);
		}
		@Override
		protected String apply(Integer a, Integer b, Integer c) {
			return "Sum: " + (a + b + c);
		}
	}

	@Test
	void testF0() {
		TestF0 f0 = new TestF0("f0Id");
		assertEquals("Hello F0", f0.apply(), "F0 apply should return 'Hello F0'");
		assertEquals("f0Id", f0.id(), "F0 id should match assigned value");
	}

	@Test
	void testF1() {
		TestF1 f1 = new TestF1("f1Id");
		assertEquals(10, f1.apply(5), "F1 should double the input value");
		assertEquals("f1Id", f1.id(), "F1 id should match assigned value");
	}

	@Test
	void testF2() {
		TestF2 f2 = new TestF2("f2Id");
		assertEquals(8, f2.apply(3, 5), "F2 should sum the two input values");
		assertEquals("f2Id", f2.id(), "F2 id should match assigned value");
	}

	@Test
	void testF3() {
		TestF3 f3 = new TestF3("f3Id");
		String expected = "Sum: 12";
		assertEquals(expected, f3.apply(3, 4, 5), "F3 should properly compute a + b + c");
		assertEquals("f3Id", f3.id(), "F3 id should match assigned value");
	}
}
