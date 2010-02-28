/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.common.command.DictType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DoBooking extends AbstractPanelCommand {

    private ICrudControler cPan;
    private final IResLocator rI;

    DoBooking(IResLocator rI) {
        this.rI = rI;

    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        cPan = HInjector.getI().getDictCrudControlerFactory().getCrud(new DictData(DictType.BookingList));
        iSet.setGwtWidget(cPan.getMWidget());
    }

    public void drawAction() {
        cPan.drawTable();
    }
}
