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
package com.javahotel.webhelper;

import java.util.List;
import java.util.Map;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.dbutil.log.GetLogger;
import com.javahotel.dbutil.prop.ReadProperties;
import com.javahotel.loginhelper.HotelServiceLocator;
import com.javahotel.remoteinterfaces.HotelServerType;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.ISecurity;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelHelper {

	private static final IList iL;
	//private static final HotelServerType t = HotelServerType.GLASSFISH;
	// private static final HotelServerType t = HotelServerType.JBOSS;
	private static final HotelServerType t = HotelServerType.APPENGINE;
	// private static final HotelServerType t = HotelServerType.TOMCAT;
	private static ISecurity sec;
	private static IAuthentication aut;
	private static IHotelData hot;
	private static IHotelOp hop;
	private static GetLogger lo = new GetLogger("com.javahotel.webgwt");
	private static final String ADMINDB = "admindb";
	private static final String PASSWORDDB = "passworddb";
	private static final String SESSIONDB = "SESSIONDB-CREATE";

	private static String getPort() {
		switch (t) {
		case TOMCAT:
			return "8084";
			// return "8080";
		case GLASSFISH:
//			return "3763";
			return "3700";
		case JBOSS:
			return "1099";

		}
		return "";
	}

	private static String getHost() {
		switch (t) {
		case TOMCAT:
			// return "192.168.1.3";
			// return "192.168.1.2";
			return "localhost";
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
		default:
			break;
		}
		return "";
	}

	private static String getPropName() {
		switch (t) {
		case GLASSFISH:
		case JBOSS:
		case TOMCAT:
		case APPENGINE:
			return "helperconfig.properties";
		}
		return "";
	}

	private static void setProp() {
		Map<String, String> p = ReadProperties.getRProperties(getPropName(), lo);
		sec.setNewProperties(null, p);
	}

	abstract static private class RunAsAdminTemplate {

		protected SessionT adminSe = null;

		abstract protected void doadmin();

		void run() {
			adminSe = sec.loginadminSession(SESSIONDB, ADMINDB, new PasswordT(
					PASSWORDDB));
			doadmin();
			sec.logoutSession(adminSe);
		}
	}

	private static class setDBClass extends RunAsAdminTemplate {

		@Override
		protected void doadmin() {
			Map<String, String> p = ReadProperties.getRProperties(getName(), lo);
			sec.setDatabaseDefinition(adminSe, p);
		}
	}

	private static class GetIList extends RunAsAdminTemplate {

		private final RType r;
		private final CommandParam p;
		List<AbstractTo> out;

		GetIList(RType r, final CommandParam p) {
			this.r = r;
			this.p = p;
		}

		@Override
		protected void doadmin() {
			out = iL.getList(adminSe, r, p);
		}
	}

	static void setDB() {
		setDBClass se = new setDBClass();
		se.run();
	}

	static {
		HotelServiceLocator hLoc = new HotelServiceLocator(t, getHost(),
				getPort());
		HelperResources.info(HelperResources.STARTINIT);
		HelperResources.info(HelperResources.GETLISTBEAN);
		iL = hLoc.getListBean();
		sec = hLoc.getSecurityBean();
		aut = hLoc.getAuthenticationBean();
		setProp();
		setDB();
		hot = hLoc.getHotelBean();
		hop = hLoc.getHotelOpBean();

	}

	private HotelHelper() {
	}

	public static void loginAdmin(final String sessionId, final String user,
			final String password) {
		SessionT se = sec.loginadminSession(sessionId, user, new PasswordT(
				password));
		// always return nor null
		SessionCache.addSession(sessionId, se);
	}

	public static void loginUser(final String sessionId, final String user,
			final String password) {
		SessionT se = sec
				.loginSession(sessionId, user, new PasswordT(password));
		// always return nor null
		SessionCache.addSession(sessionId, se);
	}

	public static void logOut(final String sessionId) {
		SessionT se = SessionCache.getSessionT(sessionId);
		sec.logoutSession(se);
		SessionCache.removeSession(sessionId);
	}

	public static List<AbstractTo> getList(final String sessionId,
			final RType r, final CommandParam p) {
		if (r == RType.AllHotels) {
			GetIList ilist = new GetIList(r, p);
			ilist.run();
			return ilist.out;
		}
		SessionT se = SessionCache.getSessionT(sessionId);
		return iL.getList(se, r, p);
	}

	public static AbstractTo getOne(final String sessionId, final RType r,
			final CommandParam p) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return iL.getOne(se, r, p);
	}

	public static void addHotel(final String sessionId, final HotelP hotel) {
		SessionT se = SessionCache.getSessionT(sessionId);
		aut.persistHotel(se, new HotelT(hotel.getName()), hotel
				.getDescription(), hotel.getDatabase());
	}

	public static void removeHotel(final String sessionId, final HotelP hotel) {
		SessionT se = SessionCache.getSessionT(sessionId);
		aut.removeHotel(se, new HotelT(hotel.getName()));
	}

	public static void addPerson(final String sessionId, final PersonP person,
			final String password) {
		SessionT se = SessionCache.getSessionT(sessionId);
		aut.persistPerson(se, person.getName(), new PasswordT(password));
	}

	public static void removePerson(final String sessionId, final PersonP person) {
		SessionT se = SessionCache.getSessionT(sessionId);
		aut.removePerson(se, person.getName());
	}

	public static void setRoles(final String sessionId, final String person,
			final String hotel, final List<String> roles) {
		SessionT se = SessionCache.getSessionT(sessionId);
		aut.persistPersonHotel(se, person, new HotelT(hotel), roles);
	}

	public static void persistDict(final String sessionId, final DictType d,
			final DictionaryP dp) {
		SessionT se = SessionCache.getSessionT(sessionId);
		hot.persistDic(se, d, dp);
	}

	public static ReturnPersist persistDictRet(final String sessionId,
			final DictType d, final DictionaryP dp) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hot.persistDicReturn(se, d, dp);
	}

	public static ReturnPersist persistResBookingReturn(final String sessionId,
			final BookingP dp) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hot.persistResBookingReturn(se, dp);
	}

	public static void removeDict(final String sessionId, final DictType d,
			final DictionaryP dp) {
		SessionT se = SessionCache.getSessionT(sessionId);
		hot.removeDic(se, d, dp);
	}

	public static void hotelOp(final String sessionId, final HotelOpType op,
			final CommandParam p) {
		SessionT se = SessionCache.getSessionT(sessionId);
		hop.hotelOp(se, op, p);
	}

	public static ReturnPersist hotelOpRet(final String sessionId,
			final HotelOpType op, final CommandParam p) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hop.hotelOpRet(se, op, p);
	}

	public static ReturnPersist hotelOpRet(final String sessionId,
			final CommandParam p) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hop.hotelOpRet(se, p);
	}

	public static List<ReturnPersist> hotelOpRet(final String sessionId,
			final List<CommandParam> p) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hop.hotelOpRet(se, p);
	}

	public static void clearHotelData(final String sessionId, HotelP hotel) {
		SessionT se = SessionCache.getSessionT(sessionId);
		hot.clearHotelBase(se, new HotelT(hotel.getName()));
	}

	public static ReturnPersist testHotelPersist(final String sessionId,
			PersistType t, HotelP hotel) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return aut.testPersistHotel(se, t, hotel);
	}

	public static ReturnPersist testPersonPersist(final String sessionId,
			PersistType t, PersonP person) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return aut.testPersistPerson(se, t, person);
	}
	
	public static ReturnPersist testDictPersist(final String sessionId,
			PersistType t, DictType da, DictionaryP a) {
		SessionT se = SessionCache.getSessionT(sessionId);
		return hot.testDicPersist(se, t, da, a);
	}

	public static Map<String, String> getParam() {
		String preFix = "unknown";
		switch (t) {
		case TOMCAT:
			preFix = "tomcat";
			break;
		case GLASSFISH:
			preFix = "glassfish";
			break;
		case JBOSS:
			preFix = "jboss";
			break;
		case APPENGINE:
			preFix = "appengine";
			break;
		default:
			break;
		}
		Map<String, String> ma = ReadProperties.getProperties(
				ReadProperties.getResourceName("reportcall.properties"), preFix, lo);
		return ma;
	}
}
