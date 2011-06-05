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
package com.javahotel.nmvc.factories.cleardatahotel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.gwtmodel.table.IClickYesNo;
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
import com.javahotel.client.IImageGallery;
import com.javahotel.client.MM;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.HotelP;

public class ClearHotelData extends TemplateContainerSlotable<IDataControler> {

    private final ClickButtonType cClear;
    private final static String CUSTOM = "CUSTOM-BUTTOM";

    private class DelC extends CommonCallBack<ReturnPersist> {

        @Override
        public void onMySuccess(ReturnPersist arg) {
            Window.alert("OK - dane usunięte !");
        }
    }

    private class ConfDelete implements IClickYesNo {

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
                    GetActionEnum.GetListLineChecked, dType);
            IVModelData vData = ret.getVData();
            WSize wSize = ret.getWSize();

            if (wSize == null) {
                return;
            }

            HModelData m = (HModelData) vData;
            HotelP ho = (HotelP) m.getA();
            String hName = ho.getName();
            String q = MM.M().removeHoteDataQuestion(hName);
            YesNoDialog yes = new YesNoDialog(q, null, new ConfDelete(ho));
            yes.show(wSize);
        }
    }

    public ClearHotelData(CellId panelId) {

        cClear = new ClickButtonType(CUSTOM);
        ControlButtonDesc bDesc = new ControlButtonDesc(
                IImageGallery.REMOVEHOTELDATA, MM.L().RemoveHotelData(), cClear);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bDesc);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        dType = new DataType(RType.AllHotels);
        DisplayListControlerParam dList = tFactory.constructParam(dType, null,
                cList, panelId);
        iSlot = tFactory.constructDataControler(dList);
        getSlContainer().registerSubscriber(cClear, new ClearData());
    }

    // keep it as overrided
    @Override
    public void startPublish(CellId cellId) {
        iSlot.startPublish(null);
    }

}
