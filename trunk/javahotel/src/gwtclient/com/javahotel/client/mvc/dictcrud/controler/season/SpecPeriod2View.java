/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictdata.model.ISeasonOffModel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class SpecPeriod2View extends AbstractAuxRecordPanel
        implements ISeasonOffModel {

    private final SpecPeriodView s1;
    private final SpecPeriodView s2;
    private final VerticalPanel vP;

    public SpecPeriod2View(IResLocator rI) {
        s1 = new SpecPeriodView(rI, SeasonPeriodT.LOW, this);
        s2 = new SpecPeriodView(rI, SeasonPeriodT.SPECIAL, this);
        vP = new VerticalPanel();
        vP.add(s1.getMWidget().getWidget());
        vP.add(s2.getMWidget().getWidget());
    }

    @Override
    public void changeMode(int actionMode) {
        s1.changeMode(actionMode);
        s2.changeMode(actionMode);
    }

    public void show() {
        s1.show();
        s2.show();
    }

    public ICrudControler getTable1() {
        return s1.getICrud();
    }

    public ICrudControler getTable2() {
        return s2.getICrud();
    }

    public SeasonPeriodT getP1() {
        return s1.getS();
    }

    public SeasonPeriodT getP2() {
        return s2.getS();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(vP);
    }
}
