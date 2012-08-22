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

import java.util.List;

/**
 * Facade to some JDBC/SQL action.
 * 
 * @author sbartkowski
 * 
 */
public interface IJDBCAction {

	/**
	 * Connect to database
	 */
	public void connect();

	/**
	 * Disconnect
	 */
	public void disconnect();

	/**
	 * Add new person to PERSON table.
	 * 
	 * @param re
	 *            Person data to add (only first and family name should be
	 *            filled)
	 */
	public void addPerson(PersonRecord re);

	/**
	 * Return the list of the persons
	 * 
	 * @return List of persons.
	 */
	List<PersonRecord> getList();

	/**
	 * Create PERSON table
	 */
	public void createTable();

	/**
	 * Drop PERSON table
	 */
	public void dropTable();

	/**
	 * Get current connection status
	 * 
	 * @return String describing the current status
	 */
	public String getStatus();

	/**
	 * Get the status of the last executed statement
	 * 
	 * @return String describing the status of the last statement
	 */
	public String getLastResult();

	/**
	 * Set autocommit mode
	 * @param on true: set autocommit on, off otherwise
	 */
	public void setAutocommit(boolean on);
}
