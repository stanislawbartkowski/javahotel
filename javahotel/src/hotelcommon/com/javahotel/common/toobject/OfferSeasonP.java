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

import com.javahotel.common.command.CommandUtil;
import com.javahotel.types.DateP;
import java.util.List;
import java.util.Date;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class OfferSeasonP extends DictionaryP {

    private DateP startP;
    private DateP endP;

    public OfferSeasonP() {
        super();
        startP = new DateP();
        endP = new DateP();
    }

    private List<OfferSeasonPeriodP> periods;

    public List<OfferSeasonPeriodP> getPeriods() {
        return periods;
    }

    public void setPeriods(final List<OfferSeasonPeriodP> periods) {
        this.periods = periods;
    }

    public enum F implements IField {

        startp, endp
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
        case startp:
        case endp:
            return Date.class;
        }
        return null;
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;

        switch (fi) {
        case startp:
            return getStartP();
        case endp:
            return getEndP();
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
        case startp:
            setStartP((Date) o);
            break;
        case endp:
            setEndP((Date) o);
            break;
        }
    }

    public Date getStartP() {
        return startP.getD();
    }

    public void setStartP(final Date startP) {
        this.startP.setD(startP);
    }

    public Date getEndP() {
        return endP.getD();
    }

    public void setEndP(final Date endP) {
        this.endP.setD(endP);
    }
}
