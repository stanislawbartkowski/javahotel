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
package com.gwtmodel.table.view.mdialog;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.util.SolidPos;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.vaadin.polymer.paper.widget.PaperDialog;

class PolymerDialog implements IMDialog {

	private final Widget w;
	private ISignal iSig;
	private final ISignal iClose;
	private final String addStyleNames;

	PolymerDialog(Widget w, IDataType dType, boolean autohide, boolean modal, ISignal iClose, String addStyleNames) {
		this.w = w;
		this.iClose = iClose;
		this.addStyleNames = addStyleNames;
	}

	@Override
	public void show(WSize ws, SolidPos sPos) {
		PaperDialog p = null;
		if (w instanceof PaperDialog)
			p = (PaperDialog) w;
		else {
			String errmess = LogT.getT().PupupPolymerDialogShouldBePaperDialog(PaperDialog.class.getName(),
					w.getClass().getName());
			Utils.errAlertB(errmess);
		}
		iSig = PolymerUtil.popupPolymer(ws, null, p, iClose, addStyleNames);
	}

	@Override
	public void hide() {
		iSig.signal();
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub

	}

}
