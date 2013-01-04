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
package com.mygwt.common.data;

import java.io.Serializable;
import java.util.Date;

import com.gwtmodel.table.common.dateutil.DateP;

/**
 * @author hotel
 * Transient object related to ItemRecord entity bean
 */
@SuppressWarnings("serial")
public class TOItemRecord implements Serializable {

    private String iName;

    private DateP iDate;

    private Integer iNumber;
    
    private Integer rootLevel;

    public TOItemRecord() {
        iDate = new DateP();

    }

    /**
     * @param iName
     *            the iName to set
     */
    public void setiName(String iName) {
        this.iName = iName;
    }

    /**
     * @param iDate
     *            the iDate to set
     */
    public void setiDate(Date iDate) {
        this.iDate.setD(iDate);
    }

    /**
     * @param iNumber
     *            the iNumber to set
     */
    public void setiNumber(Integer iNumber) {
        this.iNumber = iNumber;
    }

    /**
     * @return the iName
     */
    public String getiName() {
        return iName;
    }

    /**
     * @return the iDate
     */
    public Date getiDate() {
        return iDate.getD();
    }

    /**
     * @return the iNumber
     */
    public Integer getiNumber() {
        return iNumber;
    }

    /**
     * @return the rootLevel
     */
    public Integer getRootLevel() {
        return rootLevel;
    }

    /**
     * @param rootLevel the rootLevel to set
     */
    public void setRootLevel(Integer rootLevel) {
        this.rootLevel = rootLevel;
    }
    
    

}
