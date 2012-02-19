/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.view.util.OkDialog;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.types.DataType;
import com.javahotel.nmvc.factories.persist.dict.IHotelPersistFactory;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;
import com.javahotel.nmvc.factories.persist.ui.InvalidBookingAction;

/**
 * @author hotel
 * 
 */
abstract class AbstractPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    protected final IResLocator rI;
    protected final IPersistRecord iPersist;
    protected final DataType da;
    protected IGWidget wButton = null;

    private void publishSuccess(PersistTypeEnum persistTypeEnum) {
        publish(dType, DataActionEnum.PersistDataSuccessSignal, persistTypeEnum);

    }

    private class AfterPersistBooking implements IPersistResult {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersistBooking(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void success(PersistResultContext re) {
            if (persistTypeEnum == PersistTypeEnum.REMOVE) {
                publishSuccess(persistTypeEnum);
                return;
            }
            assert wButton != null : LogT.getT().cannotBeNull();
            if (re.getRet().getIdName() != null) {
                String mess = M.M().OkBooking(re.getRet().getIdName());
                OkDialog ok = new OkDialog(mess, null, new ISignal() {

                    @Override
                    public void signal() {
                        publishSuccess(persistTypeEnum);
                    }

                });
                ok.show(wButton);
            } else {
                InvalidBookingAction.action(re, wButton);
            }
        }

    }

    private class AfterPersist implements IPersistResult {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersist(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void success(PersistResultContext re) {
            publishSuccess(persistTypeEnum);
        }
    }

    protected IPersistResult getPersist(PersistTypeEnum persistTypeEnum) {
        if (da.isBookingType()) {
            return new AfterPersistBooking(persistTypeEnum);
        }
        return new AfterPersist(persistTypeEnum);
    }

    AbstractPersistLayer(IDataType dd, IResLocator rI,
            IHotelPersistFactory iPersistFactory) {
        this.rI = rI;
        this.dType = dd;
        this.da = (DataType) dd;
        iPersist = iPersistFactory.construct(da, false);
    }

}
