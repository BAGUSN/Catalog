package com.sourcen.microsite.model;

import java.io.Serializable;
import java.util.Date;



public class User implements Serializable{
	private String firstName;

	private String lastName;

	private boolean enabled;
	private boolean admin=false;
	private String created;
	private String modified;
	private String email;
	private String timeZone;
	private String password;
	private int id;
	private String username;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public boolean isAdmin() {
		return admin;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String passowrd) {
		this.password = passowrd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public Object getLastLoggedIn() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLastLoggedIn(Date date) {
		// TODO Auto-generated method stub
		
	}
	

}
