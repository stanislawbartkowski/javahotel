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
package com.javahotel.nmvc.factories.booking;

import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.editc.IChangeObject;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;

/**
 * @author hotel
 *
 */
public class ReceiveChange implements ISlotListener {

    private final IEditChooseRecordContainer cContainer;
    private final IResLocator rI;

    public ReceiveChange(IEditChooseRecordContainer cContainer) {
        this.cContainer = cContainer;
        rI = HInjector.getI().getI();
    }

    @Override
    public void signal(ISlotSignalContext slContext) {
        IChangeObject i = (IChangeObject) slContext.getCustom();
        LogT.getLS().info(
                LogT.getT().receivedSignalLogParam(
                        slContext.getSlType().toString(), i.toString()));
        String mess = null;
        String ask = null;
        final boolean newset;
        final boolean changeset;
        if (i.getWhat() == IChangeObject.NEW) {
            newset = !i.getSet();
            changeset = cContainer.getChangeCheck();
            if (i.getSet()) {
                ask = rI.getLabels().NextCustomerSymbol();
            } else {
                mess = rI.getLabels().CannotChangeNoNewCustomer();
            }
        } else {
            newset = cContainer.getNewCheck();
            changeset = !i.getSet();
            if (i.getSet()) {
                ask = rI.getLabels().CustomerDataWillBeChanged();
            } else {
                mess = rI.getLabels().CannotChangeCustomerToNotChange();
            }
        }

        if (mess != null) {
            cContainer.SetNewChange(newset, changeset);
            OkDialog ok = new OkDialog(mess, null);
            ok.show(i.getW().getGWidget());
        }
        if (ask != null) {
            IClickYesNo c = new IClickYesNo() {

                @Override
                public void click(boolean yes) {
                    if (!yes) {
                        cContainer.SetNewChange(newset, changeset);
                    } else {
                        cContainer.ModifForm();
                    }

                }
            };
            YesNoDialog y = new YesNoDialog(ask + " "
                    + rI.getLabels().Confirm(), c);
            y.show(i.getW().getGWidget());
        }
    }
}
