package com.javahotel.commoncache;
/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;

@SuppressWarnings("serial")
public class CollCache<T> implements Serializable {

	private class C extends HashMap<String, T> {
	}

	public CollCache(String cKey) {
		this.iKey = cKey + "--INC";
		this.cKey = cKey;
	}

	private final String iKey;
	private final String cKey;

	private C getC() {
		C c = (C) HotelCache.get(cKey);
		if (c == null) {
			c = new C();
			HotelCache.put(cKey, c);
		}
		return c;
	}

	public T get(String key) {
		C c = getC();
		return c.get(key);
	}

	public T getE(String key) {
		C c = getC();
		T t = c.get(key);
		if (t == null) {
			HotelCache.lo.getL().log(Level.SEVERE, "", new HotelCacheEntryNotFound(key
					+ " entry not found"));
		}
		return t;
	}

	private abstract class A {
		abstract void action(C c);
	}

	private void atomicAction(A a) {
		while (true) {
			long l = HotelCache.inc(iKey, true);
			if (l == 1) {
				break;
			}
			HotelCache.inc(iKey, false);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		C c = getC();
		a.action(c);
		HotelCache.put(cKey, c);
		HotelCache.inc(iKey, false);
	}

	public void addT(final String key, final T t) {
		A a = new A() {

			@Override
			void action(C c) {
				c.put(key, t);
			}
		};
		atomicAction(a);
	}

	public void removeT(final String key) {
		A a = new A() {

			@Override
			void action(C c) {
				c.remove(key);
			}
		};
		atomicAction(a);
	}
	
	public void clearT() {
		A a = new A() {

			@Override
			void action(C c) {
				c.clear();
			}
		};
		atomicAction(a);		
	}
	
	public boolean EmptyT() {
		C c = (C) HotelCache.get(cKey);
		if (c == null) { return true; }
		return c.size() == 0;
	}

}
