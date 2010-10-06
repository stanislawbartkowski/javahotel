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
package com.javahotel.client.mvc.dictcrud.controler.season;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SpecPeriodView extends AbstractAuxRecordPanel {

    @SuppressWarnings("unused")
    private final IResLocator rI;
    private final SeasonPeriodT te;
    private final ICrudControler iCrud;

    SpecPeriodView(IResLocator rI, SeasonPeriodT te, Object auxV1) {
        this.rI = rI;
        this.te = te;
        RecordAuxParam pa = new RecordAuxParam();
        pa.setAuxO1(auxV1);
        iCrud = HInjector.getI().getDictCrudControlerFactory().getCrud(new DictData(
                DictData.SpecE.SpecialPeriod, te), pa, null);
    }

    ICrudControler getICrud() {
        return iCrud;
    }

    SeasonPeriodT getS() {
        return te;
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
