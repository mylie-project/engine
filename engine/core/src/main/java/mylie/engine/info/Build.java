package mylie.engine.info;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.Getter;
import org.slf4j.Logger;

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