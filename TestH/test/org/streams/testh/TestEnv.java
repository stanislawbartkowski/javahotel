package org.streams.testh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.streams.testh1.TestHR;

abstract public class TestEnv {

	protected Connection conH;
	protected java.sql.Connection conB;

	protected BufferedReader getB(String test) throws IOException {
		ClassLoader classLoader = TestHR.class.getClassLoader();
		InputStream is = classLoader.getResource("resource/" + test + ".txt").openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader;
	}

	protected void runU(String stmt) throws SQLException {
		System.out.println(stmt);
		conB.createStatement().execute(stmt);
	}

	@Before
	public void setup() throws IOException, ClassNotFoundException, SQLException {
		conH = TestHelper.connect();
		conB = TestHelper.getC();
	}

	@After
	public void winddown() throws IOException, SQLException {
		if (conH != null)
			conH.close();
		if (conB != null)
			conB.close();
	}

}
