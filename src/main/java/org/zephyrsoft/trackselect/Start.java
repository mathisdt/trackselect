package org.zephyrsoft.trackselect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Properties;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * start-up class for this application
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Start {
	
	private static final Logger LOG = LoggerFactory.getLogger(Start.class);
	
	private static final String USER_SETTINGS_PATH = System.getProperty("user.home")
		+ "/.trackselect/commands.properties";
	
	public static void main(String[] args) {
		
		Properties commands = new Properties();
		File userPropertiesFile = new File(USER_SETTINGS_PATH);
		boolean commandsLoaded = false;
		if (userPropertiesFile.exists() && userPropertiesFile.canRead()) {
			LOG.info("loading user command definitions");
			try {
				commands.load(new FileReader(userPropertiesFile));
				commandsLoaded = true;
			} catch (Exception e) {
				LOG.warn("could not load user command definitions");
			}
		} else {
			LOG.info("no user settings found at {}, using default", USER_SETTINGS_PATH);
		}
		
		if (!commandsLoaded) {
			try {
				commands.load(Start.class.getResourceAsStream("/default-commands.properties"));
			} catch (Exception e) {
				LOG.error("could load neither user nor default command definitions, aborting");
				System.exit(-1);
			}
		}
		
		Service service = new Service(commands);
		
		// set look-and-feel (temporarily redirect error stream so the console is not polluted)
		PrintStream originalErrorStream = System.err;
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ee) {
				// set no specific look-and-feel as it keeps on making problems
			}
		}
		System.setErr(originalErrorStream);
		
		GUI gui = new GUI(service);
		gui.setVisible(true);
		
	}
	
}
