/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ChooseDictList;
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

        public void setWidget(IGWidget w) {
            iSet.setWidget(w.getGWidget());
        }

        public void setChoosed(IVModelData vData, IVField comboFie) {
            String sy = vData.getS(comboFie);
            asetValue(sy);
            hide();
        }

        public void setResign() {
            hide();
        }
    }

    private class PopU implements WidgetWithPopUpTemplate.IGetP {

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
