/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class OfferSeasonPeriodP extends AbstractTo {

    private DateP startP;
    private DateP endP;
    private SeasonPeriodT periodT;
    private String description;
    private Long pId;

    public enum F implements IField {

        startP, endP, periodT, description, pId
    }

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
            case startP:
            case endP:
                cla = Date.class;
                break;
            case pId:
                cla = Long.class;
                break;
            case periodT:
                cla = SeasonPeriodT.class;
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
        F fi = F.valueOf(f.toString());
        switch (fi) {
            case startP:
                return getStartP();
            case endP:
                return getEndP();
            case periodT:
                return getPeriodT();
            case description:
                return getDescription();
            case pId:
                return getPId();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = F.valueOf(f.toString());
        switch (fi) {
            case startP:
                setStartP((Date) o);
                break;
            case endP:
                setEndP((Date) o);
                break;
            case periodT:
                setPeriodT((SeasonPeriodT) o);
                break;
            case description:
                setDescription((String) o);
                break;
            case pId:
                setPId((Long) o);
                break;
        }
    }

    public OfferSeasonPeriodP() {
        startP = new DateP();
        endP = new DateP();
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

    public SeasonPeriodT getPeriodT() {
        return periodT;
    }

    public void setPeriodT(SeasonPeriodT periodT) {
        this.periodT = periodT;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getPId() {
        return pId;
    }

    public void setPId(Long pId) {
        this.pId = pId;
    }
}
