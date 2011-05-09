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
package com.gwtmodel.table.stackpanelcontroller;

import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.controlbuttonview.StackPanelButtonFactory;
import com.gwtmodel.table.view.stack.StackPanelFactory;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */

public class StackPanelControllerFactory {

    private final StackPanelFactory paFactory;
    private final StackPanelButtonFactory baFactory;

    @Inject
    public StackPanelControllerFactory(StackPanelFactory paFactory,
            StackPanelButtonFactory baFactory) {
        this.paFactory = paFactory;
        this.baFactory = baFactory;
    }

    public IStackPanelController construct(IDataType dType, List<ControlButtonDesc> bList, String html) {
        return baFactory.construct(dType, bList, html);
//        return new StackPanelController(paFactory, bList, html, dType);
    }
}
