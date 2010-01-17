/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */

package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class RadioBoxField extends AbstractField {

    private final List<ComboVal> wy;
    private final VerticalPanel vP;
    private final List<RadioButton> ra;

    RadioBoxField(TableFactoriesContainer tFactories,String zName, List<ComboVal> wy) {
        super(tFactories);
        this.wy = wy;
        vP = new VerticalPanel();
        ra = new ArrayList<RadioButton>();
        for (ComboVal w : wy ) {
            RadioButton r = new RadioButton(zName,w.getDispVal());
            vP.add(r);
            ra.add(r);
        }
        initWidget(vP);
    }


    public void setVal(String v) {
        int no = 0;
        boolean found = false;
        for (ComboVal w : wy) {
            if (w.eqS(v)) {
                found = true;
                break;
            }
            no++;
        }
        if (!found) { return; }
        RadioButton r = ra.get(no);
        r.setValue(true);
    }

    public String getVal() {
        int no = 0;
        for (RadioButton r : ra) {
            if (r.getValue()) {
                ComboVal w = wy.get(no);
                return w.getVal();
            }
            no++;
        }
        return "";
    }

    public void setReadOnly(boolean readOnly) {
        for (RadioButton r : ra) {
            r.setEnabled(!readOnly);
        }
    }

}
