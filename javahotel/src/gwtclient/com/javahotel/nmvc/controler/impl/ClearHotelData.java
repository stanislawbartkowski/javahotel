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
package com.javahotel.nmvc.controler.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.gwtmodel.table.IClickNextYesNo;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.TemplateContainerSlotable;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VModelData;

public class ClearHotelData extends TemplateContainerSlotable<IDataControler> {

//    private final IDataControler iData;
    private final ClickButtonType cClear;
    private final DataType daType;
    private final static String CUSTOM = "CUSTOM-BUTTOM";

//    @Override
//    public void replaceSlContainer(SlotListContainer sl) {
//        iData.replaceSlContainer(sl);
//    }

    private class DelC extends CommonCallBack<Object> {

        @Override
        public void onMySuccess(Object arg) {
            Window.alert("OK - dane usunięte !");
        }
    }

    private class ConfDelete implements IClickNextYesNo {

        private final HotelP ho;

        ConfDelete(HotelP ho) {
            this.ho = ho;
        }

        @Override
        public void click(boolean yes) {
            if (yes) {
                GWTGetService.getService().clearHotelData(ho, new DelC());
            }
        }
    }

    private class ClearData implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext sl) {
            ISlotSignalContext ret = getSlContainer().getGetterContext(
                    GetActionEnum.GetListLineChecked, daType);
            IVModelData vData = ret.getVData();
            WSize wSize = ret.getWSize();

            VModelData m = (VModelData) vData;
            HotelP ho = (HotelP) m.getA();
            String hName = ho.getName();
            YesNoDialog yes = new YesNoDialog("Usunąć wszystkie dane z hotelu "
                    + hName + "?", null, new ConfDelete(ho));
            yes.show(wSize);
        }
    }

    public ClearHotelData(IResLocator rI, CellId panelId) {
        cClear = new ClickButtonType(CUSTOM);
        ControlButtonDesc bDesc = new ControlButtonDesc("MessageError32",
                "Usuń dane z hotelu", cClear);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bDesc);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        daType = new DataType(RType.AllHotels);
        // iData = tFactory.constructDataControler(daType, null, cList,
        // panelId);
        DisplayListControlerParam dList = tFactory.constructParam(daType, null,
                cList, panelId);
        iSlot = tFactory.constructDataControler(dList);
        // iData = dList.getListParam().
        getSlContainer().registerSubscriber(cClear, new ClearData());
    }

//    public SlotListContainer getSlContainer() {
//        return iData.getSlContainer();
//    }

    @Override
    public void startPublish(CellId cellId) {
        iSlot.startPublish(null);
    }

}
