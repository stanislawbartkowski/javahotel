package com.jythonui.server.envvar;

import javax.mail.Session;
import javax.sql.DataSource;


public interface IGetEnvVariable {
	
	interface IEnvVar {
		boolean getL();
		String getS();
		boolean isEmpty();
		Session  getSession();
		DataSource getDS();
	}
	
	enum ResType {
	  LOG,STRING,MAIL,JDBC 
	}
	
    IEnvVar getEnvString(String name, ResType rType, boolean throwerror);

}
