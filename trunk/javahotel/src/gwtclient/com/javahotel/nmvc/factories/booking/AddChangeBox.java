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
package com.javahotel.nmvc.factories.booking;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

class AddChangeBox extends Composite {

    private final VerticalPanel hp = new VerticalPanel();
    private final CheckBox addNew = new CheckBox("Dodaj nowego");
    private final CheckBox changeD = new CheckBox("Zmień dane");

    AddChangeBox() {
        initWidget(hp);
        hp.add(addNew);
        hp.add(changeD);
    }

    void setNewCheck(boolean set) {
        addNew.setValue(set);
    }

    void setChangeCheck(boolean set) {
        changeD.setValue(set);
    }

}