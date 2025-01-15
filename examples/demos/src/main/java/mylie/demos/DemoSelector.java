package mylie.demos;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import mylie.core.Engine;
import mylie.core.component.Components;
import mylie.core.components.Vault;

@Slf4j
public class DemoSelector extends Components.AppSequential implements Components.Initializable {
	private final Map<String, List<Demo>> loadedDemos = new HashMap<>();
	private Demo currentDemo;
	@Override
	public void onInitialize() {
		log.info("Scanning for demos...");
		ClassGraph classGraph = new ClassGraph().enableAllInfo().acceptPackages("mylie.demos").enableClassInfo();
		try (ScanResult scanResult = classGraph.scan()) {
			for (ClassInfo subclass : scanResult.getSubclasses(Demo.class)) {
				String category = extractCategory(subclass.getName());
				log.info("Found demo: {} in category: {}", subclass.getSimpleName(), category);
				Class<? extends Demo> subclass1 = subclass.loadClass().asSubclass(Demo.class);
				try {
					Constructor<? extends Demo> constructor = subclass1.getDeclaredConstructor();
					Demo demo = constructor.newInstance();
					List<Demo> demos = loadedDemos.computeIfAbsent(category, k -> new ArrayList<>());
					demos.add(demo);
				} catch (NoSuchMethodException | InvocationTargetException | InstantiationException
						| IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		Engine.Args args = component(Vault.class).value(Engine.Args.class);
		if (args != null) {
			if (args.defined("runDemo")) {
				String demoId = args.value("runDemo");
				selectDemo(demoId);
			}
		}
	}

	public void selectDemo(String demoId) {
		String category = extractCategory(demoId);
		String id = extractId(demoId);
		List<Demo> demos = loadedDemos.get(category);
		if (demos != null) {
			for (Demo demo : demos) {
				if (demo.getClass().getSimpleName().equals(id)) {
					selectDemo(demo);
					return;
				}
			}
			log.error("No demo found for id: {} in category: {}", demoId, category);
		} else {
			log.error("No demos found for category: {}", category);
		}
	}

	public void selectDemo(Demo demo) {
		if (currentDemo == null || !currentDemo.equals(demo)) {
			removeComponent(currentDemo);
		}
		currentDemo = demo;
		component(demo);
	}

	private String extractId(String id) {
		return id.substring(id.lastIndexOf(".") + 1);
	}

	private String extractCategory(String id) {
		String substring = id.substring(0, id.lastIndexOf("."));
		substring = substring.replace("mylie.demos.", "");
		return substring;
	}

	@Override
	public void onShutdown() {

	}
}
