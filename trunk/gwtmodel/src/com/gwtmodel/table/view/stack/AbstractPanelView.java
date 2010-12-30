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
package com.gwtmodel.table.view.stack;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.view.button.ImgButtonFactory;

/**
 *
 * @author perseus
 */
abstract class AbstractPanelView implements IStackPanelView {

    private final IClickStackButton click;

    protected AbstractPanelView(IClickStackButton click) {
        this.click = click;
    }

    private class Click implements ClickHandler {

        private final StackButton bu;
        private final IGFocusWidget bt;

        Click(StackButton bu, IGFocusWidget bt) {
            this.bu = bu;
            this.bt = bt;
        }

        public void onClick(ClickEvent event) {
            click.click(bu, bt.getGWidget());
        }
    }

    protected IGFocusWidget constructButton(StackButton bu) {
        IGFocusWidget bt = ImgButtonFactory.getButton(bu.getId(),
                bu.getDisplayName(), null);
//        bt.setWidth("100%");
        bt.addClickHandler(new Click(bu, bt));
        return bt;

    }
}
