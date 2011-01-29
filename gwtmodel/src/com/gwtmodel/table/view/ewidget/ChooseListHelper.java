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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ChooseDictList;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;

/**
 * 
 * @author perseus
 */
abstract class ChooseListHelper {

    private final IDataType dType;

    ChooseListHelper(IDataType dType) {
        this.dType = dType;
    }

    abstract void asetValue(String sy);

    abstract void hide();

    private class ChooseD implements
            ChooseDictList.ICallBackWidget<IVModelData> {

        private final WidgetWithPopUpTemplate.ISetWidget iSet;

        ChooseD(WidgetWithPopUpTemplate.ISetWidget iSet) {
            this.iSet = iSet;
        }

        @Override
        public void setWidget(WSize ws, IGWidget w) {
            iSet.setWidget(w.getGWidget());
        }

        @Override
        public void setChoosed(IVModelData vData, IVField comboFie) {
            String sy = FUtils.getValueS(vData, comboFie);
            asetValue(sy);
            hide();
        }

        @Override
        public void setResign() {
            hide();
        }
    }

    private class PopU implements WidgetWithPopUpTemplate.IGetP {

        @Override
        public void getPopUp(Widget startW,
                WidgetWithPopUpTemplate.ISetWidget iSet) {
            ChooseDictList cList = new ChooseDictList(dType, new WSize(startW),
                    new ChooseD(iSet));
        }
    }

    WidgetWithPopUpTemplate.IGetP getI() {
        return new PopU();
    }

    ChooseDictList.ICallBackWidget<IVModelData> getC(
            WidgetWithPopUpTemplate.ISetWidget iSet) {
        return new ChooseD(iSet);
    }
}
