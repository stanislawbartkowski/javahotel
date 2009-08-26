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
package com.javahotel.dbjpa.copybean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class AbstractGetField {

	protected abstract Object getVal(final Object sou, final Object dest,
			final Method m) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	public void setField(final String na, final Object sou, final Object dest)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String naf = GetFieldHelper.getF(na);
		Method m = GetFieldHelper.getMe(sou, naf);

		Object val = getVal(sou, dest, m);

		GetFieldHelper.setVal(dest, val, na, m);

	}
}
