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
package com.javahotel.loginhelper;

import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.ISecurity;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelServiceLocator {

	private final HotelServerType t;
	private final String serverHost;
	private final String port;
	private static final String GLASSSECURITYBEAN = "securityEJB";
	private static final String TOMCATSECURITYBEAN = "SecurityImplRemote";
	private static final String JBOSSSECURITYBEAN = "SecurityImpl/remote";
	private static final String GLASSAUTHENTICATIONBEAN = "authenticationEJB";
	private static final String TOMCATAUTHENTICATIONBEAN = "AuthenticationImplRemote";
	private static final String JBOSSAUTHENTICATIONBEAN = "AuthenticationImpl/remote";
	private static final String GLASSFISHLISTBEAN = "listEJB";
	private static final String TOMCATLISTBEAN = "ListCommandRemote";
	private static final String JBOSSLISTBEAN = "ListCommand/remote";
	private static final String GLASSFISHDATABEAN = "hotelEJB";
	private static final String TOMCATDATABEAN = "HotelDataRemote";
	private static final String JBOSSDATABEAN = "HotelData/remote";
	private static final String GLASSFISHTESTHOTEL = "hoteltestEJB";
	private static final String TOMCATTESTHOTEL = "HotelTestRemote";
	private static final String JBOSSTESTHOTEL = "HotelTest/remote";
	private static final String JBOSSOPBEAN = "HotelOp/remote";
	private static final String TOMCATOPBEAN = "HotelOpRemote";
	private static final String GLASSOPBEAN = "hotelOpEJB";
	private final boolean remote;
	private static final String GLASSECURITYLOCAL = "java:comp/env/SecurityLocal";
	private static final String GLASSAUTHENTICATIONLOCAL = "java:comp/env/AuthenticationLocal";
	private static final String GLASSLISTLOCAL = "java:comp/env/ListLocal";
	private static final String GLASSHOTELLOCAL = "java:comp/env/HotelDataLocal";

	public HotelServiceLocator(final HotelServerType t,
			final String serverHost, final String port) {
		this.t = t;
		this.serverHost = serverHost;
		this.port = port;
		remote = true;
	}

	public HotelServiceLocator(final HotelServerType t) {
		this.t = t;
		this.serverHost = null;
		this.port = null;
		remote = false;
	}

	private String getSecName() {
		switch (t) {
		case TOMCAT:
			return TOMCATSECURITYBEAN;
		case GLASSFISH:
			return GLASSSECURITYBEAN;
		case JBOSS:
			return JBOSSSECURITYBEAN;
		}
		return null;
	}

	private String getAuthName() {
		switch (t) {
		case TOMCAT:
			return TOMCATAUTHENTICATIONBEAN;
		case GLASSFISH:
			return GLASSAUTHENTICATIONBEAN;
		case JBOSS:
			return JBOSSAUTHENTICATIONBEAN;
		}
		return null;
	}

	private String getTestName() {
		switch (t) {
		case TOMCAT:
			return TOMCATTESTHOTEL;
		case GLASSFISH:
			return GLASSFISHTESTHOTEL;
		case JBOSS:
			return JBOSSTESTHOTEL;
		}
		return null;

	}

	private String getListName() {
		switch (t) {
		case GLASSFISH:
			return GLASSFISHLISTBEAN;
		case TOMCAT:
			return TOMCATLISTBEAN;
		case JBOSS:
			return JBOSSLISTBEAN;
		}
		return null;
	}

	private String getHotelName() {
		switch (t) {
		case GLASSFISH:
			return GLASSFISHDATABEAN;
		case TOMCAT:
			return TOMCATDATABEAN;
		case JBOSS:
			return JBOSSDATABEAN;
		}
		return null;
	}

	private String getOpName() {
		switch (t) {
		case GLASSFISH:
			return GLASSOPBEAN;
		case TOMCAT:
			return TOMCATOPBEAN;
		case JBOSS:
			return JBOSSOPBEAN;
		}
		return null;
	}

	private String getBName(BeanName na) {
		switch (na) {
		case SecName:
			return getSecName();
		case AutName:
			return getAuthName();
		case HotelName:
			return getHotelName();
		case TestName:
			return getTestName();
		case ListName:
			return getListName();
		case HotelOpName:
			return getOpName();
		}
		return null;
	}

	private String getBeanLocalName(BeanName na) {
		return null;
	}

	private <T> T getBean(BeanName na) {
		if (remote) {
			Object o = HotelGetBeanHelper.getBean(t, getBName(na), serverHost,
					port);
			return (T) o;
		} else {
			try {
				InitialContext ic = new InitialContext();
				T bean = (T) ic.lookup(getBeanLocalName(na));
				return bean;
			} catch (NamingException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public ISecurity getSecurityBean() {
		return getBean(BeanName.SecName);
	}

	public IAuthentication getAuthenticationBean() {
		return getBean(BeanName.AutName);
	}

	public IList getListBean() {
		return getBean(BeanName.ListName);
	}

	public IHotelTest getHotelTestBean() {
		return getBean(BeanName.TestName);
	}

	public IHotelData getHotelBean() {
		return getBean(BeanName.HotelName);
	}

	public IHotelOp getHotelOpBean() {
		return getBean(BeanName.HotelOpName);
	}
}
