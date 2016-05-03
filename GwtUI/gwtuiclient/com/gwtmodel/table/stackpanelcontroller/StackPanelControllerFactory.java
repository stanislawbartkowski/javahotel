/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stackpanelcontroller;

import javax.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.controlbuttonview.StackPanelButtonFactory;
import com.gwtmodel.table.view.stackpanel.ViewStackPanelFactory;
import java.util.List;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class StackPanelControllerFactory {

    private final StackPanelButtonFactory baFactory;
    private final ViewStackPanelFactory fFactory;

    @Inject
    public StackPanelControllerFactory(StackPanelButtonFactory baFactory,
            ViewStackPanelFactory fFactory) {
        this.baFactory = baFactory;
        this.fFactory = fFactory;
    }

    public IStackPanelController construct(IDataType dType,
            List<ControlButtonDesc> bList, String html) {
        return baFactory.construct(dType, bList, html);
    }

    public IStackPanelController constructDownMenu(IDataType dType,
            String downMenuImage, List<ControlButtonDesc> bList) {
        return new MenuPanelController(dType, downMenuImage, bList);
    }

    public IStackPanelController constructStackMenu(IDataType dType,
            List<ControlButtonDesc> bList) {
        return new StackViewPanelController(dType, fFactory, bList);
    }
}
