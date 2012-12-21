/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.copy;

import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.SortNumerable;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.RemarkP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.Customer;
import com.javahotel.db.hotelbase.jpa.CustomerBankAccount;
import com.javahotel.db.hotelbase.jpa.CustomerPhoneNumber;
import com.javahotel.db.hotelbase.jpa.CustomerRemark;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.dbres.resources.IMess;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CopyCustomer {

    private CopyCustomer() {
    }

    static void copy1(final ICommandContext iC, final CustomerP sou,
            final Customer dest) {
        String pId = IMess.CUSTOMERPERSONPATTID;
        String patt = IMess.CUSTOMERPERSONPATT;
        if (sou.getCType() != CustomerType.Person) {
            pId = IMess.CUSTOMERCOMPANYPATTID;
            patt = IMess.CUSTOMERCOMPANYPATT;
        }
        CopyHelper.setPattName(iC, sou, pId, patt);
        CopyHelper.copyDict1(iC, sou, dest, FieldList.CustomerList);
        CopyHelper.checkPersonDateOp(iC, dest);
        final CopyBeanToP.ICopyHelper eqremark =
                new CopyHelper.IICopyHelper(FieldList.RemarkList) {

                    public Object getI(final Object se) {
                        return new CustomerRemark();
                    }
                };

        final CopyBeanToP.ICopyHelper eqphones =
                new CopyHelper.IICopyHelper(FieldList.PhoneList) {

                    public Object getI(final Object se) {
                        return new CustomerPhoneNumber();
                    }
                };

        final CopyBeanToP.ICopyHelper eqbank =
                new CopyHelper.IICopyHelper(FieldList.BankList) {

                    public Object getI(final Object se) {
                        return new CustomerBankAccount();
                    }
                };

        CopyBeanToP.copyRes1Collection(iC, sou, dest, "remarks", "customer",
                Customer.class, eqremark, true);
        CopyBeanToP.copyRes1Collection(iC, sou, dest, "phones", "customer",
                Customer.class, eqphones, true);
        CopyBeanToP.copyRes1Collection(iC, sou, dest, "accounts", "customer",
                Customer.class, eqbank, true);
    }

    static void copy2(final ICommandContext iC, final Customer sou,
            final CustomerP dest) {
        CopyHelper.copyDict2(iC,sou, dest, FieldList.CustomerList);
        HId id = sou.getId();
        dest.setId(ToLD.toLId(id));
        CopyHelper.copyRes2Collection(iC, sou, dest, "remarks",
                RemarkP.class);
        CopyHelper.copyRes2Collection(iC, sou, dest, "phones",
                PhoneNumberP.class);
        CopyHelper.copyRes2Collection(iC, sou, dest, "accounts",
                BankAccountP.class);
        SortNumerable.sortN(dest.getRemarks());
    }
}
