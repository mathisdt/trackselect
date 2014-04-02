package org.zephyrsoft.trackselect.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The whole disc.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Disc implements Iterable<Title> {
	
	private String name;
	private List<Title> titles = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Title> getTitles() {
		return Collections.unmodifiableList(titles);
	}
	
	public boolean add(Title e) {
		return titles.add(e);
	}
	
	@Override
	public Iterator<Title> iterator() {
		return titles.iterator();
	}
	
}
