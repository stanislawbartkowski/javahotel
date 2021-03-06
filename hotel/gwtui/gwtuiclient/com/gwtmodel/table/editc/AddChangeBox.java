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
package com.gwtmodel.table.editc;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.MM;

class AddChangeBox extends Composite {

    private final VerticalPanel hp = new VerticalPanel();
    private final CheckBox addNew = new CheckBox(MM.getL().AddNewRecord());
    private final CheckBox changeD = new CheckBox(MM.getL().ChangeRecord());
    private final EditChooseRecordFactory ecFactory;

    private class Checked implements ClickHandler {

        private final ITransferClick i;
        private final int what;

        Checked(ITransferClick i, int what) {
            this.i = i;
            this.what = what;
        }

        @Override
        public void onClick(ClickEvent event) {
            CheckBox b = (CheckBox) event.getSource();
            IChangeObject o = ecFactory.constructChangeObject(what,
                    b.isChecked(), new GWidget(b));
            i.signal(o);
        }
    }

    AddChangeBox(ITransferClick c) {
        ecFactory = GwtGiniInjector.getI().getEditChooseRecordFactory();
        initWidget(hp);
        hp.add(addNew);
        hp.add(changeD);
        addNew.addClickHandler(new Checked(c, IChangeObject.NEW));
        changeD.addClickHandler(new Checked(c, IChangeObject.CHANGE));
    }

    void setNewCheck(boolean set) {
        addNew.setValue(set);
    }

    void setChangeCheck(boolean set) {
        changeD.setValue(set);
    }

    boolean getNewCheck() {
        return addNew.isChecked();
    }

    boolean getChangeCheck() {
        return changeD.isChecked();
    }

    void setReadOnly(boolean readOnly) {
        addNew.setEnabled(!readOnly);
        changeD.setEnabled(!readOnly);
    }
}
