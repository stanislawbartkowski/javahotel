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
package com.jythonui.client.dialog.impl;

import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.jythonui.client.dialog.ICreateBackActionFactory;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.IYesNoAction;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.MapDialogVariable;

class HandleYesNoDialog implements IYesNoAction {

	private final MapDialogVariable addV;
	private final ButtonItem bId;
	private final IVariablesContainer iCon;
	private final DialogFormat d;
	private final ICreateBackActionFactory bFactory;

	HandleYesNoDialog(MapDialogVariable addV, ButtonItem bId, IVariablesContainer iCon, DialogFormat d,
			ICreateBackActionFactory bFactory) {
		this.addV = addV;
		this.bId = bId;
		this.iCon = iCon;
		this.d = d;
		this.bFactory = bFactory;
	}

	@Override
	public void answer(String content, String title, final String param1, final WSize w) {
		IClickYesNo i = new IClickYesNo() {

			@Override
			public void click(boolean yes) {
				String action = param1;
				if (CUtil.EmptyS(param1) && bId != null)
					action = bId.getId();
				DialogVariables v = iCon.getVariables(action);
				v.setValueB(ICommonConsts.JYESANSWER, yes);
				v.copyVariables(addV);
				// M.JR().runAction(v, d.getId(), param1,
				// new BackClass(param1, false, w, null));
				// 2013/04/14
				// ExecuteAction.action(v, d.getId(), action, new
				// BackClass(param1, false, w, null));
				ExecuteAction.action(v, d.getId(), action, bFactory.construct(param1, w, null, null));
			}

		};

		YesNoDialog yes = new YesNoDialog(content, title, i);
		yes.show(w);
	}
}
