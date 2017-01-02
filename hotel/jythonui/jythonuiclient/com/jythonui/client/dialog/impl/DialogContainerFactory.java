/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

import com.gwtmodel.table.IDataType;
import com.jythonui.client.dialog.ICustomClickAction;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.interfaces.IDialogContainerFactory;
import com.jythonui.client.util.IExecuteAfterModalDialog;
import com.jythonui.client.util.ISendCloseAction;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;

public class DialogContainerFactory implements IDialogContainerFactory {

	@Override
	public IDialogContainer construct(IDataType dType, DialogInfo info,
			IVariablesContainer pCon, ISendCloseAction iClose,
			DialogVariables addV, IExecuteAfterModalDialog iEx,
			String[] startVal, ICustomClickAction iCustomClick, boolean mainD) {
		return new DialogContainer(dType, info, pCon, iClose, addV, iEx,
				startVal, iCustomClick, mainD);
	}

}
