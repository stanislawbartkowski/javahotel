/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;

/**
 *
 * @author perseus
 */
public class ComboVal {

    private final String val;
    private final String dispVal;
    private final int no;
    private final boolean compareByNumb;

    public ComboVal(String val, String dispVal) {
        this.val = val;
        this.dispVal = dispVal;
        IGetCustomValues c = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
        compareByNumb = c.compareComboByInt();
        if (compareByNumb) {
            no = CUtil.getNumb(val);
        } else {
            no = -1;
        }

    }

    /**
     * @return the val
     */
    public String getVal() {
        return val;
    }

    /**
     * @return the dispVal
     */
    public String getDispVal() {
        return dispVal;
    }

    boolean eqS(String val) {
        if (!compareByNumb) {
            return CUtil.EqNS(this.val, val);
        }
        String s = CUtil.toAfterS(val, 0);
        int n = CUtil.getNumb(s);
        return n == no;
    }
}
