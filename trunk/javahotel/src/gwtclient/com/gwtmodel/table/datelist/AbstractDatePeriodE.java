/*
 *  Copyright 2010 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormatUtil;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hotel
 */
public abstract class AbstractDatePeriodE extends AbstractLpVModelData {

    private Date dFrom;
    private Date dTo;
    private String comment;

    @Override
    public List<IVField> getF() {
        IVField f[] = {
            new DatePeriodField(DatePeriodField.F.DATEFROM),
            new DatePeriodField(DatePeriodField.F.DATETO),
            new DatePeriodField(DatePeriodField.F.COMMENT)
        };
        List<IVField> li = Utils.toList(f);
        return addV(li);
    }

    @Override
    public String getS(IVField fie) {
        if (super.isValid(fie)) {
            return super.getS(fie);
        }
        Object val = getF(fie);
        if (val == null) {
            return null;
        }
        DatePeriodField d = (DatePeriodField) fie;
        if (d.getFie() == DatePeriodField.F.COMMENT) {
            return (String) val;
        }
        Date de = (Date) val;
        return DateFormatUtil.toS(de);
    }

    @Override
    public Object getF(IVField fie) {
        if (super.isValid(fie)) {
            return super.getS(fie);
        }
        DatePeriodField d = (DatePeriodField) fie;
        switch (d.getFie()) {
            case DATEFROM:
                return getdFrom();
            case DATETO:
                return getdTo();
            case COMMENT:
                return getComment();
        }
        return null;
    }

    @Override
    public boolean isEmpty(IVField fie) {
        DatePeriodField d = (DatePeriodField) fie;
        switch (d.getFie()) {
            case DATEFROM:
                return getdFrom() == null;
            case DATETO:
                return getdTo() == null;
            case COMMENT:
                return CUtil.EmptyS(getComment());
        }
        return true;
    }

    @Override
    public void setF(IVField fie, Object val) {
        if (super.isValid(fie)) {
            super.setF(fie, val);
            return;
        }
        DatePeriodField d = (DatePeriodField) fie;
        switch (d.getFie()) {
            case DATEFROM:
                setdFrom(DateFormatUtil.DToD(val));
                break;
            case DATETO:
                setdTo(DateFormatUtil.DToD(val));
                break;
            case COMMENT:
                setComment((String) val);
                break;
        }
    }

    /**
     * @return the dFrom
     */
    public Date getdFrom() {
        return dFrom;
    }

    /**
     * @param dFrom the dFrom to set
     */
    public void setdFrom(Date dFrom) {
        this.dFrom = dFrom;
    }

    /**
     * @return the dTo
     */
    public Date getdTo() {
        return dTo;
    }

    /**
     * @param dTo the dTo to set
     */
    public void setdTo(Date dTo) {
        this.dTo = dTo;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

}
