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
package com.gwtmodel.table.view.ewidget;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.ewidget.hourminutepicker.HourMinutePicker;
import com.gwtmodel.table.view.ewidget.hourminutepicker.HourMinutePicker.PickerFormat;

class DateTimePicker extends AbstractField {

	private final HorizontalPanel h;
	private final HourMinutePicker hourMinutePicker;
	private final DateBoxCalendar dBox;

	@Override
	public void setReadOnly(boolean readOnly) {
		// hourMinutePicker.
		dBox.setReadOnly(readOnly);
		hourMinutePicker.setReadonly(readOnly);
	}

	@Override
	public Object getValObj() {
		Date d = (Date) dBox.getValObj();
		if (d == null) {
			return null;
		}
		Timestamp t = new Timestamp(d.getTime());
		t.setHours(hourMinutePicker.getHour());
		t.setMinutes(hourMinutePicker.getMinute());
		t.setSeconds(0);
		return t;
	}

	@Override
	public void setValObj(Object o) {
		if (o == null) {
			dBox.setValObj(o);
			hourMinutePicker.clear();
			return;
		}
		Timestamp t = (Timestamp) o;
		dBox.setValObj(new Date(t.getTime()));
		if (o != null) {
			hourMinutePicker.setTime(null, t.getHours(), t.getMinutes());
		}
	}

	private class PickerChange implements HourMinutePicker.IOnChange {

		@Override
		public void changeAction() {
			onTouch();
			runOnChange(DateTimePicker.this, true);
		}

	}

	@SuppressWarnings({ "unchecked" })
	DateTimePicker(IGetCustomValues cValues, IVField v, String htmlName) {
		super(cValues, v, htmlName);
		hourMinutePicker = new HourMinutePicker(PickerFormat._24_HOUR, new PickerChange());
		dBox = new DateBoxCalendar(cValues, v, htmlName);
		h = new HorizontalPanel();
		h.add(dBox);
		h.add(hourMinutePicker);
		initWidget(h);
	}

	@Override
	public void addChangeListener(final IFormChangeListener l) {
		dBox.addChangeListener(l);
		super.addChangeListener(l);
	}

	@Override
	public void setOnTouch(final ITouchListener iTouch) {
		super.setOnTouch(iTouch);
		dBox.setOnTouch(iTouch);
	}

	@Override
	public void setFocus(boolean focus) {
		dBox.setFocus(focus);

	}

}