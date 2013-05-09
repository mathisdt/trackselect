package org.zephyrsoft.trackselect;

/**
 * Represents the available commands that can be executed. Every command uses a shell expression defined in
 * commands.properties.
 * 
 * @author Mathis Dirksen-Thedens
 */
public enum Command {
	
	READ_NAME("read.name"),
	READ_DISC("read.disc"),
	PLAY_TITLE("play.title"),
	PLAY_CHAPTER("play.chapter"),
	EXTRACT_TITLE("extract.title"),
	EXTRACT_CHAPTER("extract.chapter");
	
	private String propertyKey;
	
	private Command(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	
	public String getPropertyKey() {
		return propertyKey;
	}
}
