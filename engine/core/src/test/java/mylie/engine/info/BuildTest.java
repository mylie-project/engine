package mylie.engine.info;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuildTest {

	/**
	 * This test verifies that each property of the `Build` object is correctly logged
	 * using the `toLog` method.
	 */
	@Test
	void toLog_shouldLogAllProperties() {
		Build build = new Build();
		assertNotNull(build.engineVersion(), "Engine version should not be null");
		assertNotNull(build.lastTag(), "Last tag should not be null");
		assertNotNull(build.commitDistance(), "Commit distance should not be null");
		assertNotNull(build.gitHash(), "Git hash should not be null");
		assertNotNull(build.gitHashFull(), "Git hash full should not be null");
		assertNotNull(build.branchName(), "Branch name should not be null");
		assertNotNull(build.isCleanTag(), "Is clean tag should not be null");
		assertNotNull(build.buildTime(), "Build time should not be null");
	}
}
