/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormatUtil;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import java.util.Date;

/**
 *
 * @author perseus
 */
class DateBoxCalendar extends AbstractField {

    private final DateBox db;

    public void setReadOnly(boolean readOnly) {
        db.setEnabled(!readOnly);
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

    DateBoxCalendar(TableFactoriesContainer tFactories) {
        super(tFactories);
        db = new DateBox();
        db.setFormat(new DFormat());
        db.addValueChangeHandler(new ValueChange());
        db.getTextBox().addKeyboardListener(new Touch(iTouch));
        initWidget(db);
        setMouse();
    }

    @Override
    public void addChangeListener(final IFormChangeListener l) {
        super.addChangeListener(l);
    }

    public void setVal(String v) {
        Date d = DateFormatUtil.toD(v);
        db.setValue(d);
    }

    public String getVal() {
        Date d = db.getValue();
        return DateFormatUtil.toS(d);
    }

    @Override
    public boolean emptyF() {
        String s = getVal();
        return CUtil.EmptyS(s);
    }
}
