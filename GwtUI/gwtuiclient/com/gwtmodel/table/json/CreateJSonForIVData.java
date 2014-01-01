/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.google.gwt.core.client.JsonUtils;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;

public class CreateJSonForIVData implements IJsonConvert {

	private String construct(IVModelData line, String oName) {
		CreateJson s = new CreateJson(oName);
		for (IVField f : line.getF()) {
			boolean number = false;
			switch (f.getType().getType()) {
			case BIGDECIMAL:
			case LONG:
			case INT:
				number = true;
				break;
			default:
				break;
			}
			String name = f.getId();
			String val = FUtils.getValueS(line, f);
			s.addElem(name, val, number);
		}
		return s.createJsonString();
	}

	@Override
	public String construct(IVModelData line) {
		return construct(line, IConsts.DEFJSONROW);
	}
}
