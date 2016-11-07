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
package com.gwtmodel.table.view.ewidget.gwt;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.view.ewidget.comboutil.ChooseListHelper;

class ListFieldWithHelp extends ExtendTextBox {

	private final WidgetWithPopUpTemplate wHelp;
	private final ChooseListHelper cHelper;

	private class RHelp extends ChooseListHelper {

		RHelp(IDataType dType) {
			super(dType);
		}

		@Override
		protected void asetValue(String sy) {
			setValObj(sy);
		}

		@Override
		protected void hide() {
			wHelp.hide();
		}
	}

	ListFieldWithHelp(IVField v, IFormFieldProperties pr, IDataType dType, ExtendTextBox.EParam p,
			boolean refreshAlways) {
		super(v, pr, p);
		cHelper = new RHelp(dType);

		wHelp = new WidgetWithPopUpTemplate(v, hPanel, cValues.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP),
				cHelper.getI(), refreshAlways, getHtmlName());
	}
}
