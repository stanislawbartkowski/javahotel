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
package com.gwtmodel.table.view.mdialog;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.view.util.ModalDialog;

class GwtModalDialog extends ModalDialog implements IMDialog {

	private final Widget w;

	// private ModalDialog md;

	GwtModalDialog(Widget w, IDataType dType, boolean autohide, boolean modal) {
		super(new VerticalPanel(), "", autohide, modal);
		this.w = w;
		create();
		// this.setOnClose(new CloseI(dType));
	}

	@Override
	protected void addVP(VerticalPanel vp) {
		vp.add(w);
	}
}
