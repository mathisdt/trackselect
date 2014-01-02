package org.zephyrsoft.trackselect.model;

/**
 * An item that can be selected for extraction.
 * 
 * @author Mathis Dirksen-Thedens
 */
public abstract class Selectable {
	
	/** filled by user */
	private boolean selected = false;
	/** filled by user */
	private String name = "";
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
