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
package com.javahotel.common.toobject;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.DecimalP;
import java.math.BigDecimal;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class VatDictionaryP extends DictionaryP {

    public enum F implements IField {

        vat, defaultv
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
            case vat:
                return BigDecimal.class;
            case defaultv:
                return Boolean.class;
        }
        return null;
    }
    private DecimalP vatPercent;
    private boolean defVat;

    public VatDictionaryP() {
        vatPercent = new DecimalP();
        defVat = false;
    }

    public BigDecimal getVatPercent() {
        return vatPercent.getDecim();
    }

    public void setVatPercent(BigDecimal vatPercent) {
        this.vatPercent.setDecim(vatPercent);
    }

    public boolean isDefVat() {
        return defVat;
    }

    public void setDefVat(boolean defVat) {
        this.defVat = defVat;
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;

        switch (fi) {
            case vat:
                return getVatPercent();
            case defaultv:
                return isDefVat();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            super.setF(pfi, o);
            return;
        }

        F fi = (F) f;

        switch (fi) {
            case vat:
                setVatPercent((BigDecimal) o);
                break;
            case defaultv:
                Boolean b = (Boolean) o;
                setDefVat(b.booleanValue());
                break;
        }
    }
}
