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
package com.jythonui.client.dialog.impl;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.common.ConvertTT;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.view.util.SetWValue;

class SetFields {

	private SetFields() {

	}

	static void setV(HTMLPanel ha, DialogInfo arg, DialogVariables va) {

		visitListOfFields(va, ICommonConsts.JCOPY, (fie, field) -> {
			FieldValue val = va.getValue(fie);
			assert val != null;
			FieldItem def = arg.getDialog().findFieldItem(fie);
			int afterdot = ICommonConsts.DEFAULTAFTERDOT;
			if (def != null)
				afterdot = def.getAfterDot();
			String sval = ConvertTT.toS(val.getValue(), val.getType(), afterdot);
			SetWValue.setVal(arg.getDialog().getId() + " " + field, ha, fie, sval, val);
		});

	}

	@FunctionalInterface
	public interface IVisitor {
		void action(String fie, String field);
	};

	static void visitListOfFields(DialogVariables var, String prefix, IVisitor i) {

		var.getFields().stream().filter(key -> key.startsWith(prefix)).forEach(key -> {
			String fie = key.substring(prefix.length());
			i.action(fie, key);
		});

	}

}
