package com.javahotel.client.idialog;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.datepicker.client.DateBox;
import com.javahotel.client.IResLocator;
import com.javahotel.common.dateutil.DateFormatUtil;

class DDateCalendar extends ELineDialog {

	DateBox dateBox = new DateBox();
	
	DDateCalendar(IResLocator iRes) {
		super(iRes);
		dateBox.setFormat(new DFormat());
		initWidget(dateBox);
		setMouse();
		dateBox.addStyleName("calendar-date");
		dateBox.addValueChangeHandler(new VC());
	}
	
    private class DFormat implements DateBox.Format {

        public String format(DateBox dateBox, Date date) {
            if (date == null) {
                return "";
            }
            return DateFormatUtil.toS(date);
        }

        public Date parse(DateBox dateBox, String text, boolean reportError) {
            return DateFormatUtil.toD(text);
        }

        public void reset(DateBox dateBox, boolean abandon) {
        }
    }

	public String getVal() {
		return dateBox.getTextBox().getText();
	}


	public void refresh() {
	}


	public void setReadOnly(boolean readOnly) {
		dateBox.getTextBox().setReadOnly(readOnly);
	}


	public void setVal(String s) {
		dateBox.getTextBox().setValue(s);
	}


	public boolean validateField() {
		return true;
	}
	
	private class VC implements ValueChangeHandler<java.util.Date> {

		public void onValueChange(ValueChangeEvent<Date> event) {
			if (lC != null) {
				lC.onChange(DDateCalendar.this);
			}			
		}
		
	}

}
