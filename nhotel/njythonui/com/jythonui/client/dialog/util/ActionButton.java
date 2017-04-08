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

import com.jythonui.client.dialog.IReadDialog;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.U;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.polymerui.client.callback.CommonCallBack;
import com.polymerui.client.eventbus.IEventBus;
import com.polymerui.client.eventbus.ResultButtonAction;

public class ActionButton {

	private ActionButton() {

	}

	public static void call(IEventBus iBus, DialogVariables v, ButtonItem b) {
		callA(iBus, v, b.getId(), b.getId());
	}

	public static void callA(IEventBus iBus, DialogVariables v, String action, String buttonAction) {
		IReadDialog iR = U.getIDialog(iBus);

		ButtonItem bI = DialogFormat.findE(iR.getD().getDialog().getAllButton(), buttonAction);

		if (bI != null && bI.isAction()) {
			RunAction.buttonAction(iBus, bI);
			return;
		}

		ExecuteAction.action(v, iR.getD().getDialog().getId(), action, new CommonCallBack<DialogVariables>() {

			@Override
			public void onMySuccess(DialogVariables arg) {
				iBus.publish(new ResultButtonAction(), arg);
			}
		});

	}

}
