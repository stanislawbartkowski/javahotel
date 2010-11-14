/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.ReadDictList;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.factories.IPersistFactoryAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.grid.IGridViewDecimal;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.nmvc.pricemodel.ISeasonPriceModel;
import com.javahotel.nmvc.pricemodel.PriceSeasonModelFactory;

public class PriceListContainer extends AbstractSlotContainer {

    private final GridViewFactory gFactory;
    private final IGridViewDecimal iView;
    private final PriceSeasonModelFactory prFactory;
    private final IResLocator rI;

    private String aSeason = null;

    private final DrawP synch = new DrawP();

    private class DrawP extends SynchronizeList {

        ISeasonPriceModel iModel;
        List<String> servNames;
        OfferPriceP priceP;

        DrawP() {
            super(3);
        }

        @Override
        protected void doTask() {
            int row = 0;
            for (String service : servNames) {
                List<BigDecimal> pList = iModel.getPrices(priceP, service);
                int col = 0;
                for (BigDecimal b : pList) {
                    iView.setRowDecimal(row, col, b);
                    col++;
                }
                row++;
            }
        }
    }

    private class R implements ReadDictList.IListCallBack<IDataListType> {

        @Override
        public void setList(IDataListType dList) {
            List<DictionaryP> servList = DataUtil.construct(dList);
            synch.servNames = DataUtil.fromDicttoString(servList);
            iView.setRowBeginning(synch.servNames);
            synch.signalDone();
        }
    }

    private class ReadSeason implements IOneList<OfferSeasonP> {

        @Override
        public void doOne(OfferSeasonP val) {
            synch.iModel = prFactory.construct(val);
            List<String> cols = synch.iModel.pricesNames();
            iView.setCols("ceny", cols);
            synch.signalDone();
        }

    }

    private void setSeason(String season) {
        if (season == null) {
            return;
        }
        if (aSeason != null) {
            if (aSeason.equals(season)) {
                return;
            }
        }
        aSeason = season;
        if (aSeason.equals("")) {
            return;
        }
        CommandParam p = rI.getR().getHotelCommandParam();
        p.setDict(DictType.OffSeasonDict);
        p.setRecName(season);
        rI.getR().getOne(RType.ListDict, p, new ReadSeason());
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;
            synch.priceP = (OfferPriceP) vData.getA();
            String season = synch.priceP.getSeason();
            setSeason(season);
            synch.signalDone();
        }
    }

    private class ChangeSeason implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IFormLineView formLine = slContext.getChangedValue();
            String season = formLine.getVal();
            setSeason(season);
        }

    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;
            OfferPriceP priceP = (OfferPriceP) vData.getA();
            int row = 0;
            for (String service : synch.servNames) {
                List<BigDecimal> priceList = new ArrayList<BigDecimal>();
                for (int col = 0; col < synch.iModel.noPrices(); col++) {
                    priceList.add(iView.getCellDecimal(row, col));
                }
                synch.iModel.setPrices(priceP, service, priceList);
            }
            return slContext;
        }

    }

    public PriceListContainer(IPersistFactoryAction persistFactory,
            IDataType dType, IDataType cType) {
        gFactory = GwtGiniInjector.getI().getGridViewFactory();
        prFactory = HInjector.getI().getPriceSeasonModelFactory();
        rI = HInjector.getI().getI();
        iView = gFactory.constructDecimal(true, true, true);
        DataType daType = new DataType(DictType.ServiceDict);
        new ReadDictList<IDataListType>().readList(daType, new R());
        registerCaller(GetActionEnum.GetViewModelEdited, cType, new SetGetter());
        registerCaller(GetActionEnum.GetModelToPersist, cType, new SetGetter());
        registerSubscriber(DataActionEnum.DrawViewFormAction, cType,
                new DrawModel());
        registerSubscriber(dType, new VField(OfferPriceP.F.season),
                new ChangeSeason());
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(cellId, iView);

    }

}
