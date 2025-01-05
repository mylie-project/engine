package mylie.core.util;

import static org.junit.jupiter.api.Assertions.*;

import mylie.util.Versioned;
import org.junit.jupiter.api.Test;

class VersionedTest {

	@Test
	void testValueMethodSetsValueAndVersionCorrectly() {
		Versioned<String> versioned = new Versioned<>();
		String newValue = "testValue";
		long newVersion = 1L;

		versioned.value(newValue, newVersion);

		assertEquals(newValue, versioned.value());
		assertEquals(newVersion, versioned.version());
	}

	@Test
	void testValueMethodUpdatesToDifferentValueAndVersion() {
		Versioned<Integer> versioned = new Versioned<>();
		versioned.value(10, 1L);

		int newValue = 20;
		long newVersion = 2L;

		versioned.value(newValue, newVersion);

		assertEquals(newValue, versioned.value());
		assertEquals(newVersion, versioned.version());
	}

	@Test
	void testValueMethodWithSameValueAndVersion() {
		Versioned<Double> versioned = new Versioned<>();
		versioned.value(3.14, 1L);

		double sameValue = 3.14;
		long sameVersion = 1L;

		versioned.value(sameValue, sameVersion);

		assertEquals(sameValue, versioned.value());
		assertEquals(sameVersion, versioned.version());
	}

	@Test
	void testValueMethodWithNullValue() {
		Versioned<String> versioned = new Versioned<>();
		long newVersion = 5L;

		versioned.value(null, newVersion);

		assertNull(versioned.value());
		assertEquals(newVersion, versioned.version());
	}
	@Test
	void testReferenceChangeWithMutableObject() {
		Versioned<StringBuilder> versioned = new Versioned<>();
		StringBuilder builder = new StringBuilder("initial");
		versioned.value(builder, 1L);

		builder.append(" changed");

		assertEquals("initial changed", versioned.value().toString());
		assertEquals(1L, versioned.version());
	}

	@Test
	void testReferenceUpdateWithSameObject() {
		Versioned<String> versioned = new Versioned<>();
		String value = "test";
		versioned.value(value, 1L);

		versioned.value(value, 2L);

		assertEquals(value, versioned.value());
		assertEquals(2L, versioned.version());
	}

	@Test
	void testConsecutiveValueAndVersionUpdates() {
		Versioned<String> versioned = new Versioned<>();
		versioned.value("first", 1L);
		versioned.value("second", 2L);
		versioned.value("third", 3L);

		assertEquals("third", versioned.value());
		assertEquals(3L, versioned.version());
	}

	@Test
	void testUpdateVersionWithoutChangingValue() {
		Versioned<String> versioned = new Versioned<>();
		versioned.value("consistent", 1L);
		versioned.value("consistent", 2L);

		assertEquals("consistent", versioned.value());
		assertEquals(2L, versioned.version());
	}

	@Test
	void testReferenceBeforeAndAfterMultipleUpdates() {
		Versioned<String> versioned = new Versioned<>();
		versioned.value("initial", 1L);

		Versioned.Reference<String> referenceBefore = versioned.reference();
		versioned.value("updated once", 2L);
		versioned.value("updated twice", 3L);

		Versioned.Reference<String> referenceAfter = versioned.reference();

		assertEquals("initial", referenceBefore.value());
		assertEquals(1L, referenceBefore.version());
		assertEquals("updated twice", referenceAfter.value());
		assertEquals(3L, referenceAfter.version());
	}

	@Test
	void testReferenceUpdateWithNewObject() {
		Versioned<String> versioned = new Versioned<>();
		versioned.value("initial", 1L);

		String newValue = "updated";
		versioned.value(newValue, 2L);

		assertEquals(newValue, versioned.value());
		assertEquals(2L, versioned.version());
	}
	@Test
	void testReferenceMethodReturnsCurrentValueAndVersion() {
		Versioned<String> versioned = new Versioned<>();
		versioned.value("initial", 1L);

		Versioned.Reference<String> reference = versioned.reference();

		assertEquals("initial", reference.value());
		assertEquals(1L, reference.version());
	}

	@Test
	void testReferenceMethodDoesNotChangeAfterUpdates() {
		Versioned<Integer> versioned = new Versioned<>();
		versioned.value(10, 1L);

		Versioned.Reference<Integer> reference = versioned.reference();

		versioned.value(20, 2L); // Update the value and version

		assertEquals(10, reference.value());
		assertEquals(1L, reference.version());
	}

	@Test
	void testReferenceForNullValue() {
		Versioned<Double> versioned = new Versioned<>();

		versioned.value(null, 5L);
		Versioned.Reference<Double> reference = versioned.reference();

		assertNull(reference.value());
		assertEquals(5L, reference.version());
	}

	@Test
	void testReferenceWithMutableObject() {
		Versioned<StringBuilder> versioned = new Versioned<>();
		StringBuilder builder = new StringBuilder("initial");
		versioned.value(builder, 1L);

		Versioned.Reference<StringBuilder> reference = versioned.reference();

		builder.append(" modified"); // Mutate the object

		assertEquals("initial modified", reference.value().toString());
		assertEquals(1L, reference.version());
	}

}
