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

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.polymerui.client.IConsts;
import com.polymerui.client.eventbus.CloseDialogEvent;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.util.Utils;

public class RunAction {

	private RunAction() {

	}

	private static String getValueS(DialogVariables arg, String key) {
		FieldValue val = arg.getValue(key);
		if (val == null)
			return null;
		if (val.getType() == TT.STRING)
			return arg.getValueS(key);
		String mess = M.M().ValueShouldBeString(key, val.getType().toString());
		Utils.errAlert(mess);
		return null;
	}

	static public void action(IEventBus iBus, DialogVariables arg) {
		// it a little tricky but this way allows code reuse with performAction
		String[] kom = { ICommonConsts.JMAINDIALOG, ICommonConsts.JUPDIALOG, ICommonConsts.JOKMESSAGE,
				ICommonConsts.JERRORMESSAGE, ICommonConsts.JYESNOMESSAGE, ICommonConsts.JSUBMIT,
				ICommonConsts.JURL_OPEN, ICommonConsts.JEXECUTEACTION, ICommonConsts.JCLOSEDIALOG };
		String[] param = { null, IConsts.JBUTTONDIALOGSTART, ICommonConsts.JMESSAGE_TITLE, ICommonConsts.JMESSAGE_TITLE,
				ICommonConsts.JMESSAGE_TITLE, null, ICommonConsts.JMESSAGE_TITLE, null, ICommonConsts.JCLOSEBUTTON };
		String[] param2 = { null, ICommonConsts.JAFTERDIALOGACTION, ICommonConsts.JAFTERDIALOGACTION,
				ICommonConsts.JAFTERDIALOGACTION, ICommonConsts.JAFTERDIALOGACTION, null, null,
				IConsts.JBUTTONDIALOGRES, null };
		String[] param3 = { null, IConsts.JBUTTONDIALOGSTART1, null, null, null, null, null, null, null };
		assert (kom.length == param.length);
		assert (kom.length == param2.length);
		assert (kom.length == param3.length);
		for (int i = 0; i < kom.length; i++) {
			FieldValue val = arg.getValue(kom[i]);
			String[] pars = { null, null, null, null };
			if (val == null)
				continue;
			if (val.getType() == TT.STRING)
				pars[0] = val.getValueS();
			if (param[i] != null)
				pars[1] = getValueS(arg, param[i]);

			if (param2[i] != null)
				pars[2] = getValueS(arg, param2[i]);

			if (param3[i] != null)
				pars[3] = getValueS(arg, param3[i]);

			performAction(iBus, kom[i], pars);
		}

	}

	public static void buttonAction(IEventBus iBus, ButtonItem bItem) {
		String action = bItem.getAction();
		String param = bItem.getActionParam();
		String param1 = bItem.getAttr(ICommonConsts.ACTIONPARAM1);
		String param2 = bItem.getAttr(ICommonConsts.ACTIONPARAM2);
		String param3 = bItem.getAttr(ICommonConsts.ACTIONPARAM3);
		if (CUtil.EmptyS(param3))
			param3 = bItem.getId();
		performAction(iBus, action, new String[] { param, param1, param2, param3, bItem.getDisplayName() });
	}

	private static void verifyParams(String action, String[] pars) {
		String[] requ0 = { ICommonConsts.JMAINDIALOG, ICommonConsts.JUPDIALOG };
		boolean require0 = false;
		for (String s : requ0)
			if (action.equals(s))
				require0 = true;
		if (!require0 || !CUtil.EmptyS(pars[0]))
			return;
		String mess = M.M().ActionRequiresParameter(action);
		Utils.errAlertB(mess);
	}

	private static void performAction(IEventBus iBus, String action, String[] pars) {
		verifyParams(action, pars);
		String param = null;
		String param1 = null;
		String param2 = null;
		String param3 = null;
		String displayName = null;
		if (pars != null) {
			if (pars.length > 0)
				param = pars[0];
			if (pars.length > 1)
				param1 = pars[1];
			if (pars.length > 2)
				param2 = pars[2];
			if (pars.length > 3)
				param3 = pars[3];
			if (pars.length > 4)
				displayName = pars[4];
		}
		// if (action.equals(ICommonConsts.JURL_OPEN)) {
		// Utils.openTabUrl(param, param1);
		// return;
		// }
		// if (action.equals(ICommonConsts.JLOGOUTACTION)) {
		// IWebPanel i = GwtGiniInjector.getI().getWebPanel();
		// i.logOut();
		// return;
		// }
		if (action.equals(ICommonConsts.JMAINDIALOG) || action.equals(ICommonConsts.JUPDIALOG)) {
			IReadDialog d = UIGiniInjector.getI().getReadDialog();
			d.readDialog(pars[0], displayName, action.equals(ICommonConsts.JMAINDIALOG));
			return;
		}
		// if (!checkW(action, param, param1, param2, w))
		// return;
		// if (iEx != null)
		// iEx.setAction(param2);
		// new RunAction().upDialog(param, w, iCon, iEx, param1, param3, null,
		// null);
		// return;
		// }
		if (action.equals(ICommonConsts.JOKMESSAGE) || action.equals(ICommonConsts.JERRORMESSAGE)) {

			StandardDialog.dialog(iBus, StandardDialog.OKDIALOG, pars);
			return;
		}
		if (action.equals(ICommonConsts.JYESNOMESSAGE)) {
			StandardDialog.dialog(iBus, StandardDialog.YESNODIALOG, pars);
			return;
		}
		// if (iYesno != null) {
		// if (!checkW(action, param, param1, param2, w))
		// return;
		// iYesno.answer(param, param1, param2, w);
		// }
		// return;
		// }
		// // JCloseDialog: param : resString, param1: resButton
		// if (action.equals(ICommonConsts.JCLOSEDIALOG)) {
		// if (iClose != null)
		// iClose.closeAction(param, param1);
		// return;
		// }
		if (action.equals(ICommonConsts.JCLOSEDIALOG)) {
			iBus.publish(new CloseDialogEvent(), new String[] { param, param1 });
			return;
		}
		// if (action.equals(ICommonConsts.JSUBMIT)) {
		// if (iClose != null)
		// iClose.submitAction();
		// return;
		// }
		// if (action.equals(ICommonConsts.JEXECUTEACTION)) {
		// iEx.executeFromModeless(true, param, param2);
		// return;
		// }

		Utils.errAlert(M.M().UnknownAction(action, param));
	}

}