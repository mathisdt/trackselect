package org.zephyrsoft.trackselect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.zephyrsoft.trackselect.model.Chapter;
import org.zephyrsoft.trackselect.model.Disc;
import org.zephyrsoft.trackselect.model.Title;

/**
 * Performs the work, oftentimes by issuing a shell command.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Service {
	
	private Properties commands;
	private AtomicInteger nextCommandId = new AtomicInteger(1);
	
	ExecutorService executor;
	
	public Service(Properties commands) {
		this.commands = commands;
		
		executor = Executors.newSingleThreadExecutor();
	}
	
	public Disc loadDiscData() {
		String name = execute(Command.READ_NAME, null);
		String rawData = execute(Command.READ_DISC, null);
		
		Disc ret = new Disc();
		ret.setName(name == null ? null : name.trim());
		
		Title currentTitle = null;
		for (String line : rawData.toLowerCase().split("\n")) {
			if (line.startsWith("title")) {
				currentTitle = parseTitle(line);
				ret.add(currentTitle);
			} else if (line.startsWith("chapter")) {
				if (currentTitle == null) {
					System.out.println("unexpected line order, ignoring: " + line);
				} else {
					currentTitle.add(parseChapter(line));
				}
			} else if (!line.trim().isEmpty()) {
				System.out.println("unknown line type, ignoring: " + line);
			}
		}
		
		return ret;
	}
	
	private static Title parseTitle(String line) {
		Title ret = new Title();
		String[] parts = line.split(" ");
		ret.setNumber(parts[1]);
		ret.setLength(parts[2]);
		return ret;
	}
	
	private static Chapter parseChapter(String line) {
		Chapter ret = new Chapter();
		String[] parts = line.split(" ");
		ret.setNumber(parts[1]);
		ret.setLength(parts[2]);
		return ret;
	}
	
	public void play(String titleNumber, String chapterNumber) {
		Map<CommandProperty, String> parameters = new HashMap<CommandProperty, String>();
		parameters.put(CommandProperty.TITLE, titleNumber);
		if (chapterNumber == null || chapterNumber.isEmpty()) {
			executeInBackground(Command.PLAY_TITLE, parameters);
		} else {
			parameters.put(CommandProperty.CHAPTER, chapterNumber);
			executeInBackground(Command.PLAY_CHAPTER, parameters);
		}
	}
	
	public void extract(final String titleNumber, final String chapterNumber, final String name,
		final LogTarget logTarget) {
		final Map<CommandProperty, String> parameters = new HashMap<CommandProperty, String>();
		parameters.put(CommandProperty.TITLE, titleNumber);
		parameters.put(CommandProperty.NAME, name);
		if (chapterNumber == null || chapterNumber.isEmpty()) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					logTarget.log("extracting title " + titleNumber + " with name " + name + "...");
					execute(Command.EXTRACT_TITLE, parameters);
					logTarget.log(name + " done!");
				}
			});
		} else {
			parameters.put(CommandProperty.CHAPTER, chapterNumber);
			executor.submit(new Runnable() {
				@Override
				public void run() {
					logTarget.log("extracting title " + titleNumber + " / chapter " + chapterNumber + " with name "
						+ name + "...");
					execute(Command.EXTRACT_CHAPTER, parameters);
					logTarget.log(name + " done!");
				}
			});
		}
	}
	
	public void cancelAllJobs() {
		executor.shutdownNow();
		try {
			executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {
			// ignore
		}
	}
	
	private String execute(final Command command, final Map<CommandProperty, String> parameters) {
		int commandId = nextCommandId.getAndIncrement();
		StringBuilder ret = new StringBuilder();
		String commandline = commands.getProperty(command.getPropertyKey());
		if (parameters != null) {
			for (Entry<CommandProperty, String> parameter : parameters.entrySet()) {
				commandline = commandline.replaceAll("\\$" + parameter.getKey().toString(), parameter.getValue());
			}
		}
		System.out.println("[" + commandId + "] starting " + command + " (" + commandline + ")");
		ProcessBuilder pb = new ProcessBuilder("sh", "-c", commandline);
		pb.redirectErrorStream(true);
		
		try {
			Process process = pb.start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.out.println("[" + commandId + "] interrupted, killing command");
				process.destroy();
			}
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String str;
				while ((str = input.readLine()) != null) {
					ret.append(str);
					ret.append("\n");
				}
			} catch (IOException ioe) {
				System.out.println("[" + commandId + "] couldn't read output, was the command killed?");
			}
		} catch (Exception e) {
			System.out.println("[" + commandId + "] problem");
			e.printStackTrace();
		}
		System.out.println("[" + commandId + "] finished");
		
		return ret.toString();
	}
	
	private void executeInBackground(final Command command, final Map<CommandProperty, String> parameters) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				execute(command, parameters);
			}
		}).start();
	}
	
}
