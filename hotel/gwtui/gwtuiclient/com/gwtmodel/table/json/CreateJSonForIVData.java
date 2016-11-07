/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.json;

import java.math.BigDecimal;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;

public class CreateJSonForIVData implements IJsonConvert {

	private String construct(IVModelData line, String oName) {
		CreateJson s = new CreateJson(oName);
		for (IVField f : line.getF()) {
			boolean number = false;
			String val = null;
			switch (f.getType().getType()) {
			case BIGDECIMAL:
			case LONG:
			case INT:
				number = true;
				break;
			case BOOLEAN:
				Boolean b = (Boolean) line.getF(f);
				if (b == null)
					val = IConsts.JSNULL;
				else if (b.booleanValue())
					val = IConsts.JSTRUE;
				else
					val = IConsts.JSFALSE;
				number = true;
				break;
			default:
				break;
			}
			String name = f.getId();
			if (val == null)
				val = FUtils.getValueS(line, f);
			s.addElem(name, val, number);
		}
		return s.createJsonString();
	}

	@Override
	public String construct(IVModelData line) {
		return construct(line, IConsts.DEFJSONROW);
	}

	private JSONObject toS(IVModelData v) {
		JSONObject j = new JSONObject();
		for (IVField f : v.getF()) {
			JSONValue va;
			Object vval = FUtils.getValue(v, f);
			String sval = FUtils.getValueOS(vval, f);
			if (vval == null)
				va = JSONNull.getInstance();
			else {
				switch (f.getType().getType()) {
				case BIGDECIMAL:
					BigDecimal bb = (BigDecimal) vval;
					va = new JSONNumber(bb.doubleValue());
					break;
				case LONG:
					Long ll = (Long) vval;
					va = new JSONNumber(ll);
					break;
				case INT:
					Integer ii = (Integer) vval;
					va = new JSONNumber(ii);
					break;
				case BOOLEAN:
					Boolean bo = (Boolean) vval;
					va = JSONBoolean.getInstance(bo);
					break;
				default:
					// String ss = (String) vval;
					va = new JSONString(sval);
					break;
				}
			}
			String name = f.getId();
			j.put(name, va);
		}
		return j;
	}

	@Override
	public String construct(IDataListType d) {
		JSONObject responseObj = new JSONObject();
		JSONArray a = new JSONArray();
		for (int i = 0; i < d.getList().size(); i++)
			a.set(i, toS(d.getList().get(i)));
		responseObj.put(IConsts.JSONROWLIST, a);
		return responseObj.toString();
	}
}
