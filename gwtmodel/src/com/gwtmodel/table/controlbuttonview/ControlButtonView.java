/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controlbuttonview;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ButtonAction;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;

class ControlButtonView extends AbstractSlotContainer implements
        IControlButtonView, IStackPanelController {

    private final IContrButtonView vButton;

    private Map<ClickButtonType, ClickButtonType> redirMap = new HashMap<ClickButtonType, ClickButtonType>();

    private class Click implements IControlClick {

        @Override
        public void click(ControlButtonDesc co, Widget w) {
            ClickButtonType bType = redirMap.get(co.getActionId());
            publish(bType != null ? bType : co.getActionId(), new GWidget(w));
        }
    }

    private class EnableButton implements ISlotListener {

        private final ButtonAction bAction;
        private final ClickButtonType actionId;

        EnableButton(ButtonAction bAction, ClickButtonType actionId) {
            this.actionId = actionId;
            this.bAction = bAction;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            LogT.getLS().info(
                    LogT.getT().receivedSignalLog(
                            slContext.getSlType().toString()));
            vButton.setEnable(actionId,
                    bAction.getAction() == ButtonAction.Action.EnableButton);
        }
    }

    private class RedirectButton implements ISlotListener {

        private final ControlButtonDesc b;

        RedirectButton(ControlButtonDesc b) {
            this.b = b;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            redirMap.put(b.getActionId(), slContext.getSlType().getbAction()
                    .getRedirectB());
        }
    }

    /**
     * Listener for 'force button' action
     * 
     * @author hotel
     * 
     */
    private class ForceButton implements ISlotListener {

        private final ControlButtonDesc b;

        ForceButton(ControlButtonDesc b) {
            this.b = b;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            vButton.emulateClick(b.getActionId());

        }

    }

    private void register(ButtonAction.Action bA, ControlButtonDesc b) {
        ButtonAction ba = new ButtonAction(bA);
        EnableButton e = new EnableButton(ba, b.getActionId());
        registerSubscriber(dType, b.getActionId(), ba, e);
    }

    ControlButtonView(ContrButtonViewFactory vFactory,
            ListOfControlDesc listButton, IDataType dType, boolean hori) {
        this.dType = dType;
        vButton = vFactory.getView(listButton, new Click(), hori);
        for (ControlButtonDesc b : listButton.getcList()) {
            register(ButtonAction.Action.EnableButton, b);
            register(ButtonAction.Action.DisableButton, b);
            ButtonAction bR = new ButtonAction(
                    ButtonAction.Action.RedirectButton);
            registerSubscriber(dType, b.getActionId(), bR,
                    new RedirectButton(b));
            ButtonAction fR = new ButtonAction(ButtonAction.Action.ForceButton);
            registerSubscriber(dType, b.getActionId(), fR, new ForceButton(b));

        }

    }

    @Override
    public void startPublish(CellId cellId) {
        IGWidget w = getHtmlWidget(cellId);
        if (w == null) {
            publish(dType, cellId, vButton);
        } else {
            vButton.fillHtml(w);
        }
    }
}
