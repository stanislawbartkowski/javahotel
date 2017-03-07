/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.polymerui.client.view.util;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.TT;
import com.jythonui.client.M;
import com.jythonui.shared.FieldValue;
import com.polymerui.client.util.Utils;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperInput;

public class SetWValue {

	private SetWValue() {

	}

	public static void setVal(String dialogName, HTMLPanel ha, String fieldid, String vals, FieldValue val) {
		// PaperInput
		Widget w = PolymerUtil.findandverifyWidget(dialogName, ha, fieldid, PaperInput.class, PaperCheckbox.class);
		if (w instanceof PaperInput) {
			PaperInput p = (PaperInput) w;
			p.setValue(vals);
			return;
		}
		if (w instanceof PaperCheckbox) {
			if (val.getValue() == null) Utils.errAlertB(dialogName, M.M().BooleanValueCannotBeNull()) ;
			if (val.getType() != TT.BOOLEAN) Utils.errAlertB(dialogName,M.M().ShouldBeBooleanValue(vals));
			PaperCheckbox c = (PaperCheckbox) w;
			if (val != null) c.setChecked(val.getValueB());
		}

	}

}
