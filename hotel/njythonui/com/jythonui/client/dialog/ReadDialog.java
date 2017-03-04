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
package com.jythonui.client.dialog;

import com.jythonui.client.M;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.shared.DialogInfo;
import com.polymerui.client.callback.CommonCallBack;
import com.polymerui.client.util.Utils;

public class ReadDialog {

	
	private void verify(DialogInfo d) {
		if (d.getDialog().getBinderW() == null) {
			Utils.errAlertB(M.M().DialogShouldContainBinder(d.getDialog().getId()));			
		}
	}
	
	private class CallB extends  CommonCallBack<DialogInfo> {

		@Override
		public void onMySuccess(DialogInfo arg) {
			verify(arg);			
		}
		
	}
	
	public void readDialog(String dialogName) {
		
		M.JR().getDialogFormat(UIGiniInjector.getI().getRequestContext(), dialogName, new CallB());
		
	}

}
