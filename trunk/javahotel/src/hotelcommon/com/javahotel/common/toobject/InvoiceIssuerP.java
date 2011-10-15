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
package com.javahotel.common.toobject;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.common.command.CommandUtil;

/**
 * @author hotel Transient Object related to InvoiceIssuer
 */

@SuppressWarnings("serial")
public class InvoiceIssuerP extends CustomerP {

    private Integer paymentDays; // time (number of day) for payment
    private String townMaking; // place where invoice was made
    private String personMaking; // person making invoice

    /**
     * @return the paymentDays
     */
    public Integer getPaymentDays() {
        return paymentDays;
    }

    /**
     * @param paymentDays
     *            the paymentDays to set
     */
    public void setPaymentDays(Integer paymentDays) {
        this.paymentDays = paymentDays;
    }

    /**
     * @return the townMaking
     */
    public String getTownMaking() {
        return townMaking;
    }

    /**
     * @param townMaking
     *            the townMaking to set
     */
    public void setTownMaking(String townMaking) {
        this.townMaking = townMaking;
    }

    /**
     * @return the personMaking
     */
    public String getPersonMaking() {
        return personMaking;
    }

    /**
     * @param personMaking
     *            the personMaking to set
     */
    public void setPersonMaking(String personMaking) {
        this.personMaking = personMaking;
    }

    public enum F implements IField {
        paymentDays, townMaking, personMaking, bankAccount
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addT(F.values(), super.getT());
    }

    @Override
    public Object getF(IField f) {
        if (!isOnList(f, F.values())) {
            return super.getF(f);
        }
        F fi = (F) f;
        switch (fi) {
        case paymentDays:
            return getPaymentDays();
        case townMaking:
            return getTownMaking();
        case personMaking:
            return getPersonMaking();
        case bankAccount:
            return getBankAccount();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        if (!isOnList(f, F.values())) {
            super.setF(f, o);
            return;
        }
        F fi = (F) f;
        switch (fi) {
        case paymentDays:
            setPaymentDays((Integer) o);
            break;
        case townMaking:
            setTownMaking((String) o);
            break;
        case personMaking:
            setPersonMaking((String) o);
            break;
        case bankAccount:
            setBankAccount((String) o);
            break;
        }

    }

    public void setBankAccount(String acc) {
        List<BankAccountP> bList = new ArrayList<BankAccountP>();
        BankAccountP bacc = new BankAccountP();
        bacc.setAccountNumber(acc);
        bList.add(bacc);
        this.setAccounts(bList);
    }

    public String getBankAccount() {
        List<BankAccountP> bList = this.getAccounts();
        if ((bList == null) || bList.isEmpty()) {
            return null;
        }
        return bList.get(0).getAccountNumber();
    }

}
