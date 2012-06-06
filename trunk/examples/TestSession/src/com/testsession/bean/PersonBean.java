package com.testsession.bean;

import java.io.Serializable;

public class PersonBean implements Serializable {
	
	private String firstName;
	
	private String lastName;
	
	private Long id = new Long(0);

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
