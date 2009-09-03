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
import com.javahotel.client.dialog.DefaultMvcWidget;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RoomsAdmin implements IPanelCommand {

    private PokojePanel pa;
    private final IResLocator rI;

    RoomsAdmin(IResLocator rI) {
        this.rI = rI;
    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        pa = new PokojePanel(rI);
        iSet.setGwtWidget(new DefaultMvcWidget(pa.getMWidget().getWidget()));
    }

    public void drawAction() {
    }
}
