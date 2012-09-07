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

	void connect();

	void disconnect();
	
	void setAutocommit(boolean on);
	
	void createTable();
	
	void dropTable();

	String getStatus();

	String gestLastResult();
	
	List<TOPerson> getListOfPersons();
	
	void addPerson(TOPerson person);
}
