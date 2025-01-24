package mylie.engine.info;

import org.slf4j.Logger;

/**
 * The {@code AsciiLogo} class provides a method to log an ASCII art logo.
 * <p>
 * This class is designed to be used exclusively for logging purposes and cannot
 * be instantiated.
 */
@SuppressWarnings("unused")
public class AsciiLogo {
	private AsciiLogo() {
	}

	/**
	 * Logs an ASCII art logo using the provided logger instance.
	 *
	 * @param logger the SLF4J {@link Logger} instance used to log the ASCII logo
	 */
	public static void toLog(Logger logger) {
		logger.error(".----------------.  .----------------.  .----------------.  .----------------. "
				+ " .----------------.");
		logger.error("| .--------------. || .--------------. || .--------------. || .--------------. ||"
				+ " .--------------. |");
		logger.error("| | ____    ____ | || |  ____  ____  | || |   _____      | || |     _____    | ||"
				+ " |  _________   | |");
		logger.error("| ||_   \\  /   _|| || | |_  _||_  _| | || |  |_   _|     | || |    |_   _|   | ||"
				+ " | |_   ___  |  | |");
		logger.error("| |  |   \\/   |  | || |   \\ \\  / /   | || |    | |       | || |      | |     |"
				+ " || |   | |_  \\_|  | |");
		logger.error("| |  | |\\  /| |  | || |    \\ \\/ /    | || |    | |   _   | || |      | |     |"
				+ " || |   |  _|  _   | |");
		logger.error("| | _| |_\\/_| |_ | || |    _|  |_    | || |   _| |__/ |  | || |     _| |_    | ||"
				+ " |  _| |___/ |  | |");
		logger.error("| ||_____||_____|| || |   |______|   | || |  |________|  | || |    |_____|   | ||"
				+ " | |_________|  | |");
		logger.error("| |              | || |              | || |              | || |              | ||"
				+ " |              | |");
		logger.error("| '--------------' || '--------------' || '--------------' || '--------------' ||"
				+ " '--------------' |");
		logger.error("'----------------'  '----------------'  '----------------'  '----------------' "
				+ " '----------------'");
	}
}
