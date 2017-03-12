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
package com.jythonui.client.dialog.util;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.TT;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.util.U;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.util.Utils;
import com.polymerui.client.view.util.PolymerUtil;

public class SetFields {

	private SetFields() {

	}

	static FieldItem getDef(IReadDialog iR, String id) {
		FieldItem i = iR.getD().getDialog().findFieldItem(id);
		if (i == null)
			return i;
		return DialogFormat.findE(iR.getDynamicList(), id);
	}

	public static void setV(DialogVariables va, IEventBus iBus) {

		IReadDialog iR = U.getIDialog(iBus);
		String dialogName = iR.getD().getDialog().getId();

		visitListOfFields(va, ICommonConsts.JCOPY, (fie, field) -> {

			FieldItem def = getDef(iR,fie);
			Widget w = PolymerUtil.findWidgetByFieldId(iR.getH(),fie);
			if (w == null) Utils.errAlert(M.M().DialogField(dialogName, fie),M.M().CannotWindWidget());			
			BiWidget bi = new BiWidget(w,fie);			
			TT t = bi.getWidgetType();
			if (t == null) Utils.errAlertB(M.M().DialogField(dialogName, field),M.M().WidgetTypeNotImplemented(w.getClass().getName()));
			FieldValue val = va.getValue(fie);
			assert val != null;
			
			// try to specify type
			if (def == null) {
				String dType = null;
				switch (t) {
				case STRING:
					switch (val.getType()) {
					case STRING : dType = ICommonConsts.STRINGTYPE; break;
					case LONG : dType = ICommonConsts.LONGTYPE; break;
					case INT: dType = ICommonConsts.INTTYPE; break;
					case BIGDECIMAL: dType = ICommonConsts.DECIMALTYPE; break;
					default : break;
					}
					break;
				case DATE:
					if (val.getType() == TT.DATE) dType = ICommonConsts.DATETYPE;
					break;
				case BOOLEAN:
					if (val.getType() == TT.BOOLEAN) dType = ICommonConsts.BOOLTYPE;
					break;
				}
				if (dType == null) 
					Utils.errAlertB(M.M().DialogField(dialogName, field),M.M().WidgetTypeAndValueDoesNotMatch(w.getClass().getName(), val.getType().name()));
				
				def = new FieldItem();
				def.setId(fie);
				def.setAttr(ICommonConsts.TYPE,dType);
				def.setAttr(ICommonConsts.AFTERDOT,Integer.toString(ICommonConsts.DEFAULTAFTERDOT));
				// update dynamic list (once only)
				iR.getDynamicList().add(def);
			}
			
			if (def.getFieldType() != val.getType())
				Utils.errAlertB(M.M().DialogField(dialogName, field),M.M().FieldDefinitionValueNotMatch(def.getFieldType().name(),val.getType().name()));
						
			bi.setValue(iR, val, def);
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
