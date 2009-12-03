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
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.bookelemlist.BookRowList;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.ExtractOfferPriceService;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.ICreateViewContext;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.client.param.ConfigParam;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.tableprice.TableSeasonPrice;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.PaymentRowP;

public class BookRoom extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private HorizontalPanel hp;
    private PriceRecord pri;
    private final BookRowList bElem;
    private final ExtractOfferPriceService priceService;
    private final TableSeasonPrice pa;
    private OfferSeasonP osP;

    @Inject
    public BookRoom(IResLocator rI, BookRowList bElem,
            ExtractOfferPriceService pService) {
        this.rI = rI;
        this.bElem = bElem;
        this.priceService = pService;
        this.pa = new TableSeasonPrice(ConfigParam.getStartWeek());
    }

    private class SetP implements ISetGwtWidget {

        public void setGwtWidget(IMvcWidget i) {
            hp.add(i.getWidget());
            hp.add(bElem.getMWidget().getWidget());
        }
    }
    
    private class ReadSeason implements IOneList {

        public void doOne(AbstractTo val) {
            osP = (OfferSeasonP) val;            
        }
        
    }


    private class ChangeListener implements IChangeListener {

        private final RecordField rFrom, rTo, serv;

        ChangeListener(RecordField rFrom, RecordField rTo, RecordField serv) {
            this.rFrom = rFrom;
            this.rTo = rTo;
            this.serv = serv;
        }

        public void onChange(ILineField i) {
            List<MapSpecialToI> col = pri.getCol();
            IDecimalTableView dView = pri.getDecV();
            List<BigDecimal> val = dView.getCols(0);
            OfferServicePriceP o = new OfferServicePriceP();
            priceService.ExtractOneLine(o, val, col, serv.getELine().getVal());
            
            OfferPriceP oP = new OfferPriceP();
            List<OfferServicePriceP> off = new ArrayList<OfferServicePriceP>();
            off.add(o);
            oP.setServiceprice(off);
            
//            priceService.ExtractOfferPrice(oP, dView, col);
            Date dFrom = rFrom.getELine().getDate();
            Date dTo = rTo.getELine().getDate();
            if ((dFrom == null) || (dTo == null)) {
                return;
            }
            pa.setPriceList(oP);
            pa.setPeriods(osP);
            
            List<PaymentRowP> li = pa.getPriceRows(serv.getELine().getVal(),
                    dFrom, dTo);
            bElem.drawTable(li);
        }

    }

    @Override
    public boolean getCustomView(ISetGwtWidget iSet, ICreateViewContext con,
            IContrButtonView i) {

        hp = new HorizontalPanel();
        IBookRoomConnector bRom = (IBookRoomConnector) iContx.getI();
        con.getIPanel().add(hp);
        VerticalPanel pad = new VerticalPanel();
        con.createDefaultDialog(pad);
        RecordField serv = null;
        RecordField rFrom = null;
        RecordField rTo = null;
        for (RecordField f : con.getModel().getFields()) {
            if (f.getFie() == BookElemP.F.service) {
                serv = f;
            }
            if (f.getFie() == BookElemP.F.checkIn) {
                rFrom = f;
            }
            if (f.getFie() == BookElemP.F.checkOut) {
                rTo = f;
            }
        }
        IChangeListener iC = new ChangeListener(rFrom, rTo, serv);
        rFrom.getELine().setChangeListener(iC);
        rTo.getELine().setChangeListener(iC);
        hp.add(pad);
        bElem.show();
        if (i != null) {
            VerticalPanel pa = new VerticalPanel();
            pa.add(i.getMWidget().getWidget());
            pad.add(pa);
        }
        pri = new PriceRecord(rI, new SetP(), bRom, serv.getELine());
        iSet.setGwtWidget(new DefaultMvcWidget(hp));
        
        CommandParam p = rI.getR().getHotelCommandParam();
        String season = bRom.getEseason().getE().getVal();
        p.setDict(DictType.OffSeasonDict);
        p.setRecName(season);
        rI.getR().getOne(RType.ListDict, p, new ReadSeason());

        return true;
    }

    @Override
    public void setFields(RecordModel mo) {
        List<PaymentRowP> li = (List<PaymentRowP>) mo.getAList();
        bElem.drawTable(li);
    }

}
