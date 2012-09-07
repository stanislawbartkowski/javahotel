package com.ibm.gwthadr.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.gwthadr.client.GreetingService;
import com.ibm.gwthadr.shared.TOPerson;
import com.jdbc.proc.IGetConnection;
import com.jdbc.proc.IJDBCAction;
import com.jdbc.proc.JDBCActionFactory;
import com.jdbc.proc.PersonRecord;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private class GetConnection implements IGetConnection {

		@Override
		public Connection getCon() throws SQLException, ClassNotFoundException {
			InitialContext ic;
			DataSource ds;
			try {
				ic = new InitialContext();
				ds = (DataSource) ic.lookup("java:comp/env/jdbc/SAMPLE");
				Connection c = ds.getConnection();
				return c;
			} catch (NamingException e) {
				throw new ClassNotFoundException(e.getLocalizedMessage());
			}
		}

	}

	private final IJDBCAction i = JDBCActionFactory
			.contruct(new GetConnection());

	@Override
	public void connect() {
		i.connect();
	}

	@Override
	public void disconnect() {
		i.disconnect();
	}

	@Override
	public String getStatus() {
		return i.getStatus();
	}

	@Override
	public String gestLastResult() {
		return i.getLastResult();
	}

	@Override
	public void setAutocommit(boolean on) {
		i.setAutocommit(on);
	}

	@Override
	public void createTable() {
		i.createTable();

	}

	@Override
	public void dropTable() {
		i.dropTable();
	}

	@Override
	public List<TOPerson> getListOfPersons() {
		List<PersonRecord> li = i.getList();
		List<TOPerson> peList = new ArrayList<TOPerson>();
		for (PersonRecord pe : li) {
			TOPerson person = new TOPerson();
			person.setId(pe.getId());
			person.setName(pe.getName());
			person.setFamilyName(pe.getFamilyName());
			peList.add(person);
		}
		return peList;
	}

	@Override
	public void addPerson(TOPerson person) {
		PersonRecord pe = new PersonRecord(person.getName(),
				person.getFamilyName());
		i.addPerson(pe);
	}

}
