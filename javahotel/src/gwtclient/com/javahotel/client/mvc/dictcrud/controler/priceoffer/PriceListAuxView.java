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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.SynchronizeList;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.price.PriceListErrorContext;
import com.javahotel.client.mvc.dict.validator.price.PriceListValidatorService;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.IBeforeViewSignal;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.client.mvc.util.GetFieldModif;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.util.StringU;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PriceListAuxView extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private final IDecimalTableView tView;
    private final GetSeasonSpecial sS;
    private final GetFieldModif fModif;
    private boolean valStarted;
    private SyncC sync;
    private final static int HWIDTH = 30;
    private final GetRecordDefFactory gFactory;
    private final PriceListValidatorService vService;

    @Inject
    public PriceListAuxView(IResLocator rI, IDecimalTableView tView,
            GetSeasonSpecial sS,GetRecordDefFactory gFactory,PriceListValidatorService vService) {
        this.rI = rI;
        this.tView = tView;
        this.sS = sS;
        fModif = new GetFieldModif(OfferPriceP.F.season);
        fModif.setChangeL(new ChangeL());
        this.gFactory = gFactory;
        this.vService = vService;
    }

    public Object getAuxO() {
        ISeasonPriceModel iM = new ISeasonPriceModel() {

            public IDecimalTableView getT() {
                return tView;
            }

            public void setSpecial(String sName, ISpecialMap ma) {
                sS.runSpecial(sName, ma);
            }
        };
        return iM;
    }

    public IMvcWidget getMWidget() {
        return tView.getMWidget();
    }

    private class SyncC extends SynchronizeList {

        private OfferPriceP oP;
        private String sName;

        SyncC() {
            super(2);
            oP = null;
            sName = null;
        }

        @Override
        protected void doTask() {
            valStarted = true;
            if (StringU.isEmpty(oP.getSeason())) {
                return;
            }
            if (!oP.getSeason().equals(sName)) {
                return;
            }
            SetTableVal.setVal(rI, sS, tView, oP);
        }

        /**
         * @param oP
         *            the oP to set
         */
        void setOP(OfferPriceP oP) {
            this.oP = oP;
        }

        /**
         * @param sName
         *            the sName to set
         */
        void setSName(String sName) {
            this.sName = sName;
        }
    }

    private class SetC implements ISpecialMap {

        private final StorePrices sto;
        private final String sName;

        SetC(StorePrices sto, String sName) {
            this.sto = sto;
            this.sName = sName;
        }

        public void set(List<MapSpecialToI> col) {
            List<ColsHeader> co = new ArrayList<ColsHeader>();
            setTitle(co);
            for (MapSpecialToI m : col) {
                co.add(new ColsHeader(m.getName(), HWIDTH));
            }
            tView.setCols(new ColsHeader("Usługi", 70), co);
            sync.setSName(sName);
            sync.signalDone();
            if (sto != null) {
                sto.restore();
            }
        }
    }

    private void setSeason(String season, StorePrices sto) {
        ISpecialMap i = new SetC(sto, season);
        sS.runSpecial(season, i);
    }

    private class ChangeL implements IChangeListener {

        public void onChange(ILineField arg0) {
            ILineField season = (ILineField) arg0;
            String s = season.getVal();
            if (StringU.isEmpty(s)) {
                return;
            }
            StorePrices sto = null;
            if (valStarted) {
                sto = new StorePrices(tView);
            }
            setSeason(s, sto);
        }
    }

    public IModifRecordDef getModif() {
        return fModif.getModif();
    }

    public IBeforeViewSignal getBSignal() {
        return new BSignal();
    }

    private class R implements RData.IVectorList {

        public void doVList(final List<? extends AbstractTo> val) {

            List<String> rows = new ArrayList<String>();
            for (AbstractTo a : val) {
                DictionaryP d = (DictionaryP) a;
                rows.add(d.getName());
            }
            tView.setRows(rows);
        }
    }

    private void setTitle(List<ColsHeader> cols) {
        List<String> pri = gFactory.getStandPriceNames();
        for (int i = 0; i <= ISeasonPriceModel.MAXSPECIALNO; i++) {
            cols.add(new ColsHeader("", 0));
        }
        for (int i = 0; i < pri.size(); i++) {
            cols.set(i, new ColsHeader(pri.get(i), HWIDTH));
        }
    }

    private class BSignal implements IBeforeViewSignal {

        public void signal(RecordModel mo) {
            valStarted = false;
            sync = new SyncC();
            sS.initSeason();
            tView.reset();
        }
    }

    @Override
    public void setFields(RecordModel mo) {
        OfferPriceP oP = (OfferPriceP) mo.getA();

        CommandParam p1 = rI.getR().getHotelCommandParam();
        p1.setDict(DictType.ServiceDict);
        rI.getR().getList(RType.ListDict, p1, new R());
        sync.setOP(oP);
        sync.signalDone();
    }

    @Override
    public void changeMode(int actionMode) {
        if (actionMode == IPersistAction.DELACTION) {
            tView.setEnable(false);
        } else {
            tView.setEnable(true);
        }
    }

    @Override
    public IRecordValidator getValidator() {
        vService.setErrContext(new PriceListErrorContext());
        return vService;
    }

    @Override
    public void showInvalidate(IErrorMessage co) {
        PriceListErrorContext pCon = (PriceListErrorContext) co;
    }


}
