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

import java.sql.SQLException;

/**
 * Command design pattern, abstract command
 * 
 * @author sbartkowski
 * 
 */
abstract class SqlCommand {

	protected final SQLCommandRunTime context;

	/** Error code 'connection reestablished' */
	private final int RETRYCODE = -4498;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            SQLCommandRunTime context
	 */
	SqlCommand(SQLCommandRunTime context) {
		this.context = context;
	}

	/**
	 * Transactional command to be executed.
	 * 
	 * @throws SQLException
	 *             SQLException thrown
	 */
	abstract protected void command() throws SQLException;

	/**
	 * Executes command, encapsulation around lower level transactional code.
	 * Implements logic related to HADR failover
	 */
	void runCommand() {
		if (context.con == null) {
			context.lastResult = "Cannot execute, not connected";
			return;
		}
		try {
			command();
			context.lastResult = "Command executed successfully";
		} catch (SQLException e) {
			int errCode = e.getErrorCode();
			if (errCode == RETRYCODE) {
				// run again command in case of reestablish error
				try {
					command();
					context.lastResult = "Command executed successfully";
				} catch (SQLException e1) {
					// do not try again, throw exception unconditionally
					context.lastResult = e.getMessage();
					e.printStackTrace();
				}
			} else {
				// throws exception if error code is not reestablish error
				context.lastResult = e.getMessage();
				e.printStackTrace();
			}

		}
	}
}
