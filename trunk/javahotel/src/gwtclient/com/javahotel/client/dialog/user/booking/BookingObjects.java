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
package com.javahotel.client.dialog.user.booking;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ICreatedValue;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.util.DefDrawCol;
import com.javahotel.client.mvc.util.IDrawCol;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferSeasonP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookingObjects extends Composite {

    private ResRoomTable bO;
    private final IResLocator rI;
    private ILineField season;
    private PanelResCalendar pCa;
    private DrawResGrid dG;
    private DrawLock sSync;
    private final HorizontalPanel controlH = new HorizontalPanel();
    private final VerticalPanel sPanel = new VerticalPanel();
    private final HorizontalPanel seasonC = new HorizontalPanel();
    private final Date today = DateUtil.getToday();

    private void drawPa(final ILineField e) {
        final String sName = e.getVal();
        CommandParam co = rI.getR().getHotelCommandParam();
        co.setDict(DictType.OffSeasonDict);
        IDrawCol ic = new IDrawCol() {

            public void draw(final List<? extends AbstractTo> co) {
                OfferSeasonP oP = null;
                for (final AbstractTo a : co) {
                    oP = (OfferSeasonP) a;
                    if (oP.getName().equals(sName)) {
                        break;
                    }
                }
                if (oP != null) {
                    pCa.draPa(oP);
                }
            }
        };
        rI.getR().getList(RType.ListDict, co, new DefDrawCol(ic));

    }

    private class DrawLock extends SynchronizeList {

        private ILineField e;

        DrawLock() {
            super(2);
            e = null;
        }

        @Override
        protected void doTask() {
            drawPa(e);
        }

        /**
         * @param e
         *            the e to set
         */
        void setE(ILineField e) {
            this.e = e;
        }
    }

    public void draw() {
        this.sSync = new DrawLock();
        bO = new ResRoomTable(rI, sSync);
        sPanel.add(controlH);
        sPanel.add(bO.getG());
        IDrawPartSeason drawI = new IDrawPartSeason() {

            public void draw(final int sno, final int sto) {
                dG.draw();
            }

            public void setGwtWidget(IMvcWidget i) {
                // TODO Auto-generated method stub
            }
        };
        pCa = new PanelResCalendar(rI, bO.getG(), seasonC, bO.colS(), drawI,
                today);
        CommandParam p = rI.getR().getHotelCommandParam();
        p.setDict(DictType.OffSeasonDict);
        dG = new DrawResGrid(rI, bO, pCa, bO.getG(), today);

        ICreatedValue iV = new ICreatedValue() {

            public void createdSignal(final ILineField e) {
                sSync.setE(e);
                sSync.signalDone();
            }
        };
        season = GetIEditFactory.getListValuesBox(rI, RType.ListDict, p,
                DictionaryP.F.name, iV);

        IChangeListener cLi = new IChangeListener() {

            public void onChange(final ILineField arg0) {
                if (sSync.signalledAlready()) {
                    drawPa(season);
                }
            }
        };
        season.setChangeListener(cLi);
        controlH.add(season.getMWidget().getWidget());
        controlH.add(seasonC);

    }

    public BookingObjects(final IResLocator rI) {
        this.rI = rI;
        initWidget(sPanel);
    }
}
