/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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
package com.javahotel.test;

import java.util.Map;

import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.ReadProperties;
import com.javahotel.loginhelper.HotelServiceLocator;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.ISecurity;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class TestUtil {
//        private static final HotelServerType t = HotelServerType.GLASSFISH;
	private static final HotelServerType t = HotelServerType.APPENGINE;
//	private static final HotelServerType t = HotelServerType.JBOSS;
//	private static final HotelServerType t = HotelServerType.TOMCAT;
	private static final boolean remote = true;
//	private static String LOCALBEANGLASS = "securityTestEJB";

	private static String getPort() {
		switch (t) {
		case TOMCAT:
			return "8084";
			// return "8080";
			// case GLASSFISH: return "13385";
		case GLASSFISH:
			return "3700";
		case JBOSS:
			return "1099";
		}
		return "";
	}

	private static String getHost() {
		switch (t) {
		case TOMCAT:
			return "localhost";
			// return "192.168.1.3";
			// return "192.168.1.2";
			// case TOMCAT: return "192.168.10.96";
			// case TOMCAT: return "192.168.1.5";
		case GLASSFISH:
			return "localhost";
		case JBOSS:
			return "localhost";
		}
		return null;
	}

	private static String getName() {
		switch (t) {
		case TOMCAT:
			return "tomcatpers.properties";
		case GLASSFISH:
			return "glassfishpers.properties";
		case JBOSS:
			return "jbosspers.properties";
		case APPENGINE:
			return "appengine.properties";
		}
		return "";
	}

	private static String getPropName() {
		switch (t) {
		case TOMCAT:
		case GLASSFISH:
		case JBOSS:
		case APPENGINE:
			return "loginconfig.properties";
		}
		return "";

	}

	private static ISecurity getLocalSecBean() {
		// ISecurity sec = (ISecurity) HotelGetBeanHelper.getBean(t,
		// LOCALBEANGLASS, getHost(), getPort());
		// return sec;
		return null;
	}

	static ISecurity getSe() {
		ISecurity sec;
		if (remote) {
			HotelServiceLocator h = new HotelServiceLocator(t, getHost(),
					getPort());
			sec = h.getSecurityBean();
		} else {
			sec = getLocalSecBean();
		}
		return sec;
	}

	static IAuthentication getAu() {
		HotelServiceLocator h = new HotelServiceLocator(t, getHost(), getPort());
		IAuthentication sec = h.getAuthenticationBean();
		return sec;
	}

	static IList getList() {
		HotelServiceLocator h = new HotelServiceLocator(t, getHost(), getPort());
		IList sec = h.getListBean();
		return sec;
	}

	static IHotelTest getHotelTest() {
		HotelServiceLocator h = new HotelServiceLocator(t, getHost(), getPort());
		IHotelTest sec = h.getHotelTestBean();
		return sec;

	}

	static IHotelData getHotel() {
		HotelServiceLocator h = new HotelServiceLocator(t, getHost(), getPort());
		IHotelData sec = h.getHotelBean();
		return sec;

	}

	static IHotelOp getHotelOp() {
		HotelServiceLocator h = new HotelServiceLocator(t, getHost(), getPort());
		IHotelOp sec = h.getHotelOpBean();
		return sec;

	}

	static void propStart(final SessionT se, ISecurity sec) {
		GetLogger lo = new GetLogger("com.javahotel.javatest");
		Map<String, String> p = ReadProperties
				.getRProperties(getPropName(), lo);
		sec.setNewProperties(se, p);
	}

	static void testStart(final SessionT se, ISecurity sec) {
		GetLogger lo = new GetLogger("com.javahotel.javatest");
		Map<String, String> p = ReadProperties.getRProperties(getName(), lo);
		sec.setDatabaseDefinition(se, p);
	}

	static void testStop() {
	}
}
