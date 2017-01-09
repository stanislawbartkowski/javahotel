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

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.util.ClickPopUp;

class DateCalendarPolymerPaper extends TextHelperImage {

	interface IGetSetValue {
		Date getVal();

		void setVal(Date d);
	}

	private class GetSetValue implements IGetSetValue {

		@Override
		public Date getVal() {
			return (Date) getValObj();
		}

		@Override
		public void setVal(Date d) {
			setValObj(d);
		}
	}

	private final HorizontalPanel vp = new HorizontalPanel();

	protected IGetSetValue iGet = new GetSetValue();

	DateCalendarPolymerPaper(IVField v, IFormFieldProperties pr, String pattern, String standErrMess) {
		super(v, pr, pattern, standErrMess, "vaadin-icons:calendar");
		bu.addClickHandler(event -> {

			final DatePicker dPicker = new DatePicker();
			// Date da = (Date) getValObj();
			Date da = iGet.getVal();
			if (da != null) {
				dPicker.setValue(da);
				dPicker.setCurrentMonth(da);
			}
			final ClickPopUp pUp = new ClickPopUp(bu, dPicker);
			dPicker.addValueChangeHandler(e -> {

				// setValObj(dPicker.getValue());
				iGet.setVal(dPicker.getValue());
				pUp.setVisible(false);

			});
			pUp.setVisible(true);
		});
	}

}
