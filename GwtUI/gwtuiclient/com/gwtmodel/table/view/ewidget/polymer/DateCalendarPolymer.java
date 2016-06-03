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
package com.gwtmodel.table.view.ewidget.polymer;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.vaadin.polymer.paper.widget.PaperIconButton;

class DateCalendarPolymer extends PolymerTextField {

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

	protected IGetSetValue iGet = new GetSetValue();

	DateCalendarPolymer(IVField v, IFormFieldProperties pr, String pattern, String standErrMess) {
		super(v, pr, pattern, standErrMess, false);
		PaperIconButton bu = new PaperIconButton();
		bu.setIcon("vaadin-icons:calendar");
		bu.setAttributes("suffix");
		in.add(bu);
		bu.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DatePicker dPicker = new DatePicker();
				// Date da = (Date) getValObj();
				Date da = iGet.getVal();
				if (da != null) {
					dPicker.setValue(da);
					dPicker.setCurrentMonth(da);
				}
				ClickPopUp pUp = new ClickPopUp(bu, dPicker);
				dPicker.addValueChangeHandler(new ValueChangeHandler<Date>() {

					@Override
					public void onValueChange(ValueChangeEvent<Date> event) {
						// setValObj(dPicker.getValue());
						iGet.setVal(dPicker.getValue());
						pUp.setVisible(false);

					}
				});
				pUp.setVisible(true);
			}
		});
	}
}
