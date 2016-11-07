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
package com.gwtmodel.table.view.stackpanel;

import javax.inject.Inject;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.stackvertical.IStackPanelView;
import com.gwtmodel.table.view.stackvertical.StackPanelFactory;
import java.util.List;

/**
 *
 * @author perseus
 */
public class ViewStackPanelFactory {

    private final StackPanelFactory baFactory;

    @Inject
    public ViewStackPanelFactory(StackPanelFactory baFactory) {
        this.baFactory = baFactory;
    }

    public IStackPanelView construct(List<ControlButtonDesc> bList,
            IControlClick click) {
        return new ViewStackPanel(baFactory, bList, click);
    }

}
