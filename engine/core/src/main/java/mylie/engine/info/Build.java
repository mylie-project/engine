package mylie.engine.info;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.Getter;
import org.slf4j.Logger;

/**
 * Provides build information for the engine, such as version, git metadata, and build time.
 * This class reads information from the version properties file at runtime.
 */
@SuppressWarnings("unused")
@Getter
public final class Build {
	/**
	 * Singleton instance of the {@code Build} class, providing access to build information.
	 */
	@Getter
	private static final Build info = new Build();

	/**
	 * The engine version retrieved from the build system.
	 */
	final String engineVersion;

	/**
	 * The last version tag from git.
	 */
	private final String lastTag;

	/**
	 * The number of commits since the last tagged release.
	 */
	private final String commitDistance;

	/**
	 * The short git hash of the current commit.
	 */
	private final String gitHash;

	/**
	 * The full git hash of the current commit.
	 */
	private final String gitHashFull;

	/**
	 * The name of the git branch used to create this build.
	 */
	private final String branchName;

	/**
	 * Indicates whether the last tagged version was clean.
	 */
	private final String isCleanTag;

	/**
	 * The timestamp of when the build was created.
	 */
	private final String buildTime;

	/**
	 * Creates an instance of the {@code Build} class and initializes build information
	 * by reading the {@code version.properties} file from the classpath.
	 *
	 * @throws IllegalStateException if the version properties file does not exist
	 * @throws RuntimeException      if an error occurs while reading the properties file
	 */
	public Build() {
		Properties properties = new Properties();
		try (InputStream versionPropertiesStream = getClass().getResourceAsStream("/mylie/engine/version.properties")) {
			if (versionPropertiesStream == null) {
				throw new IllegalStateException("Version properties file does not exist");
			}
			properties.load(new InputStreamReader(versionPropertiesStream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.engineVersion = properties.getProperty("version");
		this.lastTag = properties.getProperty("lastTag");
		this.commitDistance = properties.getProperty("commitDistance");
		this.gitHash = properties.getProperty("gitHash");
		this.gitHashFull = properties.getProperty("gitHashFull");
		this.branchName = properties.getProperty("branchName");
		this.isCleanTag = properties.getProperty("isCleanTag");
		this.buildTime = properties.getProperty("buildTime");
	}

	/**
	 * Logs the build information using the specified SLF4J logger.
	 *
	 * @param logger the SLF4J logger instance to use for logging the build information
	 */
	public void toLog(Logger logger) {
		logger.error("Engine version: {}", engineVersion);
		logger.error("Last tag: {}", lastTag);
		logger.error("Commit distance: {}", commitDistance);
		logger.error("Git hash: {}", gitHash);
		logger.error("Git hash full: {}", gitHashFull);
		logger.error("Branch name: {}", branchName);
		logger.error("Is clean tag: {}", isCleanTag);
		logger.error("Build time: {}", buildTime);
	}
}
