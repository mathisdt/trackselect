package org.zephyrsoft.trackselect;

/**
 * Represents the available commands that can be executed. Every command uses a shell expression defined in
 * commands.properties.
 */
public enum Command {

	READ_NAME("read.name", null),
	READ_DISC("read.disc", null),
	PLAY_TITLE("play.title", null),
	PLAY_CHAPTER("play.chapter", null),
	EXTRACT_TITLE("extract.title", "external.process"),
	EXTRACT_CHAPTER("extract.chapter", "external.process");

	private String propertyKey;
	private String commandPrefix;

	private Command(String propertyKey, String commandPrefix) {
		this.propertyKey = propertyKey;
		this.commandPrefix = commandPrefix;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public String getCommandPrefix() {
		return commandPrefix;
	}

}
