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
package com.javahotel.nmvc.factories.booking.elem;

import java.util.Date;
import java.util.List;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.nmvc.factories.booking.GetSlowC;

/**
 * @author hotel
 * 
 */
class PayElemInfo {

    private String season;
    private String sprice;
    private Date dFrom;
    private Date dTo;
    private String service;
    private List<PaymentRowP> pList;

    /**
     * @return the service
     */
    String getService() {
        return service;
    }

    private final IDataType bookType;
    private final IDataType dType;
    private final ISlotable iSlo;
    private final ICallContext iContext;
    private final ISlotable mainSlo;

    PayElemInfo(IDataType dType, IDataType bookType, ISlotable iSlo,
            ISlotable mainSlo, ICallContext iContext) {
        this.season = null;
        this.sprice = null;
        this.dFrom = null;
        this.dTo = null;
        this.dType = dType;
        this.bookType = bookType;
        this.iSlo = iSlo;
        this.mainSlo = mainSlo;
        this.iContext = iContext;
    }

    /**
     * @return the season
     */
    String getSeason() {
        return season;
    }

    /**
     * @return the sprice
     */
    String getSprice() {
        return sprice;
    }

    /**
     * @return the dFrom
     */
    Date getdFrom() {
        return dFrom;
    }

    /**
     * @return the dTo
     */
    Date getdTo() {
        return dTo;
    }

    private void setE() {
        this.season = (String) DataUtil.getO(mainSlo, bookType,
                BookingP.F.season);
        ICustomObject i = mainSlo.getSlContainer()
                .getGetterCustom(GetSlowC.GETSLOTS).getCustom();
        GetSlowC sC = (GetSlowC) i;
        this.sprice = (String) DataUtil.getO(sC.getiSlo(), new DataType(
                AddType.BookRecord), BookRecordP.F.oPrice);
    }

    void initW() {
        setE();
        if (iContext.getPersistTypeEnum() != PersistTypeEnum.ADD) {
            IVModelData vData = mainSlo.getSlContainer()
                    .getGetterContext(dType, GetActionEnum.GetListLineChecked)
                    .getVData();
            HModelData ho = (HModelData) vData;
            BookElemP p = (BookElemP) ho.getA();
            dFrom = p.getCheckIn();
            dTo = p.getCheckOut();
            service = p.getService();
            pList = p.getPaymentrows();
        }
    }

    /**
     * @return the pList
     */
    List<PaymentRowP> getpList() {
        return pList;
    }

    /**
     * @param pList
     *            the pList to set
     */
    void setpList(List<PaymentRowP> pList) {
        this.pList = pList;
    }

    void setFromW() {
        setE();
        this.service = (String) DataUtil.getO(iSlo, dType, BookElemP.F.service);
        this.dFrom = (Date) DataUtil.getO(iSlo, dType, BookElemP.F.checkIn);
        this.dTo = (Date) DataUtil.getO(iSlo, dType, BookElemP.F.checkOut);
    }

    boolean isNotDefined() {
        if (season == null) {
            return true;
        }
        if (sprice == null) {
            return true;
        }
        if (dFrom == null) {
            return true;
        }
        if (dTo == null) {
            return true;
        }
        if (sprice == null) {
            return true;
        }
        return false;
    }

}
