package com.export.db2.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.export.db2.main.util.ExportProperties;

public class TestProp {

	static String getTestName() {
		URL u = TestProp.class.getClassLoader().getResource("testd/oracleexp.properties");
//		URL u = TestProp.class.getClassLoader().getResource("testd/mssqlexp.properties");
		return u.getPath();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Properties prop = ExportProperties.readProp(getTestName());
	}

}
