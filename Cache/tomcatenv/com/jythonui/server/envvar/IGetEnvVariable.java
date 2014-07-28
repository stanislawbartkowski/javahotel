package com.jythonui.server.envvar;


public interface IGetEnvVariable {
	
	interface IEnvVar {
		boolean getL();
		String getS();
		boolean isEmpty();
	}
	
    IEnvVar getEnvString(String name, boolean logVal, boolean throwerror);

}
