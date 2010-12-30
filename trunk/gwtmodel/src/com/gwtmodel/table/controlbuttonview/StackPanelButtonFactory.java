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
package com.gwtmodel.table.controlbuttonview;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.stack.IClickStackButton;
import com.gwtmodel.table.view.stack.StackButton;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
public class StackPanelButtonFactory {

    private final ContrButtonViewFactory vFactory;

    @Inject
    public StackPanelButtonFactory(ContrButtonViewFactory vFactory) {
        this.vFactory = vFactory;
    }

    public IStackPanelController construct(IDataType dType, List<StackButton> bList,
            String html) {
        List<ControlButtonDesc> buList = new ArrayList<ControlButtonDesc>();
        for (StackButton b : bList) {
            ControlButtonDesc bu = new ControlButtonDesc(b.getDisplayName(), new ClickButtonType(b.getId()));
            buList.add(bu);
        }
        ListOfControlDesc listButton = new ListOfControlDesc(buList);
        return new ControlButtonView(vFactory, listButton, dType, false);
    }
}
