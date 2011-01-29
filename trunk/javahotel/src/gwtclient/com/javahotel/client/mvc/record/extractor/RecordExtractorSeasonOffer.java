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
package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.mvc.auxabstract.PeOfferNumTo;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.dictdata.model.ISeasonOffModel;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.SeasonPeriodT;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RecordExtractorSeasonOffer implements IRecordExtractor {

    private void setTable(OfferSeasonP oP, ICrudControler i, SeasonPeriodT t) {
        ArrayList<PeOfferNumTo> a = new ArrayList<PeOfferNumTo>();
        if (oP.getPeriods() != null) {
            for (OfferSeasonPeriodP o : oP.getPeriods()) {
                if (o.getPeriodT() == t) {
                    PeOfferNumTo pe = new PeOfferNumTo();
                    pe.setO(o);
                    a.add(pe);
                }
            }
        }
        i.getTableView().getModel().setList(a);
    }

    private void addTable(List<OfferSeasonPeriodP> col,
            ICrudControler i, SeasonPeriodT t) {
        for (int k = 0; k < i.getTableView().getModel().rowNum(); k++) {
            AbstractTo a = i.getTableView().getModel().getRow(k);
            PeOfferNumTo pe = (PeOfferNumTo) a;
            OfferSeasonPeriodP p = (OfferSeasonPeriodP) pe.getO();
            p.setPeriodT(t);
            col.add(p);
        }

    }

    public void toA(RecordModel mo, IRecordView view) {
        view.extractFields(mo);
        OfferSeasonP oP = (OfferSeasonP) mo.getA();
        ISeasonOffModel oM = (ISeasonOffModel) mo.getAuxData();
        List<OfferSeasonPeriodP> col = new ArrayList<OfferSeasonPeriodP>();
        addTable(col, oM.getTable1(), oM.getP1());
        addTable(col, oM.getTable2(), oM.getP2());
        oP.setPeriods(col);
    }

    public void toView(IRecordView view, RecordModel mo) {
        view.setFields(mo);
        OfferSeasonP oP = (OfferSeasonP) mo.getA();
        ISeasonOffModel oM = (ISeasonOffModel) mo.getAuxData();
        setTable(oP, oM.getTable1(), oM.getP1());
        setTable(oP, oM.getTable2(), oM.getP2());
    }
}
