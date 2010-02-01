/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import java.io.Serializable;
import java.sql.Timestamp;

import com.javahotel.types.DateP;
import com.javahotel.types.ILd;
import com.javahotel.types.INumerable;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RemarkP implements Serializable, INumerable, ILd {

    private LId id;
    private String remark;
    private DateP addDate;
    private Integer lp;

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
}
