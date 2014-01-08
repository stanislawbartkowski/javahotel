/*
 * Copyright 2014 stanislawbartkowski@gmail.com
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
package com.ibm.mystreams.client;

import com.google.gwt.i18n.client.Constants;

public interface VConstants extends Constants {

	@DefaultStringValue("Cannot find job TestFlow")
	String cannotFindJob();

	@DefaultStringValue("List of connections")
	String listOfConnectionLabel();

	@DefaultStringValue("Connection")
	String connectionLabel();

	@DefaultStringValue("Excellent")
	String excellentLabel();

	@DefaultStringValue("Rating0")
	String rating0Label();

	@DefaultStringValue("Rating")
	String ratingLabel();

	@DefaultStringValue("Type")
	String typeLabel();

	@DefaultStringValue("Actual Rating")
	String actualRatingTitle();

	@DefaultStringValue("Time")
	String timeLabel();

	@DefaultStringValue("Add")
	String addButton();

	@DefaultStringValue("Close")
	String closeButton();

	@DefaultStringValue("Host")
	String hostLabel();

	@DefaultStringValue("Port")
	String portLabel();

	@DefaultStringValue("User")
	String userLabel();

	@DefaultStringValue("Password")
	String passwordLabel();

	@DefaultStringValue("Running")
	String runningLabel();

	@DefaultStringValue("Remove")
	String removeButton();

	@DefaultStringValue("Accept")
	String acceptButton();

	@DefaultStringValue("Resign")
	String resignButton();

	@DefaultStringValue("Change")
	String changeButton();

	@DefaultStringValue("Test")
	String testButton();

	@DefaultStringValue("Do you really want to remove this connection ?")
	String removeQuestion();

	@DefaultStringValue("Do you really want to modify this connection ?")
	String changeQuestion();

	@DefaultStringValue("Failed.")
	String failedTestInfo();

	@DefaultStringValue("Connected.")
	String connectedTestInfo();

}
