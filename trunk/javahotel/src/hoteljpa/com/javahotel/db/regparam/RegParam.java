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
package com.javahotel.db.regparam;

import java.util.Collection;

import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.util.CommonHelper;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RegParam {

	private RegParam() {
	}

	public static String getParam(final ICommandContext iC, final String key,
			final String defa) {
		ParamRegistry pa = CommonHelper.getA(iC, ParamRegistry.class, key);
		if (pa == null) {
			return defa;
		}
		return pa.getDescription();
	}

	public static void setParam(final ICommandContext iC, final String key,
			final String val) {
		ParamRegistry pa = CommonHelper.getA(iC, ParamRegistry.class, key);
		if (pa == null) {
			pa = new ParamRegistry();
			RHotel h = iC.getRHotel();
			pa.setHotel(h);
			pa.setName(key);
		}
		pa.setDescription(val);
		iC.getC().put(ParamRegistry.class, key, pa,true);
	}

	public static void setParam(final ICommandContext iC, final String key,
			final int val) {
		setParam(iC, key, "" + val);
	}

	public static int getParam(final ICommandContext iC, final String key,
			final int defa) {
		String pa = getParam(iC, key, "" + defa);
		return Integer.parseInt(pa);
	}

	public static Collection<ParamRegistry> getParams(final ICommandContext iC,
			final String keylike) {
		Collection<ParamRegistry> col = GetQueries.getRegistryEntries(iC,
				keylike);
		return col;

	}
}
