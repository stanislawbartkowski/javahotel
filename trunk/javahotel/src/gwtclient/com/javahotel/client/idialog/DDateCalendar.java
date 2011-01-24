/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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

        @Override
        public String format(DateBox dateBox, Date date) {
            if (date == null) {
                return "";
            }
            return DateFormatUtil.toS(date);
        }

        @Override
        public Date parse(DateBox dateBox, String text, boolean reportError) {
            return DateFormatUtil.toD(text);
        }

        @Override
        public void reset(DateBox dateBox, boolean abandon) {
        }
    }

    @Override
    public String getVal() {
        return dateBox.getTextBox().getText();
    }

    @Override
    public void refresh() {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        dateBox.getTextBox().setReadOnly(readOnly);
    }

    @Override
    public void setVal(String s) {
        dateBox.getTextBox().setValue(s);
    }

    @Override
    public boolean validateField() {
        return true;
    }

    private class VC implements ValueChangeHandler<java.util.Date> {

        @Override
        public void onValueChange(ValueChangeEvent<Date> event) {
//            if (lC != null) {
//                lC.onChange(DDateCalendar.this);
//            }
            runOnChange(DDateCalendar.this);
        }
    }
}
