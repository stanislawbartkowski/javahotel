package com.javahotel.commoncache;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.javahotel.dbutil.log.GetLogger;

public class HotelCache {

	private HotelCache() {
	}

	// private static Cache cache;
	private static Map<String, Object> cache;
	static final GetLogger lo = new GetLogger("com.javahotel.enginecache");

	static {
		cache = new HashMap<String, Object>();
	}

	public static Object get(String key) {
		return cache.get(key);
	}

	public static Object getE(String key) {
		Object o = cache.get(key);
		if (o == null) {
			lo.getL().log(Level.SEVERE, "", new HotelCacheEntryNotFound(key + " entry not found"));
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public static void put(String key, Object o) {
		cache.put(key, o);
	}

	public static void remove(String key) {
		cache.remove(key);
	}

	public static long inc(String iKey, boolean plus) {
		long de;
		if (plus) {
			de = 1;
		} else {
			de = -1;
		}
		Long l = (Long) cache.get(iKey);
		if (l == null) {
			cache.put(iKey, new Long(de));
			return de;
		}
		return l;
	}

}
