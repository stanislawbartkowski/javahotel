/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.CheckBox;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.TableFactoriesContainer;

/**
 *
 * @author perseus
 */
class FieldCheckField extends AbstractField {

    private final CheckBox ch;

    FieldCheckField(TableFactoriesContainer tFactories) {
        super(tFactories);
        ch = new CheckBox();
        ch.setChecked(true);
        initWidget(ch);
        setMouse();
    }

    public void setVal(String v) {
        ch.setChecked(Utils.TrueL(v));

    }

    public String getVal() {
        return Utils.LToS(ch.isChecked());
    }

    @Override
    public boolean emptyF() {
        return false;
    }

    public void setReadOnly(boolean readOnly) {
        ch.setEnabled(!readOnly);
    }
}
