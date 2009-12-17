/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.dictcrud.controler.bookroom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.GetSeasonSpecial;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.SetPriceForOffer;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.toobject.OfferPriceP;

class PriceRecord {

    private final ISetGwtWidget iSet;
    private final IDecimalTableView decV;
    private final IResLocator rI;
    private final GetSeasonSpecial sS;
    private final SetPriceForOffer off;
    private final GetRecordDefFactory gFactory;

    private final ILineField serv;
    private List<MapSpecialToI> col;
    private OfferPriceP sP;

    List<MapSpecialToI> getCol() {
        return col;
    }

    IDecimalTableView getDecV() {
        return decV;
    }

    private void setNames() {
        List<String> co = new ArrayList<String>();
        setTitle(co);
        for (MapSpecialToI m : col) {
            co.add(m.getName());
        }
        decV.setRows(co);
    }

    private void drawPrice() {
        if (col == null) {
            return;
        }
        if (sP == null) {
            return;
        }
        if (serv.getVal() == null) {
            return;
        }
        List<BigDecimal> cols = off.createListPrice(col, sP, serv.getVal());
        decV.setColVal(0, cols);
    }

    private class SyncC extends SynchronizeList {

        SyncC() {
            super(2);
        }

        @Override
        protected void doTask() {
            setNames();
            drawPrice();
        }
    }

    private void setTitle(List<String> col) {
        ColsHeader co = new ColsHeader("Cena sugerowana");
        List<ColsHeader> li = new ArrayList<ColsHeader>();
        li.add(new ColsHeader(""));
        decV.setCols(co, li);
        List<String> rows = gFactory.getStandPriceNames();
        for (String s : rows) {
            col.add(s);
        }
    }

    private class SetC implements ISpecialMap {

        private final SyncC sC;

        SetC(SyncC sC) {
            this.sC = sC;
        }

        public void set(List<MapSpecialToI> colp) {
            col = colp;
            sC.signalDone();
        }
    }

    private class CDict implements IOneList<OfferPriceP> {

        private final SyncC sC;

        CDict(SyncC sC) {
            this.sC = sC;
        }

        public void doOne(OfferPriceP val) {
            sP = val;
            sC.signalDone();
        }
    }

    PriceRecord(IResLocator rI, ISetGwtWidget i, IBookRoomConnector bRom,
            ILineField serv) {
        iSet = i;
        this.rI = rI;
        this.serv = serv;
        IChangeListener iC = new IChangeListener() {

            public void onChange(ILineField i) {
                drawPrice();
            }

        };
        gFactory = HInjector.getI().getGetRecordDefFactory();
        this.serv.setChangeListener(iC);
        SyncC sC = new SyncC();
        sS = HInjector.getI().getGetSeasonSpecial();
        decV = HInjector.getI().getDecimaleTableView();
        off = HInjector.getI().getSetPriceForOffer();
        i.setGwtWidget(decV.getMWidget());
        ColsHeader co = new ColsHeader("Cena sugerowana");
        List<ColsHeader> li = new ArrayList<ColsHeader>();
        li.add(new ColsHeader(""));
        decV.setCols(co, li);
        String season = bRom.getEseason().getE().getVal();
        String sprice = bRom.getEprice().getE().getVal();
        sS.runSpecial(season, new SetC(sC));
        CommandParam p = rI.getR().getHotelDictName(DictType.PriceListDict,
                sprice);
        p.setSeasonName(season);
        rI.getR().getOne(RType.ListDict, p, new CDict(sC));
    }



}
