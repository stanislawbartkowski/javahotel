/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import java.util.Date;

import com.gwtmodel.table.common.dateutil.DateP;


/**
 * @author hotel
 * 
 */
@SuppressWarnings("serial")
public class OperationData implements Serializable {

    private DateP dateOp;
    private String remarks;
    private String personOp;

    OperationData() {
        this.dateOp = new DateP();
    }

    /**
     * @return the dateOp
     */
    Date getDateOp() {
        return dateOp.getD();
    }

    /**
     * @param dateOp
     *            the dateOp to set
     */
    void setDateOp(Date dateOp) {
        this.dateOp.setD(dateOp);
    }

    /**
     * @return the remarks
     */
    String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            the remarks to set
     */
    void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the personOp
     */
    String getPersonOp() {
        return personOp;
    }

    /**
     * @param personOp
     *            the personOp to set
     */
    void setPersonOp(String personOp) {
        this.personOp = personOp;
    }

}
