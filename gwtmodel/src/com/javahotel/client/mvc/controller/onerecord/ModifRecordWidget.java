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
package com.javahotel.client.mvc.controller.onerecord;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ModifRecordWidget extends Composite {

    final static int IMODIFDATA = 0;
    final static int ICHOOSELIST = 1;

    private final IResLocator rI;
    private final CheckBox newBox;
    private final CheckBox modBox;
    private final IContrPanel iContr;
    private final IContrButtonView cV;

    ModifRecordWidget(IResLocator rI,IControlClick cl) {
        this.rI = rI;
        ArrayList<ContrButton> dButton =
                new ArrayList<ContrButton>();
        dButton.add(new ContrButton(null, "Zmień dane", IMODIFDATA));
        dButton.add(new ContrButton(null, "Wybierz z listy", ICHOOSELIST));
        iContr = ContrButtonFactory.getContr(rI, dButton);

        cV = ContrButtonViewFactory.getView(rI, iContr, cl);
        HorizontalPanel hp = new HorizontalPanel();
        newBox = new CheckBox("Dodanie nowego");
        newBox.setEnabled(false);
        modBox = new CheckBox("Zmiana danych");
        modBox.setEnabled(false);
        VerticalPanel mP = new VerticalPanel();
        mP.add(newBox);
        mP.add(modBox);
        hp.add(mP);
        hp.add(cV.getMWidget().getWidget());
        initWidget(hp);
    }

    void setModifValue(boolean modif) {
        modBox.setChecked(modif);
    }

    boolean IsModifValue() {
        return modBox.isChecked();
    }

    void setNewValue(boolean modif) {
        newBox.setChecked(modif);
    }
}
