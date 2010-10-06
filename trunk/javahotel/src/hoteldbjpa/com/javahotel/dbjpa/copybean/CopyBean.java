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
package com.javahotel.dbjpa.copybean;

import com.javahotel.dbutil.log.GetLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CopyBean {

	private CopyBean() {
	}

	public static Object createI(final Class<?> cla, final GetLogger log) {
		try {
			Constructor<?> c = cla.getConstructor(new Class[] {});
			Object t = c.newInstance(new Object[] {});
			return t;
		} catch (NoSuchMethodException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (SecurityException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (InstantiationException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (IllegalAccessException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (IllegalArgumentException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		} catch (InvocationTargetException ex) {
			log.getL().log(Level.SEVERE,"",ex);
		}
		return null;
	}

	private static class copyGet extends AbstractGetField {

		@Override
		protected Object getVal(final Object sou, final Object dest,
				final Method m) throws IllegalAccessException,
				IllegalArgumentException, InvocationTargetException {
			Object val = m.invoke(sou, new Object[] {});
			return val;
		}
	}

	public static void copyBean(final Object sou, final Object dest,
			final GetLogger log, final String[] fList, final String[] omitFields) {
		copyGet co = new copyGet();

		for (int i = 0; i < fList.length; i++) {
			try {
				String na = fList[i];
				if (omitFields != null) {
					boolean onlist = false;
					for (int ii = 0; ii < omitFields.length; ii++) {
						if (na.equals(omitFields[ii])) {
							onlist = true;
							break;
						}
					}
					if (onlist) {
						continue;
					}

				}
				co.setField(na, sou, dest);
			} catch (IllegalAccessException ex) {
				log.getL().log(Level.SEVERE,"",ex);
			} catch (IllegalArgumentException ex) {
				log.getL().log(Level.SEVERE,"",ex);
			} catch (InvocationTargetException ex) {
				log.getL().log(Level.SEVERE,"",ex);
			} catch (NoSuchMethodException ex) {
				log.getL().log(Level.SEVERE,"",ex);
			} catch (SecurityException ex) {
				log.getL().log(Level.SEVERE,"",ex);
			}

		} // for
	}

	public static void copyBean(final Object sou, final Object dest,
			final GetLogger log, final String[] fList) {
		copyBean(sou, dest, log, fList, null);
	}
}
