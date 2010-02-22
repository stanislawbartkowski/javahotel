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
package com.javahotel.client.dialog;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictData {

    /**
     * @return the sE
     */
    public SpecE getSE() {
        return sE;
    }

    /**
     * @return the speT
     */
    public SeasonPeriodT getSpeT() {
        return speT;
    }

    public enum SpecE {

        SpecialPeriod, CustomerPhone, CustomerAccount, BookingHeader, ValidationHeader, BookingElem, RowPaymentElem, ObjectResConflict, AddPayment, ResTablePanel, ResGuestList, BillsList, AddPaymentList, LoginAdmin, LoginUser, FromReservHeader

    }

    private final DictType d;
    private final RType rt;
    private final SpecE sE;
    private final SeasonPeriodT speT;

    public DictData(final DictType d) {
        this.rt = RType.ListDict;
        this.d = d;
        this.sE = null;
        this.speT = null;
    }

    public DictData(final RType rt) {
        this.d = null;
        this.rt = rt;
        this.sE = null;
        this.speT = null;
    }

    public DictData(final SpecE e, final SeasonPeriodT te) {
        this.rt = null;
        this.d = null;
        this.sE = e;
        this.speT = te;
    }

    public DictData(final SpecE e) {
        this.rt = null;
        this.d = null;
        this.sE = e;
        this.speT = null;
    }

    public boolean isSe() {
        return sE != null;
    }

    public boolean isDa() {
        return d != null;
    }

    public boolean isRt() {
        return rt != null;
    }

    /**
     * @return the d
     */
    public DictType getD() {
        return d;
    }

    /**
     * @return the rt
     */
    public RType getRt() {
        return rt;
    }
}
