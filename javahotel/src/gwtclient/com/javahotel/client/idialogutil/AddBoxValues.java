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
package com.javahotel.client.idialogutil;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.ifield.ICreatedValue;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddBoxValues {

	private static class R implements RData.IVectorList {

		private final IField f;
		private final ICreatedValue iV;
		private final IValueLB e;

		R(final IField f, final ICreatedValue iV, final IValueLB e) {
			this.f = f;
			this.iV = iV;
			this.e = e;
		}

		public void doVList(final List<? extends AbstractTo> val) {
			List<String> li = new ArrayList<String>();
			String firstS = null;
			for (final AbstractTo a : val) {
				Object o = a.getF(f);
				String s = (String) o;
				li.add(s);
				if (firstS == null) {
					firstS = s;
				}
			}
			String be = e.getBeforeVal();
			String av = e.getVal();
			e.setList(li);
			if (be != null) {
				e.setVal(be);
			} else {
				if ((av == null) && (firstS != null)) {
					e.setVal(firstS);
				}
			}
			if (iV != null) {
				iV.createdSignal(e);
			}

		}
	}

	public static void addValues(final IResLocator rI, final RType r,
			final CommandParam p, final IField f, final ICreatedValue iV,
			final IValueLB e) {
		rI.getR().getList(r, p, new R(f, iV, e));
	}

	public static void addValues(final IResLocator rI, final IField f,
			final ICreatedValue iV, final IValueLB e,
			final List<? extends AbstractTo> col) {
		R r = new R(f, iV, e);
		r.doVList(col);
	}
}
