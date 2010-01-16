/*
 * Perseus spólka z o.o. 2001-2009
 * All rights reserved
 * Autor: Stanisław Bartkowski
 * stanislawbartkowski@gmail.com
 * 
 */
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.IDataType;

/**
 *
 * @author perseus
 */
class ChooseListField extends FieldTextField {

    private final ChooseListHelper rWyb;

    private class ReadR extends ChooseListHelper {

        ReadR(IServLocator rI, IDataType dType) {
            super(rI, dType);
        }

        @Override
        void asetValue(String sy) {
            setValue(sy);
        }

        @Override
        void adrawRej(RejestrPlik rej, IGetP iGet, String pId) {
            rWyb.getPopUp().init(sI, "Down", iGet, hPanel);
        }
    }

    ChooseListField(IServLocator sI, ReportDesc rap, String wybXml) {
        super(false);
        rWyb = new ReadR(sI, new RejDictData(rap, wybXml));
        sI.getC().readRejestr(rap, wybXml, rWyb.getIRej());
    }
}
