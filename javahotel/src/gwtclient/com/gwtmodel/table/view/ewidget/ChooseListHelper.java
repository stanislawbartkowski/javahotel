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
import com.gwtmodel.table.ReadDictList.IListCallBack;
import com.gwtmodel.table.WSize;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
abstract class ChooseListHelper {

    private IVField wybId;
    private final IDataType dType;

    ChooseListHelper(IDataType dType) {
        this.dType = dType;
    }

    abstract void asetValue(String sy);

    abstract void adrawRej(RejestrPlik rej, WidgetWithPopUpTemplate.IGetP iGet, String pId);


    /**
     * @param wybId the wybId to set
     */
    public void setWybId(IVField wybId) {
        this.wybId = wybId;
    }

    private class W implements IWybierzList {

        public void click(IVModelData l, boolean choosed) {
            if (choosed) {
                String sy = l.getS(wybId);
                asetValue(sy);
            }
            getPopUp().hideP();
        }
    }

    private class ChooseD implements ChooseDictList.ICallBackWidget {

        private final WidgetWithPopUpTemplate.ISetWidget iSet;

        ChooseD(WidgetWithPopUpTemplate.ISetWidget iSet) {
            this.iSet = iSet;
        }

        public void setWidget(IGWidget w) {
            iSet.setWidget(w.getGWidget());
        }

        public void setChoosed(IVModelData vData) {
            String sy = vData.getS(wybId);
            asetValue(sy);
            getPopUp().hideP();
        }

        public void setResign() {
            getPopUp().hideP();
        }

    }

    private class PopU implements WidgetWithPopUpTemplate.IGetP {

        public void getPopUp(Widget startW, WidgetWithPopUpTemplate.ISetWidget iSet) {
            ChooseDictList cList = new ChooseDictList(dType,new WSize(startW), new ChooseD(iSet));

        }
    }

    private class ReadR implements IBackRejestr, IListCallBack<RejTableView> {

        public void onRSuccessRej(RejestrPlik rej) {
            wNazwa = rej.getWyb().getParam(CommonConsts.TNAZWA);
            setWybId(new VField(rej.getWyb().getParam(CommonConsts.POLEWYB)));
            prej = rej;
            adrawRej(rej, new PopU(), rej.getWyb().getParam(CommonConsts.POLEWYB));
            co = new ArrayList<ListColumn>();
            for (ReportParam pa : rej.getWyb().getPar()) {
                String id = pa.getPara(CommonConsts.KOLUMNAID);
                String nazwa = pa.getPara(CommonConsts.KOLUMNA);
                co.add(new ListColumn(nazwa, id));
            }
        }

        public void setList(RejTableView dList) {
            onRSuccessRej(dList.getRej());
        }
    }

    IBackRejestr getIRej() {
        return new ReadR();
    }

    IListCallBack<RejTableView> getCallBack() {
        return new ReadR();
    }

    IWybierzList getIWyb() {
        return new W();
    }
}
