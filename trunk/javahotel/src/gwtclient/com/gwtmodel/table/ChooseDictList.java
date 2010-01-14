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
package com.gwtmodel.table;

import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotListContainer;

/**
 *
 * @author perseus
 */
public class ChooseDictList<T extends IVModelData> {

    private final ICallBackWidget<T> i;
    private final SlotListContainer sl;
    private final IDataType dType;

    public interface ICallBackWidget<T extends IVModelData> {

        void setWidget(IGWidget w);

        void setChoosed(T vData);

        void setResign();
    }

    private class GetChoosed implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData vData = sl.getGetterIVModelData(GetActionEnum.GetListLineChecked, dType);
            if (vData == null) {
                return;
            }
            T t = (T) vData;
            i.setChoosed(t);
        }
    }

    private class GetResign implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            i.setResign();
        }
    }

    private class GetWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            i.setWidget(w);
        }
    }

    public ChooseDictList(IDataType dType, ICallBackWidget<T> i) {
        this.i = i;
        this.dType = dType;
        TableDataControlerFactory tFactory = GwtGiniInjector.getI().getTableDataControlerFactory();
        IDataControler iData = tFactory.constructListChooseControler(dType, 0, 1);
        sl = iData.getSlContainer();
        sl.registerSubscriber(ClickButtonType.StandClickEnum.CHOOSELIST, new GetChoosed());
        sl.registerSubscriber(ClickButtonType.StandClickEnum.RESIGNLIST, new GetResign());
        sl.registerSubscriber(0, new GetWidget());
        iData.startPublish(0);
    }
}
