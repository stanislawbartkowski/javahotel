/*
 * Copyright 2013 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.chooselist;

import com.gwtmodel.table.*;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.*;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
class ChooseDictList implements IChooseList {

    private final ICallBackWidget i;
    private final SlotListContainer sl;
    private final IDataType dType;

    private class GetChoosed implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData vData = sl.getGetterIVModelData(dType,
                    GetActionEnum.GetListLineChecked);
            if (vData == null) {
                return;
            }
            IVField comboFie = sl.getGetterComboField(dType);
            i.setChoosed(vData, comboFie);
        }
    }

    private class GetResign implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            i.setResign();
        }
    }

    private class GetWidget implements ISlotListener {

        private final WSize ws;

        GetWidget(WSize ws) {
            this.ws = ws;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            i.setWidget(ws, w);
        }
    }

    ChooseDictList(IDataType dType, WSize wSize, ICallBackWidget i) {
        this.i = i;
        this.dType = dType;
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();

        String s = c.getCustomValue(IGetCustomValues.HTMLPANELFORCHOOSELIST);
        DisplayListControlerParam cParam = tFactory.constructChooseParam(dType,
                wSize, new CellId(0), s);

        IDataControler iData = tFactory.constructDataControler(cParam);

        sl = iData.getSlContainer();
        sl.registerSubscriber(dType, ClickButtonType.StandClickEnum.CHOOSELIST,
                new GetChoosed());
        sl.registerSubscriber(dType, ClickButtonType.StandClickEnum.RESIGNLIST,
                new GetResign());
        sl.registerSubscriber(dType, 0, new GetWidget(wSize));

        iData.startPublish(new CellId(0));
    }
}
