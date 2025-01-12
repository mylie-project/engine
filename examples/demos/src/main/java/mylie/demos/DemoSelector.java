package mylie.demos;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;

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
				String category = subclass.getName().substring(0, subclass.getName().lastIndexOf("."));
				category = category.replace("mylie.demos.", "");
				log.info("Found demo: {} in category: {}", subclass.getSimpleName(), category);
				Class<? extends Demo> subclass1 = subclass.loadClass().asSubclass(Demo.class);
			}
		}
	}

	public void selectDemo(Demo demo) {
		if (currentDemo == null || !currentDemo.equals(demo)) {
			removeComponent(currentDemo);
		}
		currentDemo = demo;
		component(demo);
	}

	@Override
	public void onShutdown() {

	}
}
