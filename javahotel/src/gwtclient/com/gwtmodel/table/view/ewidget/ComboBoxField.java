/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class ComboBoxField extends GetValueLB {

    private final List<ComboVal> wy;

    ComboBoxField(TableFactoriesContainer tFactories, List<ComboVal> wy) {
        super(tFactories);
        this.wy = wy;
        List<String> val = new ArrayList<String>();
        for (ComboVal v : wy) {
            val.add(v.getDispVal());
        }
        setList(val);
    }

    @Override
    public void setVal(String val) {
        String di = null;
        for (ComboVal v : wy) {
            if (v.eqS(val)) {
                di = v.getDispVal();
            }
        }
        super.setVal(di);

    }

    @Override
    public String getVal() {
        String vv = super.getVal();
        for (ComboVal v : wy) {
            if (CUtil.EqNS(vv, v.getDispVal())) {
                return v.getVal();
            }
        }
        return null;
    }
}
