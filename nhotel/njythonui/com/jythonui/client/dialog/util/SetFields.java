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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.common.TT;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.util.U;
import com.jythonui.client.var.ISetJythonVariables;
import com.jythonui.client.var.JythonVariables;
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
		if (i != null)
			return i;
		return DialogFormat.findE(iR.getDynamicList(), id);
	}

	private static class R {
		IReadDialog iR;
		Widget w;
	}

	private static R getIRForField(String fie) {
		List<ISetJythonVariables> stack = new ArrayList<ISetJythonVariables>();
		stack.addAll(JythonVariables.getS());
		Collections.reverse(stack); // from last to first
		R r = new R();
		for (ISetJythonVariables i : stack) {
			r.iR = U.getIDialog(i.getBus());
			r.w = PolymerUtil.findWidgetByFieldId(r.iR.getH(), fie);
			if (r.w != null)
				return r;
		}
		return null;
	}

	public static void setV(IEventBus iBus, DialogVariables va) {

		visitListOfFields(va, ICommonConsts.JCOPY, (fie, field) -> {

			R r = getIRForField(fie);
			if (r == null) {
				String dName = U.getIDialog(JythonVariables.getS().peek().getBus()).getD().getDialog().getId();
				Utils.errAlert(M.M().DialogField(dName, fie), M.M().CannotWindWidget());
			}
			String dialogName = r.iR.getD().getDialog().getId();
			FieldItem def = getDef(r.iR, fie);
			BiWidget bi = new BiWidget(iBus, r.w, fie, def);
			TT t = bi.getWidgetType();
			if (t == null)
				Utils.errAlertB(M.M().DialogField(dialogName, field),
						M.M().WidgetTypeNotImplemented(r.w.getClass().getName()));
			FieldValue val = va.getValue(fie);
			assert val != null;

			// try to specify type
			int afterdot = 0;
			if (def == null) {
				String dType = null;
				switch (t) {
				case STRING:
					switch (val.getType()) {
					case STRING:
						dType = ICommonConsts.STRINGTYPE;
						break;
					case LONG:
						dType = ICommonConsts.LONGTYPE;
						break;
					case INT:
						dType = ICommonConsts.INTTYPE;
						break;
					case BIGDECIMAL:
						afterdot = ICommonConsts.DEFAULTAFTERDOT;
						dType = ICommonConsts.DECIMALTYPE;
						break;
					default:
						break;
					}
					break;
				case DATE:
					if (val.getType() == TT.DATE)
						dType = ICommonConsts.DATETYPE;
					break;
				case BOOLEAN:
					if (val.getType() == TT.BOOLEAN)
						dType = ICommonConsts.BOOLTYPE;
					break;
				}
				if (dType == null)
					Utils.errAlertB(M.M().DialogField(dialogName, field),
							M.M().WidgetTypeAndValueDoesNotMatch(r.w.getClass().getName(), val.getType().name()));

				def = new FieldItem();
				def.setId(fie);
				def.setAttr(ICommonConsts.TYPE, dType);
				def.setAttr(ICommonConsts.AFTERDOT, Integer.toString(afterdot));
				// update dynamic list (once only)
				r.iR.getDynamicList().add(def);
			}

			if (def.getFieldType() != val.getType())
				Utils.errAlertB(M.M().DialogField(dialogName, field),
						M.M().FieldDefinitionValueNotMatch(def.getFieldType().name(), val.getType().name()));

			bi.setValue(r.iR, val, def);
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
