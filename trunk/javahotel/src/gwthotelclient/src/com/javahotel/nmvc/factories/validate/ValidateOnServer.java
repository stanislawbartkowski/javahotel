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
package com.javahotel.nmvc.factories.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.factories.persist.ConvertP;
import com.javahotel.nmvc.factories.persist.dict.IPersistRecord;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult;

/**
 * @author hotel
 * 
 */
class ValidateOnServer {

    private static class ValResult implements IPersistResult {

        private final DataType dType;
        private final ISlotable iSlo;
        private final ISlotSignalContext slContext;

        ValResult(DataType dType, ISlotable iSlo,
                ISlotSignalContext slContext) {
            this.dType = dType;
            this.iSlo = iSlo;
            this.slContext = slContext;
        }

        @Override
        public void success(PersistResultContext re) {
            String errMessage = re.getRet().getErrorMessage();
            if (CUtil.EmptyS(errMessage)) {
                P.publishValidSignal(iSlo, dType, null, slContext);
            } else {
                List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
                errMess.add(new InvalidateMess(null, errMessage));
                P.publishValidSignalE(iSlo, dType, errMess, slContext);
            }

        }

    }

    static void validateS(ISlotable iSlo, DataType da,
            PersistTypeEnum persistTypeEnum, IVModelData pData,
            ISlotSignalContext slContext) {
        IPersistRecord iPersist = HInjector.getI().getHotelPersistFactory()
                .construct(da, true);
        IPersistResult pResult = new ValResult(da, iSlo, slContext);
        HModelData ho;
        if (da.isDictType(DictType.InvoiceList)) {
            P.publishValidSignal(iSlo, da, null, slContext);
            return;
        }
        if (da.isAllPersons()) {
            AbstractTo a = ConvertP.toLoginP(pData);
            ho = VModelDataFactory.construct(a);
        } else if (da.isAllHotels() || da.isDictType()) {
            ho = (HModelData) pData;
        } else {
            P.publishValidSignal(iSlo, da, null, slContext);
            return;
        }
        iPersist.persist(persistTypeEnum, ho, pResult);
    }

}
