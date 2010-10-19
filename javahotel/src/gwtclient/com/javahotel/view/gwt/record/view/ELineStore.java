/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.record.view;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.record.model.RecordField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ELineStore {

	private List<RecordField> el = new ArrayList<RecordField>();


	void initErr() {
		el.clear();
	}

	void setEMess(RecordField re, InvalidateMess m) {
		re.getELine().setGStyleName("dialog-empty-field", true);
		String e;
		if (m.isEmpty()) {
			e = "Pole nie może być puste !";
		} else {
			e = m.getErrmess();
		}
		re.getELine().setErrMess(e);
		el.add(re);
	}

	void clearE() {
		for (RecordField re : el) {
			re.getELine().setGStyleName("dialog-empty-field", false);
			re.getELine().setErrMess(null);
		}
		initErr();
	}
}
