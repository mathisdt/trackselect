package org.zephyrsoft.trackselect.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A disc's title.
 */
public class Title extends Selectable implements Iterable<Chapter> {

	private String number;
	private String length;
	private List<Chapter> chapters = new ArrayList<>();

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public List<Chapter> getChapters() {
		return Collections.unmodifiableList(chapters);
	}

	public boolean add(Chapter e) {
		return chapters.add(e);
	}

	@Override
	public Iterator<Chapter> iterator() {
		return chapters.iterator();
	}

}
