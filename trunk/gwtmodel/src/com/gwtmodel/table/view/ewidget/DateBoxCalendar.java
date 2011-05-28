/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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

import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormChangeListener;
import java.util.Date;

/**
 * 
 * @author perseus
 */
class DateBoxCalendar extends AbstractField {

    private final DateBox db;

    @Override
    public void setReadOnly(boolean readOnly) {
        db.setEnabled(!readOnly);
    }

    @Override
    public Object getValObj() {
        return db.getValue();
    }

    @Override
    public void setValObj(Object o) {
        db.setValue((Date) o);
    }

    private class DFormat implements DateBox.Format {

        @Override
        public String format(DateBox dateBox, Date date) {
            if (date == null) {
                return "";
            }
            return Utils.toS(date);
        }

        @Override
        public Date parse(DateBox dateBox, String text, boolean reportError) {
            return Utils.toD(text);
        }

        @Override
        public void reset(DateBox dateBox, boolean abandon) {
        }
    }

    DateBoxCalendar(ITableCustomFactories tFactories, IVField v) {
        super(tFactories, v);
        db = new DateBox();
        db.setFormat(new DFormat());
        db.getTextBox().setName(v.getId());
        db.addValueChangeHandler(new ValueChange());
        db.getTextBox().addKeyboardListener(new Touch(iTouch));
        initWidget(db);
    }

    @Override
    public void addChangeListener(final IFormChangeListener l) {
        super.addChangeListener(l);
    }
}
