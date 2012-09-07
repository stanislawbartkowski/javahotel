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
