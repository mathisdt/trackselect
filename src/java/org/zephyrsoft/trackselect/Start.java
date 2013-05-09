package org.zephyrsoft.trackselect;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import javax.swing.UIManager;

/**
 * start-up class for this application
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Start {
	
	public static void main(String[] args) {
		
		Properties commands = new Properties();
		try {
			commands.load(Start.class.getResourceAsStream("/commands.properties"));
		} catch (Exception e) {
			System.err.println("could not load command definitions");
			System.exit(-1);
		}
		Service service = new Service(commands);
		
		// set look-and-feel
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
