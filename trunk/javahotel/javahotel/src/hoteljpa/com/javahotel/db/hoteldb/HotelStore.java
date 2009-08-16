/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.hoteldb;

import java.util.Collection;

import com.javahotel.common.toobject.HotelP;
import com.javahotel.commoncache.CollCache;
import com.javahotel.db.authentication.impl.GetList;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class HotelStore {

	private static CollCache<String> ca;

	static {
		ca = new CollCache<String>(IMess.HOTELCACHEID);
	}

	public static void invalidateCache() {
		ca.clearT();
	}

	private static void checkD(final SessionT se) {
		if (ca.EmptyT()) {
			readD(se);
		}
	}

	public static boolean isHotel(final SessionT se, final HotelT ho) {
		checkD(se);
		String da = ca.get(ho.getName());
		return da != null;
	}

	public static String getDatabase(final SessionT se, final HotelT ho) {
		checkD(se);
		String da = ca.get(ho.getName());
		return da;
	}

	// private static Map<String, String> cache;
	//
	// static {
	// invalidateCache();
	// }
	//
	// private HotelStore() {
	// }
	//
	// public static void invalidateCache() {
	// cache = null;
	// }
	//
	// synchronized private static void checkCache(final SessionT se) {
	// if (cache == null) {
	// readD(se);
	// }
	// }
	//
	private static void readD(final SessionT se) {
		Collection<HotelP> ho = GetList.getHotelList(se);
		for (HotelP h : ho) {
			ca.addT(h.getName(), h.getDatabase());
		}
	}
	//
	// public static boolean isHotel(final SessionT se, final HotelT ho) {
	// checkCache(se);
	//
	// String da = cache.get(ho.getName());
	// return da != null;
	// }
	//
	// public static String getDatabase(final SessionT se, final HotelT ho) {
	//
	// checkCache(se);
	//
	// String da = cache.get(ho.getName());
	// if (da == null) {
	// HLog.failureE(IMessId.UNKNOWNHOTEL, ho.getName());
	// }
	// return da;
	// }
}
