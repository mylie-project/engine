package mylie.core.input;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import mylie.core.action.Action;
import mylie.core.action.ActionGroup;
import mylie.core.action.Actions;
import mylie.core.action.ObservableAction;
import mylie.core.async.Result;
import mylie.core.async.TestResult;
import mylie.core.input.devices.Keyboard;
import mylie.util.filter.Filter;
import org.junit.jupiter.api.Test;

class InputSystemTest {

	@Test
	public void testSimpleMapping() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Filter.eq(system.device(Keyboard.class)), Filter.eq(Keyboard.Key.ESC), b -> b);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertEquals(1, counter.get());
	}

	@Test
	public void testMappedKeyIsNotPressed() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Filter.eq(system.device(Keyboard.class)), Filter.eq(Keyboard.Key.ESC), b -> b);
		simulatedInputProvider.simulate(Keyboard.Key.ENTER, true); // Simulating a different key
		system.onUpdate();
		assertEquals(0, counter.get());
	}

	@Test
	public void testMultipleKeyPresses() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Filter.eq(system.device(Keyboard.class)), Filter.eq(Keyboard.Key.ESC), b -> b);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertEquals(3, counter.get());
	}

	@Test
	public void testDecrementActionMapping() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> decrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::decrement, counter);

		system.map(decrementAction, Filter.eq(system.device(Keyboard.class)), Filter.eq(Keyboard.Key.BACKSPACE),
				b -> b);
		simulatedInputProvider.simulate(Keyboard.Key.BACKSPACE, true);
		system.onUpdate();
		simulatedInputProvider.simulate(Keyboard.Key.BACKSPACE, true);
		system.onUpdate();
		assertEquals(-2, counter.get());
	}

	@Test
	public void testMappingSimple() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Keyboard.class, Keyboard.Key.ESC);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertEquals(1, counter.get());
	}

	@Test
	public void testDoubleMappingEqual() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Keyboard.class, Keyboard.Key.ESC);
		system.map(incrementAction, Keyboard.class, Keyboard.Key.ESC);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertEquals(1, counter.get());
	}

	@Test
	public void testDoubleMappingNotEqual() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Keyboard.class, Keyboard.Key.BACKSPACE);
		system.map(incrementAction, Keyboard.class, Keyboard.Key.ESC);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertEquals(1, counter.get());
		simulatedInputProvider.simulate(Keyboard.Key.BACKSPACE, true);
		system.onUpdate();
		assertEquals(2, counter.get());
	}

	@Test
	public void testObservableAction() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		ObservableAction<Boolean> observable = Actions.observable(ActionGroup.GLOBAL_INPUT);
		system.map(observable, Keyboard.class, Keyboard.Key.ESC);
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertTrue(observable.value());
		simulatedInputProvider.simulate(Keyboard.Key.ESC, true);
		system.onUpdate();
		assertTrue(observable.value());
		simulatedInputProvider.simulate(Keyboard.Key.ESC, false);
		system.onUpdate();
		assertFalse(observable.value());
	}

	@Test
	public void testKeySequence() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Keyboard.class,
				InputFilters.sequence(Keyboard.Key.A, Keyboard.Key.S, Keyboard.Key.D, Keyboard.Key.F));
		simulatedInputProvider.simulate(Keyboard.Key.A, true);
		simulatedInputProvider.simulate(Keyboard.Key.S, true);
		simulatedInputProvider.simulate(Keyboard.Key.D, true);
		simulatedInputProvider.simulate(Keyboard.Key.F, true);
		system.onUpdate();
		assertEquals(1, counter.get());
	}

	@Test
	public void testKeySequence2() {
		InputSystem system = new InputSystem();
		SimulatedInputProvider simulatedInputProvider = new SimulatedInputProvider(system);
		ActionGroup.GLOBAL_INPUT.enabled(true);
		AtomicInteger counter = new AtomicInteger(0);
		Action<Boolean> incrementAction = Actions.call(ActionGroup.GLOBAL_INPUT, b -> b, this::increment, counter);
		system.map(incrementAction, Keyboard.class,
				InputFilters.sequence(Keyboard.Key.A, Keyboard.Key.S, Keyboard.Key.D, Keyboard.Key.F));
		simulatedInputProvider.simulate(Keyboard.Key.A, true);
		simulatedInputProvider.simulate(Keyboard.Key.S, true);
		simulatedInputProvider.simulate(Keyboard.Key.D, true);
		simulatedInputProvider.simulate(Keyboard.Key.A, true);
		system.onUpdate();
		assertEquals(0, counter.get());
		simulatedInputProvider.simulate(Keyboard.Key.S, true);
		simulatedInputProvider.simulate(Keyboard.Key.D, true);
		simulatedInputProvider.simulate(Keyboard.Key.F, true);
		system.onUpdate();
		assertEquals(1, counter.get());
		simulatedInputProvider.simulate(Keyboard.Key.A, true);
		simulatedInputProvider.simulate(Keyboard.Key.S, true);
		simulatedInputProvider.simulate(Keyboard.Key.D, true);
		simulatedInputProvider.simulate(Keyboard.Key.F, true);
		system.onUpdate();
		assertEquals(2, counter.get());
		simulatedInputProvider.simulate(Keyboard.Key.F, true);
		system.onUpdate();
		assertEquals(2, counter.get());
	}

	private void increment(AtomicInteger counter) {
		counter.incrementAndGet();
	}

	private void decrement(AtomicInteger counter) {
		counter.decrementAndGet();
	}

	private static final class SimulatedInputProvider implements Input.Provider {
		private final Keyboard keyboard;
		private final Queue<Input.Event<?, ?, ?>> events = new java.util.LinkedList<>();
		private SimulatedInputProvider(InputSystem inputSystem) {
			inputSystem.registerInputProvider(this);
			this.keyboard = inputSystem.device(Keyboard.class);
		}

		@Override
		public Result<Collection<Input.Event<?, ?, ?>>> events() {
			ArrayList<Input.Event<?, ?, ?>> tmpEvents = new ArrayList<>(events);
			events.clear();
			return TestResult.result(tmpEvents);
		}

		public void simulate(Keyboard.Key key, boolean b) {
			events.add(new Input.Event<>(this.getClass(), keyboard, key, b));
		}
	}

}
