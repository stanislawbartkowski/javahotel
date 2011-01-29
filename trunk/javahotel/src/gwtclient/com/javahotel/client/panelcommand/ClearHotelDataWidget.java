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
package com.javahotel.client.panelcommand;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.HotelP;

class ClearHotelDataWidget {

    private final ICrudControler iCrud;
    private final IResLocator rI;
    private static int CLEANACTION = IPersistAction.CUSTOMACTION;

    private class DelC extends CommonCallBack<Object> {

        @Override
        public void onMySuccess(Object arg) {
            Window.alert("OK - dane usunięte !");
        }
    }

    private class ConfDelete implements IClickYesNo {

        private final String ho;

        ConfDelete(String ho) {
            this.ho = ho;
        }

        public void click(boolean yes) {
            if (yes) {
                HotelP h = new HotelP();
                h.setName(ho);

                GWTGetService.getService().clearHotelData(h, new DelC());
            }
        }
    }

    private class CClick implements IControlClick {

        public void click(ContrButton co, Widget w) {
            ITableView i = iCrud.getTableView();
            HotelP hot = (HotelP) i.getClicked();
            if (hot == null) {
                return;
            }
            String ho = hot.getName();
            YesNoDialog y = new YesNoDialog(ho + " - usunąć dane z hotelu ?",
                    null, new ConfDelete(ho));
            y.show(w);
        }
    }

    ClearHotelDataWidget(IResLocator rI) {
        this.rI = rI;
        DictData d = new DictData(RType.AllHotels);
        RecordAuxParam param = new RecordAuxParam();
        ArrayList<ContrButton> rButton = new ArrayList<ContrButton>();
        rButton.add(new ContrButton("MessageError32", "Usuń dane z hotelu",
                CLEANACTION, true));
        IContrPanel iC = ContrButtonFactory.getContr(rI, rButton);
        param.setCPanel(iC);
        param.setIClick(new CClick());
        iCrud = HInjector.getI().getDictCrudControlerFactory().getCrud(d,
                param, null);
    }

    void drawData() {
        iCrud.drawTable();
    }

    IMvcWidget getW() {
        return iCrud.getMWidget();
    }
}
