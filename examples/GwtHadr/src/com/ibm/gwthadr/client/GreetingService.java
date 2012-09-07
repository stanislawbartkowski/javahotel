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
package com.ibm.gwthadr.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ibm.gwthadr.shared.TOPerson;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	/** Connect to database, */
	void connect();

	/** Disconnect. */
	void disconnect();
	
	/** Set autocommit mode. */
	void setAutocommit(boolean on);
	
	/** Create Person table */
	void createTable();
	
	/** Drop Person table */
	void dropTable();

	/** Get current status : connected, autocommit. */
	String getStatus();

	/** Get the result (description) of the last command. */
	String gestLastResult();

	/** Get list of persons added so far. */
	List<TOPerson> getListOfPersons();
	
	/** Add new person. */
	void addPerson(TOPerson person);
}
