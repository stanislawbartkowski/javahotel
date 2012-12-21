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
package com.javahotel.client.start.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.stack.IStackPanelView;
import com.gwtmodel.table.view.stack.StackPanelFactory;
import com.gwtmodel.table.view.util.PopupCreateMenu;
import com.javahotel.client.IImageGallery;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class StackHeaderAddList {

    private StackHeaderAddList() {
    }

    private static String getPanelCommandLabel(IResLocator sI,
            EPanelCommand command) {
        String key = "PANEL" + command.toString();
        String val = (String) sI.getLabels().PanelLabelNames().get(key);
        if (val == null) {
            return "???";
        }
        return val;
    }

    private static void addElem(IResLocator rI, List<ControlButtonDesc> v,
            EPanelCommand e) {
        String label = getPanelCommandLabel(rI, e);
        v.add(new ControlButtonDesc(label, e.name()));
    }

    private static List<ControlButtonDesc> createControlButtonDescList(
            EPanelCommand[] et) {
        IResLocator rI = HInjector.getI().getI();
        List<ControlButtonDesc> aList = new ArrayList<ControlButtonDesc>();
        for (int i = 0; i < et.length; i++) {
            addElem(rI, aList, et[i]);
        }
        return aList;
    }

    private static class Click implements IControlClick {

        @Override
        public void click(ControlButtonDesc bu, Widget w) {
            String id = bu.getActionId().getCustomButt();
            EPanelCommand e = EPanelCommand.valueOf(id);
            IPanelCommandFactory pa = HInjector.getI().getPanelCommandFactory();
            IPanelCommand i = pa.construct(e);
            IResLocator rI = HInjector.getI().getI();
            CommandDrawPanel.setC(rI, i, bu.getDisplayName());
        }

    }

    public static IGWidget constructPanel(EPanelCommand[] et) {
        StackPanelFactory sFactory = GwtGiniInjector.getI()
                .getStackPanelFactory();

        IStackPanelView iView = sFactory.construct(
                createControlButtonDescList(et), new Click(), null);
        return iView;
    }

    public static IGWidget constructMenu(EPanelCommand[] eMenu) {
        List<ControlButtonDesc> li = createControlButtonDescList(eMenu);
        String htmlImage = Utils.getImageHTML(IImageGallery.DOWNMENU);
        return PopupCreateMenu.createImageMenu(htmlImage,
                new ListOfControlDesc(li), new Click(), null);
    }
}
