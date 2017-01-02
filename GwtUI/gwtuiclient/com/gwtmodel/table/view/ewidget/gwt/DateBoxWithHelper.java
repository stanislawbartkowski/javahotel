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
package com.gwtmodel.table.view.ewidget.gwt;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.factories.IGetCustomValues;

class DateBoxWithHelper extends DateBoxCalendar {

	private final WidgetWithPopUpTemplate wHelp;
	private final HorizontalPanel hPanel = new HorizontalPanel();

	DateBoxWithHelper(IVField v, IFormFieldProperties pr, IRequestForGWidget i, boolean refreshAlways) {
		super(v, pr);
		hPanel.add(super.getGWidget());
		wHelp = new WidgetWithPopUpTemplate(v, hPanel, cValues.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP), i,
				refreshAlways, getHtmlName());

	}

	@Override
	public Widget getGWidget() {
		return hPanel;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		wHelp.setReadOnly(readOnly);
	}

}
