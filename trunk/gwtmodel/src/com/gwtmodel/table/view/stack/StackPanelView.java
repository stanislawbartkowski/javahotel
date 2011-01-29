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
package com.gwtmodel.table.view.stack;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGFocusWidget;
import java.util.List;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
class StackPanelView extends AbstractPanelView {

    private final VerticalPanel vp = new VerticalPanel();

    StackPanelView(List<StackButton> bList,
            IClickStackButton click) {
        super(click);
        for (StackButton bu : bList) {
            IGFocusWidget bt = constructButton(bu);
            bt.getGWidget().setWidth("100%");
            vp.add(bt.getGWidget());
        }
        vp.setStyleName("stack-panel");

    }

    public Widget getGWidget() {
        return vp;
    }
}
