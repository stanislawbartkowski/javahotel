/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbres.resources;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;

import com.javahotel.dbres.log.HLog;
import com.javahotel.dbutil.prop.ReadProperties;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetProp {

	static final private String CONF = IMess.RESOURCEFOLDER
			+ "/config.properties";
	private static final Map<String, String> confP;
	private static final String seID;
	private static final String ROOTTAGNAME = "RootTagName";

	static {
		confP = ReadProperties.getProperties(CONF, HLog.getL());
//		String na = confP.get(IMess.PUPREFIX);
//		JpaManagerData.setPuName(na);
//		JpaManagerData.setLog(HLog.getL());
		seID = confP.get(IMess.DATABASESECURITYID);
//		JpaManagerData.setDataBaseMapping(getSeID(), IMess.PUSECURITY);
	}

	public static Map<String, String> getConfP() {
		return confP;
	}

	public static String getSeID() {
		return seID;
	}

	private GetProp() {
	}

	public static void setNewProp(final Map<String, String> p) {
		for (final String s : p.keySet()) {
			String val = p.get(s);
			confP.put(s, val);
		}
	}

	public static String getProp(final String prop) {
		String val = confP.get(prop);
		return val;
	}

	public static String getRootTagName(final String prop) {
		String val = confP.get(prop);
		String forma = confP.get(ROOTTAGNAME);
		return MessageFormat.format(forma, new Object[] { val });
	}
	
	public static String getResourceName(String name) {
        String fName = IMess.RESOURCEFOLDER + "/" + name;
        return fName;	    
	}

	public static InputStream getXMLFile(final String prop) {
		String xmlname = confP.get(prop);
		String fName = getResourceName(xmlname);
		InputStream i = ReadProperties.getInputStream(fName);
		return i;
	}
}
