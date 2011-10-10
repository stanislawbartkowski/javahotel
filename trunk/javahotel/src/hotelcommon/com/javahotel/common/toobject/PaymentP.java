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

import com.javahotel.common.command.PaymentMethod;
import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.INumerable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class PaymentP extends AbstractTo implements INumerable {

    /** Item no, consecutive number. */
    private Integer lp;
    private PaymentMethod payMethod;
    private boolean sumOp;
    private DecimalP amount;
    private DateP dateOp;
    private String remarks;
    private String personOp;
    private DateP datePayment;

    public PaymentP() {
        this.amount = new DecimalP();
        this.dateOp = new DateP();
        this.datePayment = new DateP();
        this.sumOp = true;
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }

    public String getPersonOp() {
        return personOp;
    }

    public void setPersonOp(String personOp) {
        this.personOp = personOp;
    }

    public Date getDatePayment() {
        return datePayment.getD();
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment.setD(datePayment);
    }

    public enum F implements IField {

        lp, payMethod, sumOp, amount, dateOp, remarks, personOp, datePayment
    }

    @Override
    public Class<?> getT(final IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
            case lp:
                cla = Integer.class;
                break;
            case payMethod:
                cla = PaymentMethod.class;
                break;
            case sumOp:
                cla = Boolean.class;
                break;
            case amount:
                cla = BigDecimal.class;
                break;
            case dateOp:
            case datePayment:
                cla = Date.class;
                break;
        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
            case lp:
                return getLp();
            case payMethod:
                return getPayMethod();
            case sumOp:
                return new Boolean(isSumOp());
            case amount:
                return getAmount();
            case dateOp:
                return getDateOp();
            case remarks:
                return getRemarks();
            case personOp:
                return getPersonOp();
            case datePayment:
                return getDatePayment();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
            case lp:
                setLp((Integer) o);
                break;
            case payMethod:
                setPayMethod((PaymentMethod) o);
                break;
            case sumOp:
                setSumOp(((Boolean) o).booleanValue());
                break;
            case amount:
                setAmount((BigDecimal) o);
                break;
            case dateOp:
                setDateOp((Date) o);
                break;
            case remarks:
                setRemarks((String) o);
                break;
            case personOp:
                setPersonOp((String) o);
                break;
            case datePayment:
                setDatePayment((Date) o);
                break;
        }
    }

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    public boolean isSumOp() {
        return sumOp;
    }

    public void setSumOp(boolean sumOp) {
        this.sumOp = sumOp;
    }

    public BigDecimal getAmount() {
        return amount.getDecim();
    }

    public void setAmount(BigDecimal amount) {
        this.amount.setDecim(amount);
    }

    public Date getDateOp() {
        return dateOp.getD();
    }

    public void setDateOp(final Date dateOp) {
        this.dateOp.setD(dateOp);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }
}
