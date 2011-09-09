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

import java.math.BigDecimal;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.DecimalP;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class DownPaymentP extends AdvancePaymentP {

    private String resId;
    private LId customerId;
    private DecimalP sumPayment;

    public BigDecimal getSumPayment() {
        return sumPayment.getDecim();
    }

    public void setSumPayment(BigDecimal sumPayment) {
        this.sumPayment.setDecim(sumPayment);
    }

    public enum F implements IField {

        resId, sumPayment
    }
    
    @Override
    public IField[] getT() {
        return CommandUtil.addT(super.getT(),F.values());
    }

    
    public boolean isField(IField fType) {
        if (fType instanceof AdvancePaymentP.F) { return true; }
        if (fType instanceof F) { return true; }
        return false;
    }

    public DownPaymentP() {
        super();
        sumPayment = new DecimalP();
    }

    private static AdvancePaymentP.F dF(IField fType) {
        if (fType instanceof AdvancePaymentP.F) {
            return (AdvancePaymentP.F) fType;
        }
        return null;
    }

    @Override
    public Object getF(IField f) {
        AdvancePaymentP.F fd = dF(f);
        if (fd != null) {
            return super.getF(fd);
        }
        F fi = (F) f;
        switch (fi) {
            case resId:
                return getResId();
            case sumPayment:
                return getSumPayment();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        AdvancePaymentP.F fd = dF(f);
        if (fd != null) {
            super.setF(fd, o);
            return;
        }
        F fi = (F) f;
        switch (fi) {
            case resId:
                setResId((String) o);
                break;
            case sumPayment:
                setSumPayment((BigDecimal)o);
                break;
        }
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public LId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LId customerId) {
        this.customerId = customerId;
    }
}
