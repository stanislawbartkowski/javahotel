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
package com.gwtmodel.table.stackpanelcontroller;

import com.google.inject.Inject;
import com.gwtmodel.table.view.stack.StackButton;
import com.gwtmodel.table.view.stack.StackPanelFactory;
import java.util.List;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class StackPanelControllerFactory {

    private final StackPanelFactory paFactory;

    @Inject
    public StackPanelControllerFactory(StackPanelFactory paFactory) {
        this.paFactory = paFactory;
    }

    public IStackPanelController construct(List<StackButton> bList, String html) {
        return new StackPanelController(paFactory, bList, html);
    }
}
