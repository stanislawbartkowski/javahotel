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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.widgets.popup.PopUpTip;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class ELineStore {

	private Collection<RecordField> el = new ArrayList<RecordField>();


	void initErr() {
		el.clear();
	}

	void setEMess(RecordField re, InvalidateMess m) {
		re.getELine().setStyleName("dialog-empty-field", true);
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
			re.getELine().setStyleName("dialog-empty-field", false);
			re.getELine().setErrMess(null);
		}
		initErr();
	}
}
