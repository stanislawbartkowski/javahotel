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
package com.javahotel.client.dialog.user.addpayment;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.IMvcView;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.dictdata.model.IPaymentModel;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AddPayment implements IMvcView, IPaymentModel {

    private final ICrudControler iCrud;
    private final IResLocator rI;
    private final String resName;

    public AddPayment(IResLocator rI, Widget w, String resName,
            ICrudPersistSignal iSig) {
        this.rI = rI;
        this.resName = resName;
        RecordAuxParam aux = new RecordAuxParam();
        aux.setAuxO1(this);
        aux.setPSignal(iSig);
        iCrud = DictCrudControlerFactory.getCrud(rI,
                new DictData(SpecE.AddPayment), aux, null);
        iCrud.RecordDialog(IPersistAction.ADDACION, w, null);
    }

    public void show() {
    }

    public void hide() {
    }

    public String getResName() {
        return resName;
    }

    public IMvcWidget getMWidget() {
        return null;
    }
}
