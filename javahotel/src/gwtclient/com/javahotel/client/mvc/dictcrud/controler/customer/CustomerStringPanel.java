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
package com.javahotel.client.mvc.dictcrud.controler.customer;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CustomerStringPanel extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private final ICrudControler iCrud;
    private final SpecE eE;

    public CustomerStringPanel(IResLocator rI, SpecE eE) {
        this.rI = rI;
        this.eE = eE;
        iCrud = DictCrudControlerFactory.getCrud(rI,
                new DictData(eE));
    }

    ICrudControler getICrud() {
        return iCrud;
    }


    @Override
    public void changeMode(int actionMode) {
        iCrud.changeMode(actionMode);
    }

    @Override
    public void show() {
       iCrud.drawTable();
    }

    public IMvcWidget getMWidget() {
        return iCrud.getMWidget();
    }

}
