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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ibm.gwthadr.shared.TOPerson;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void connect(AsyncCallback<Void> callback);

	void disconnect(AsyncCallback<Void> callback);

	void getStatus(AsyncCallback<String> callback);

	void gestLastResult(AsyncCallback<String> callback);

	void setAutocommit(boolean on, AsyncCallback<Void> callback);

	void createTable(AsyncCallback<Void> callback);

	void dropTable(AsyncCallback<Void> callback);

	void addPerson(TOPerson person, AsyncCallback<Void> callback);

	void getListOfPersons(AsyncCallback<List<TOPerson>> callback);
}
