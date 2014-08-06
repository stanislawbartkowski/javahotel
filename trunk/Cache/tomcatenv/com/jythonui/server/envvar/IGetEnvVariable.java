package com.jythonui.server.envvar;

import javax.mail.Session;


public interface IGetEnvVariable {
	
	interface IEnvVar {
		boolean getL();
		String getS();
		boolean isEmpty();
		Session  getSession();
	}
	
	enum ResType {
	  LOG,STRING,MAIL  
	}
	
    IEnvVar getEnvString(String name, ResType rType, boolean throwerror);

}
