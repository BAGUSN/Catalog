package com.xtream.xstream_ex2;

public class Entry {
	private String title;
	private String description;
	
	public Entry(String title, String description){
		this.title = title;
		this.description = description;
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
