package org.zephyrsoft.trackselect.model;

/**
 * A title's chapter.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class Chapter extends Selectable {
	
	private String number;
	private String length;
	
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
	
}
