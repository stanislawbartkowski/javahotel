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
package com.javahotel.client.idialog;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.ifield.ISetWidget;
import com.javahotel.client.widgets.popup.PopUpTip;
import com.javahotel.client.widgets.popup.PopupUtil;
import com.javahotel.common.dateutil.DateFormatUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
abstract class ELineDialog extends Composite implements ILineField {

    protected final IResLocator pLi;
    protected IKeyboardAction kLi;
    protected IChangeListener lC;
    protected final boolean isCheckBox;
    protected final boolean checkBoxVal;
    private String errmess = null;
    private PopupPanel tup = null;

    private void hideUp() {
        if (tup != null) {
            tup.hide();
            tup = null;
        }
    }

    private class MouseO implements MouseOverHandler, MouseOutHandler {

        public void onMouseOver(MouseOverEvent event) {
            if (errmess != null) {
                tup = PopUpTip.getPopupTip(new Label(errmess));
                PopupUtil.setPos(tup, ELineDialog.this);
                tup.show();
            }
        }

        public void onMouseOut(MouseOutEvent event) {
            hideUp();
        }
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    protected void setMouse() {
        this.addMouseOutHandler(new MouseO());
        this.addMouseOverHandler(new MouseO());
    }

    ELineDialog(final IResLocator pLi) {
        this.pLi = pLi;
        kLi = null;
        lC = null;
        checkBoxVal = false;
        isCheckBox = true;
    }

    ELineDialog(final IResLocator pLi, boolean checkenable) {
        this.pLi = pLi;
        kLi = null;
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
            getMWidget().getWidget().setStyleName(sName);
        } else {
            getMWidget().getWidget().removeStyleName(sName);
        }
    }

    public void setKLi(final IKeyboardAction pkLi) {
        this.kLi = pkLi;
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

    public void setChangeListener(final IChangeListener l) {
        lC = l;
    }

    public void setWidget(final ISetWidget i) {
        i.setW(getMWidget().getWidget());
    }

    public void setErrMess(String errmess) {
        this.errmess = errmess;
        if (errmess == null) {
            hideUp();
        }
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(this);
    }
}
