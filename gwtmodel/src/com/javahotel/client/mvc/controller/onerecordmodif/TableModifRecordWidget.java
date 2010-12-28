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
package com.javahotel.client.mvc.controller.onerecordmodif;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class TableModifRecordWidget extends AbstractModifWidget {

    private final IContrPanel iContr;
    private final IContrButtonView cV;
    private final HorizontalPanel hp = new HorizontalPanel();

    TableModifRecordWidget(IResLocator rI, IControlClick cl) {
        super(rI);
        ArrayList<ContrButton> dButton =
                new ArrayList<ContrButton>();
        dButton.add(new ContrButton(null, "Zmień dane", IMODIFDATA));
        dButton.add(new ContrButton(null, "Wybierz z listy", ICHOOSELIST));
        dButton.add(new ContrButton(null, "Szczegółowo", IMODIFDATADIALOG));
        iContr = ContrButtonFactory.getContr(rI, dButton);

        cV = ContrButtonViewFactory.getView(rI, iContr, cl);
        newBox = new CheckBox("Dodanie nowego");
        newBox.setEnabled(false);
        modBox = new CheckBox("Zmiana danych");
        modBox.setEnabled(false);
        VerticalPanel mP = new VerticalPanel();
        mP.add(newBox);
        mP.add(modBox);
        hp.add(mP);
        hp.add(cV.getMWidget().getWidget());
    }

    public Widget getWidget() {
        return hp;
    }
}
