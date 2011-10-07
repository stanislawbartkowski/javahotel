/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.db2.sb.scriptlauncher;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * Class for keeping connection data
 * @author sbartkowski
 *
 */
public class DB2AccessData {

	/** Strings used as mapping in WorkingCopy. */
	private final static String DBNAME_ID = "DB2_SCRITP_LAUNCHER_DBALIAS";
	private final static String DBUSER_ID = "DB2_SCRITP_LAUNCHER_DBUSER";
	private final static String DBPASSWORD_ID = "DB2_SCRITP_LAUNCHER_DBPASSWORD";

	/** Map for keeping values (all strings) */
	private final Map<String, String> paramList;

	/**
	 * Constructs DB2AccessData filled with default values.
	 * @return DB2AccessData class
	 */
	public static DB2AccessData contructDefa() {
		return new DB2AccessData();
	}

	/**
	 * Constructs DB2AccessData filles with values taken from ILaunchConfiguration
	 * @param arg0 ILaunchConfiguration
	 * @return DB2AccessData class
	 */
	public static DB2AccessData construct(ILaunchConfiguration arg0) {
		DB2AccessData defMap = new DB2AccessData();
		Map<String, String> paramList;
		try {
			paramList = arg0.getAttribute(DB2ScriptTabView.DBACCESS_ID,
					defMap.getParamList());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return new DB2AccessData(paramList);
	}

	/**
	 * Constructor for default values.
	 */
	private DB2AccessData() {
		paramList = new HashMap<String, String>();
		setDb2Alias("SAMPLE");
		setDb2User("db2inst1");
		setDb2Password("db2inst1");
	}

	private DB2AccessData(Map<String, String> paramList) {
		this.paramList = paramList;
	}

	/**
	 * Getter for database alias
	 * @return database alias
	 */
	public String getDb2Alias() {
		return paramList.get(DBNAME_ID);
	}

	/**
	 * Setter for database alias
	 * @param db2Alias database alias
	 */
	public void setDb2Alias(String db2Alias) {
		paramList.put(DBNAME_ID, db2Alias);
	}

	/**
	 * Getter for database login (user)
	 * @return Database login
	 */
	public String getDb2User() {
		return paramList.get(DBUSER_ID);
	}

	/**
	 * Setter for database login (user)
	 * @param db2User Database login
	 */
	public void setDb2User(String db2User) {
		paramList.put(DBUSER_ID, db2User);
	}

	/**
	 * Getter for database password 
	 * @return Database password
	 */
	public String getDb2Password() {
		return paramList.get(DBPASSWORD_ID);
	}

	/**
	 * Setter for database password
	 * @param db2Password Database password
	 */
	public void setDb2Password(String db2Password) {
		paramList.put(DBPASSWORD_ID, db2Password);
	}

	/**
	 * Getter for Map containing all values
	 * @return Map
	 */
	public Map<String, String> getParamList() {
		return paramList;
	}

}
