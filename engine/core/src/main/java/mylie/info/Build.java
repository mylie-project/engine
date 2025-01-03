package mylie.info;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.Getter;

@Getter
public final class Build {
	@Getter
	private static final Build info = new Build();

	final String engineVersion;
	private final String lastTag;
	private final String commitDistance;
	private final String gitHash;
	private final String gitHashFull;
	private final String branchName;
	private final String isCleanTag;
	private final String buildTime;

	private Build() {
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
}
