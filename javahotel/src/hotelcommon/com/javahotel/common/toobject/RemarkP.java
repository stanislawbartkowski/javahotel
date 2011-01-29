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

import java.sql.Timestamp;

import com.javahotel.types.DateP;
import com.javahotel.types.INumerable;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RemarkP extends AbstractToILd implements INumerable {

    private LId id;
    private String remark;
    private DateP addDate;
    private Integer lp;

    public enum F implements IField {

        id, remark, addDate, lp
    };

    public RemarkP() {
        addDate = new DateP();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(final String remark) {
        this.remark = remark;
    }

    public Timestamp getAddDate() {
        return addDate.getT();
    }

    public void setAddDate(Timestamp addDate) {
        this.addDate.setT(addDate);
    }

    public LId getId() {
        return id;
    }

    public void setId(LId id) {
        this.id = id;
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return id;
        case remark:
            return remark;
        case addDate:
            return addDate;
        case lp:
            return lp;
        }
        return null;
    }

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return Long.class;
        case remark:
            return String.class;
        case addDate:
            return DateP.class;
        case lp:
            return Integer.class;
        }
        return null;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case id:
            setId((LId) o);
            break;
        case remark:
            setRemark((String) o);
            break;
        case addDate:
            setAddDate((Timestamp) o);
            break;
        case lp:
            setLp((Integer) o);
            break;
        }
    }
}
