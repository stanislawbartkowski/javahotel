/*
 * Copyright 2016 stanislawbartkowski@gmail.com
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
package com.gwtmodel.table.view.ewidget.gwt;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.chooselist.ChooseListFactory;
import com.gwtmodel.table.chooselist.ICallBackWidget;
import com.gwtmodel.table.chooselist.IChooseList;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;

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

    private class ChooseD implements ICallBackWidget<IVModelData> {

        private final ISetGWidget iSet;

        ChooseD(ISetGWidget iSet) {
            this.iSet = iSet;
        }

        @Override
        public void setWidget(WSize ws, IGWidget w) {
            iSet.setW(w);
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

    private class PopU implements IRequestForGWidget {

        @Override
        public void run(IVField v, WSize startW, ISetGWidget iSet,
                ICommand close) {
            ChooseListFactory fa = GwtGiniInjector.getI()
                    .getChooseListFactory();
            IChooseList i = fa.constructChooseList(dType, startW, new ChooseD(
                    iSet));
        }
    }

    IRequestForGWidget getI() {
        return new PopU();
    }

    ICallBackWidget<IVModelData> getC(ISetGWidget iSet) {
        return new ChooseD(iSet);
    }
}
