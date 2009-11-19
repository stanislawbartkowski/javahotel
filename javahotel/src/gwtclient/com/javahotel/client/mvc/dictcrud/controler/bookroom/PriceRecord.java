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

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.GetSeasonSpecial;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.SetPriceForOffer;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.view.gwt.recordviewdef.GetRecordDefFactory;

class PriceRecord {

    private final ISetGwtWidget iSet;
    private final IDecimalTableView decV;
    private final IResLocator rI;
    private final GetSeasonSpecial sS;
    private final SetPriceForOffer off;

    private void setTitle(List<String> col) {
        ColsHeader co = new ColsHeader("Cena sugerowana");
        List<ColsHeader> li = new ArrayList<ColsHeader>();
        li.add(new ColsHeader(""));
        decV.setCols(co, li);
        List<String> rows = GetRecordDefFactory.getStandPriceNames();
        for (String s : rows) {
            col.add(s);
        }
    }

    private class SetC implements ISpecialMap {

        public void set(List<MapSpecialToI> col) {
            List<String> co = new ArrayList<String>();
            setTitle(co);
            for (MapSpecialToI m : col) {
                co.add(m.getName());
            }
            decV.setRows(co);
        }
    }

    PriceRecord(IResLocator rI, ISetGwtWidget i, IBookRoomConnector bRom, ILineField serv) {
        iSet = i;
        this.rI = rI;
        sS = HInjector.getI().getGetSeasonSpecial();
        decV = HInjector.getI().getDecimaleTableView();
        off = HInjector.getI().getSetPriceForOffer();
        i.setGwtWidget(decV.getMWidget());
        ColsHeader co = new ColsHeader("Cena sugerowana");
        List<ColsHeader> li = new ArrayList<ColsHeader>();
        li.add(new ColsHeader(""));
        decV.setCols(co, li);
        sS.runSpecial(bRom.getEseason().getE().getVal(), new SetC());
    }

}
