/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.gwtmodel.table.IConsts;
import com.jythonui.client.IUIConsts;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class ParseJ {

	private ParseJ() {

	}

	public static RowIndex constructProp() {
		List<FieldItem> li = new ArrayList<FieldItem>();
		FieldItem f = new FieldItem();
		f.setId(IUIConsts.PROPVISIBLE);
		f.setAttr(ICommonConsts.TYPE, ICommonConsts.BOOLTYPE);
		li.add(f);
		f = new FieldItem();
		f.setId(IUIConsts.PROPID);
		f.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
		li.add(f);
		f = new FieldItem();
		f.setId(IUIConsts.PROPCOLUMNNAME);
		f.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
		li.add(f);
		return new RowIndex(li);
	}

	private static ListOfRows toS(RowIndex rI, String s) {
		ListOfRows r = new ListOfRows();

		JSONObject jo = (JSONObject) JSONParser.parseStrict(s);
		JSONArray a = (JSONArray) jo.get(IConsts.JSONROWLIST);
		for (int i = 0; i < a.size(); i++) {
			RowContent ro = rI.constructRow();
			JSONObject val = (JSONObject) a.get(i);
			for (FieldItem fi : rI.getColList()) {
				JSONValue v = val.get(fi.getId());
				FieldValue vall = new FieldValue();
				if (v.isNull() == JSONNull.getInstance())
					vall.setValue(fi.getFieldType(), null, fi.getAfterDot());
				else
					switch (fi.getFieldType()) {
					case BOOLEAN:
						JSONBoolean b = (JSONBoolean) v;
						vall.setValue(b.booleanValue());
						break;
					case DATE:
						break;
					case BIGDECIMAL:
						JSONNumber jn = (JSONNumber) v;
						vall.setValue(new BigDecimal(jn.doubleValue()),
								fi.getAfterDot());
						break;
					case INT:
						JSONNumber ji = (JSONNumber) v;
						vall.setValue(new Double(ji.doubleValue()).intValue());
						break;
					case LONG:
						JSONNumber jl = (JSONNumber) v;
						vall.setValue(new Double(jl.doubleValue()).longValue());
						break;
					default:
						JSONString js = (JSONString) v;
						vall.setValue(js.stringValue());
						break;
					}
				rI.setRowField(ro, fi.getId(), vall);
			}
			r.addRow(ro);
		}

		return r;
	}

	public static ListOfRows toProp(String jSon) {
		return toS(constructProp(), jSon);
	}
}
