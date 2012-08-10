/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;

/**
 * @author hotel
 * 
 */
public class ChangePageSize extends PopupPanel {

    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    TextBox startPageSize;
    @UiField
    TextBox currentPageSize;
    @UiField
    Button RestoreButton;
    @UiField
    Button ChangeButton;
    @UiField
    Button FinishButton;
    
    ICommand restoreClick;
    ICommand changeClick;
    ICommand finishClick;           

    interface Binder extends UiBinder<Widget, ChangePageSize> {
    }

    public ChangePageSize() {
        super(true);
        setWidget(binder.createAndBindUi(this));
    }

    @UiHandler("RestoreButton")
    void onRestoreButtonClick(ClickEvent event) {
        restoreClick.execute();
    }

    @UiHandler("ChangeButton")
    void onChangeButtonClick(ClickEvent event) {
        changeClick.execute();
    }

    @UiHandler("FinishButton")
    void onFinishButtonClick(ClickEvent event) {
        finishClick.execute();
    }
}
