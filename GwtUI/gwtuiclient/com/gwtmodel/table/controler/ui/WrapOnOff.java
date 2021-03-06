/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.controler.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author hotel
 * 
 */
public class WrapOnOff extends PopupPanel {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, WrapOnOff> {
    }

    @UiField
    CheckBox checkWrap;

    @UiField
    Button EndButton;

    public WrapOnOff() {
        super(true);
        setWidget(binder.createAndBindUi(this));
    }

    @UiHandler("EndButton")
    void onFinishButtonClick(ClickEvent event) {
        this.hide();
    }

}
