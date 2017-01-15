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
package com.gwtmodel.table.view.ewidget.polymer;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.WidgetTypes;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.mm.LogT;
import com.vaadin.polymer.vaadin.widget.VaadinUpload;

/**
 * 
 * @author perseus
 */
class FileUpLoad extends AbstractWField {

	private VaadinUpload upl = new VaadinUpload();

	protected FileUpLoad(IVField v, IFormFieldProperties pr) {
		super(v, pr, null);
	}

	@Override
	public Object getValObj() {
		return "";
	}

	@Override
	public void setValObj(Object o) {
	}

	@Override
	public Widget getGWidget() {
		return upl;
	}

	@Override
	public void replaceWidget(Widget w) {
		if (!(w instanceof VaadinUpload))
			Utils.errAlertB(LogT.getT().ReplaceTypeNotCorrect(WidgetTypes.VaadinUpload.name(),
					VaadinUpload.class.getName(), w.getClass().getName()));
		upl = (VaadinUpload) w;

	}

}
