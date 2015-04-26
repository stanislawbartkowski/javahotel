/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.columndialog;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.ILaunchPropertyDialogColumn;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.dialog.run.RunAction;
import com.jythonui.client.interfaces.IVariableContainerFactory;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;

public class PropertyColumnDialog implements ILaunchPropertyDialogColumn {

	private final IVariableContainerFactory vFactory;

	@Inject
	public PropertyColumnDialog(IVariableContainerFactory vFactory) {
		this.vFactory = vFactory;
	}

	@Override
	public void doDialog(ISlotable publishSlo, IDataType dType, WSize w) {
		IVariablesContainer iCon = vFactory.construct();
		VListHeaderContainer vHeader = SlU.getHeaderList(dType, publishSlo);
		DialogVariables var = new DialogVariables();
		ListOfRows li = new ListOfRows();
		for (VListHeaderDesc v : vHeader.getAllHeList()) {
			RowContent ro = new RowContent();
			ro.setRowSize(3);
			FieldValue val = new FieldValue();
			val.setValue(!v.isHidden());
			ro.setRow(0, val);
			val = new FieldValue();
			val.setValue(v.getFie().getId());
			ro.setRow(1, val);
			val = new FieldValue();
			val.setValue(v.getHeaderString());
			ro.setRow(2, val);
			li.addRow(ro);
		}
		var.getRowList().put("list", li);
		new RunAction().upDialog(IUIConsts.COLUMNDIALOG, w, iCon, null, null,
				null, var);
	}

}
