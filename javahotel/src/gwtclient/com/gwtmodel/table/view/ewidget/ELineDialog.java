/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.util.PopupTip;
import com.javahotel.client.CommonUtil;
import com.javahotel.common.dateutil.DateFormatUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class ELineDialog extends PopupTip implements IFormLineView {

    protected ITouchListener iTouch;
    protected Collection<IFormChangeListener> lC;
    protected final boolean isCheckBox;
    protected final boolean checkBoxVal;
    protected final TableFactoriesContainer tFactories;

    ELineDialog(TableFactoriesContainer tFactories) {
        this.tFactories = tFactories;
        iTouch = null;
        lC = null;
        checkBoxVal = false;
        isCheckBox = true;
    }

    ELineDialog(TableFactoriesContainer tFactories,boolean checkenable) {
        this.tFactories = tFactories;
        iTouch = null;
        lC = null;
        checkBoxVal = checkenable;
        isCheckBox = true;
    }

    public int getChooseResult() {
        return NOCHOOSECHECK;
    }

    public void setChooseCheck(boolean enable) {
    }

    public boolean emptyF() {
        String val = getVal();
        return ((val == null) || val.equals(""));
    }

    public void setStyleName(final String sName, final boolean set) {
        if (set) {
            getGWidget().setStyleName(sName);
        } else {
            getGWidget().removeStyleName(sName);
        }
    }

    public void setOnTouch(final ITouchListener iTouch) {
        this.iTouch = iTouch;
    }

    public int getIntVal() {
        String n = getVal();
        int no = CommonUtil.getNum(n);
        return no;
    }

    public BigDecimal getDecimal() {
        return CommonUtil.toBig(getVal());
    }

    public Date getDate() {
        String n = getVal();
        if (n == null) {
            return null;
        }
        return DateFormatUtil.toD(n);
    }

    public void setDecimal(BigDecimal b) {
        if (b == null) {
            setVal("");
            return;
        }
        setVal(CommonUtil.DecimalToS(b));
    }

    public void addChangeListener(final IFormChangeListener l) {
        if (lC == null) {
            lC = new ArrayList<IFormChangeListener>();
        }
        lC.add(l);
    }

    protected void runOnChange(IFormLineView i) {
        if (lC == null) {
            return;
        }
        for (IFormChangeListener c : lC) {
            c.onChange(i);
        }
    }
    
    public void setInvalidMess(String errmess) {
        setMessage(errmess);
    }
}
