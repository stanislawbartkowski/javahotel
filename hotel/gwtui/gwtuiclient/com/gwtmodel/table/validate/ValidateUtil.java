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
package com.gwtmodel.table.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.editw.FormField;

public class ValidateUtil {

	private ValidateUtil() {
	}

	public static List<InvalidateMess> checkEmptyRow(IVModelData mData, List<IVField> vList) {
		List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
		boolean ok = true;
		for (IVField v : vList)
			if (FUtils.isNullValue(mData, v)) {
				ok = false;
				errMess.add(new InvalidateMess(v, true, null, false));
			}

		if (ok)
			return null;
		return errMess;
	}

	public static List<InvalidateMess> checkEmpty(IVModelData mData, List<FormField> vList) {
		List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
		boolean ok = true;
		for (FormField f : vList) {
			IVField v = f.getFie();
			if (f.getELine().isInvalid()) {
				ok = false;
				errMess.add(new InvalidateMess(v, false, null, true));
				continue;
			}
			if (f.getFormProp().isNotEmpty() && FUtils.isNullValue(mData, v)) {
				ok = false;
				errMess.add(new InvalidateMess(v, true, null, false));
			}
		}
		if (ok)
			return null;
		return errMess;
	}

}
