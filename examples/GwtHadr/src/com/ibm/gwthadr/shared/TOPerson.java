package com.ibm.gwthadr.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TOPerson implements IsSerializable {

	private int id;
	private String name;
	private String familyName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

}
