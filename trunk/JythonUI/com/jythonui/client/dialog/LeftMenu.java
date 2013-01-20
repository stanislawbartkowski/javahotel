/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.stackpanelcontroller.StackPanelControllerFactory;
import com.gwtmodel.table.view.callback.ICommonCallBackFactory;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.M;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public class LeftMenu {


    private final static IDataType leftMenuD = Empty.getDataType();

    private ControlButtonDesc constructButton(ButtonItem b) {

        String id = b.getId();
        String dName = b.getDisplayName();
        return new ControlButtonDesc(dName, id);
    }

    private List<ControlButtonDesc> constructBList(List<ButtonItem> iList) {
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        for (ButtonItem b : iList) {
            bList.add(constructButton(b));
        }
        return bList;

    }

    private class CButton implements ISlotListener {

        private final String dialogName;
        private final IVariablesContainer iCon;
        private final ICommonCallBackFactory<DialogVariables> cBack;

        CButton(IVariablesContainer iCon,
                ICommonCallBackFactory<DialogVariables> cBack, String dialogName) {
            this.dialogName = dialogName;
            this.iCon = iCon;
            this.cBack = cBack;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            String id = slContext.getSlType().getButtonClick().getCustomButt();
            assert id != null : LogT.getT().cannotBeNull();
            DialogVariables v = iCon.getVariables();
            M.JR().runAction(v, dialogName, id, cBack.construct());
        }

    }

    private class GetWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            IWebPanel i = GwtGiniInjector.getI().getWebPanel();
            i.setWest(w);
        }
    }

    void createLeftButton(IVariablesContainer iCon,
            ICommonCallBackFactory<DialogVariables> backFactory,
            String dialogName, List<ButtonItem> buttList) {
        if (buttList != null) {
            List<ControlButtonDesc> bList = constructBList(buttList);
            StackPanelControllerFactory sFactory = GwtGiniInjector.getI()
                    .getStackPanelControllerFactory();
            IStackPanelController iSlo = sFactory.construct(leftMenuD, bList,
                    null);
            SlU.registerWidgetListener0(leftMenuD, iSlo, new GetWidget());
            iSlo.getSlContainer().registerSubscriber(leftMenuD,
                    ClickButtonType.StandClickEnum.ALL,
                    new CButton(iCon, backFactory, dialogName));
            CellId cId = new CellId(0);
            iSlo.startPublish(cId);
        }
    }
}