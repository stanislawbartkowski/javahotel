/*
 * Copyright 2015 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client.database;

import java.util.List;


import com.ibm.mystreams.shared.ConnectionData;
/**
 * 
 * @author sbartkowski
 * Container for database containing connections data
 */

public interface IDatabase {

	
	enum OP {
		ADD, REMOVE, CHANGE
	};

	// List of connections
	List<ConnectionData> getList();

	// Add, remove or change connection
	void databaseOp(OP op, ConnectionData data);

	// Test if connection exists (by host, port and user)
	boolean connectionExists(ConnectionData data);

	// Creates connection identifier host:port:user
	String toS(ConnectionData data);
	
	// Find connection by identifier
	ConnectionData findS(String s);

}
