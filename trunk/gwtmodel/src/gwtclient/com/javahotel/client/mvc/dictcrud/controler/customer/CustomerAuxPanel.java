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
package com.javahotel.client.mvc.dictcrud.controler.customer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.customer.model.ICustomerModelView;
import com.javahotel.client.mvc.dictcrud.controler.DictUtil;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.ICreateViewContext;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.PhoneNumberP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CustomerAuxPanel implements IAuxRecordPanel {

    private final IResLocator rI;
    private final VerticalPanel vP;
    private final CustomerStringPanel cBank;
    private final CustomerStringPanel cPhone;

    public CustomerAuxPanel(IResLocator rI) {
        this.rI = rI;
        vP = new VerticalPanel();
        cBank = new CustomerStringPanel(rI, DictData.SpecE.CustomerAccount);
        cPhone = new CustomerStringPanel(rI, DictData.SpecE.CustomerPhone);
        vP.add(cBank.getMWidget().getWidget());
        vP.add(cPhone.getMWidget().getWidget());
    }

    public ICustomerModelView getV() {
        ICustomerModelView i = new ICustomerModelView() {

            public ICrudControler getPhone() {
                return cPhone.getICrud();
            }

            public ICrudControler getBank() {
                return cBank.getICrud();
            }
        };
        return i;
    }

    public void showInvalidate(IErrorMessage col) {
    }

    public void extractFields(RecordModel mo) {
        CustomerP cu = (CustomerP) mo.getA();
        List<PhoneNumberP> c1 = new ArrayList<PhoneNumberP>();
        DictUtil.readList(cPhone.getICrud().getTableView().getModel(), c1);
        cu.setPhones(c1);
        List<BankAccountP> c2 = new ArrayList<BankAccountP>();
        DictUtil.readList(cBank.getICrud().getTableView().getModel(), c2);
        cu.setAccounts(c2);
    }

    public void setFields(RecordModel mo) {
        // toView
        CustomerP cu = (CustomerP) mo.getA();
        DictUtil.setList(cBank.getICrud().getTableView().getModel(), cu
                .getAccounts());
        DictUtil.setList(cPhone.getICrud().getTableView().getModel(), cu
                .getPhones());
    }

    public void changeMode(int actionMode) {
        cBank.changeMode(actionMode);
        cPhone.changeMode(actionMode);
    }

    public void show() {
        cBank.show();
        cPhone.show();
    }

    public void hide() {
    }

    public IRecordValidator getValidator() {
        return null;
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(vP);
    }

    public boolean getCustomView(ISetGwtWidget iSet, ICreateViewContext con,
            IContrButtonView i) {
        return false;
    }

}
