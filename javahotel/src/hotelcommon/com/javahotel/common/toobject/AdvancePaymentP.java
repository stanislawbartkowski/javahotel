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
public class AdvancePaymentP extends AbstractTo implements INumerable {

    private Long id;
    private OperationData op;
    private DateP validationDate;
    private DecimalP amount;
    private Integer lp;

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
        case id:
            cla = Long.class;
            break;
        case dateOp:
        case validationDate:
            cla = Date.class;
            break;
        case lp:
            cla = Integer.class;
            break;
        case amount:
            cla = BigDecimal.class;
            break;
        }
        return cla;
    }

    public enum F implements IField {

        id, dateOp, validationDate, amount, remarks, lp
    };

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return getId();
        case dateOp:
            return getDateOp();
        case validationDate:
            return getValidationDate();
        case amount:
            return getAmount();
        case remarks:
            return getRemarks();
        case lp:
            return getLp();
        }
        return null;

    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case id:
            setId((Long) o);
            break;
        case dateOp:
            setDateOp((Date) o);
            break;
        case validationDate:
            setValidationDate((Date) o);
            break;
        case amount:
            setAmount((BigDecimal) o);
            break;
        case remarks:
            setRemarks((String) o);
            break;
        case lp:
            setLp((Integer) o);
            break;
        }
    }

    public AdvancePaymentP() {
        op = new OperationData();
        validationDate = new DateP();
        amount = new DecimalP();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOp() {
        return op.getDateOp();
    }

    public void setDateOp(final Date dateOp) {
        op.setDateOp(dateOp);
    }

    public Date getValidationDate() {
        return validationDate.getD();
    }

    public void setValidationDate(final Date validationDate) {
        this.validationDate.setD(validationDate);
    }

    public BigDecimal getAmount() {
        return amount.getDecim();
    }

    public void setAmount(BigDecimal amount) {
        this.amount.setDecim(amount);
    }

    public String getRemarks() {
        return op.getRemarks();
    }

    public void setRemarks(final String remarks) {
        op.setRemarks(remarks);
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }
}
