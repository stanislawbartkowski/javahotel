/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jdbc.proc;

/**
 * Bean containing Person row
 * 
 * @author sbartkowski
 * 
 */
public class PersonRecord {

	private final int id;
	private final String name;
	private final String familyName;

	public PersonRecord(int id, String name, String familyName) {
		this.id = id;
		this.name = name;
		this.familyName = familyName;
	}

	public PersonRecord(String name, String familyName) {
		this.id = -1;
		this.name = name;
		this.familyName = familyName;
	}

	public String getName() {
		return name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public int getId() {
		return id;
	}

}
