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
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hotel
 * 
 */
public class ToEditRecord implements Serializable {

    private int recId;

    /**
     * @return the recId
     */
    public int getRecId() {
        return recId;
    }

    /**
     * @param recId
     *            the recId to set
     */
    public void setRecId(int recId) {
        this.recId = recId;
    }

    private boolean mark;

    private String name;

    private Date date;

    private BigDecimal number;

    private String nameS;

    /**
     * @return the mark
     */
    public boolean isMark() {
        return mark;
    }

    /**
     * @param mark
     *            the mark to set
     */
    public void setMark(boolean mark) {
        this.mark = mark;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the nameS
     */
    public String getNameS() {
        return nameS;
    }

    /**
     * @param nameS
     *            the nameS to set
     */
    public void setNameS(String nameS) {
        this.nameS = nameS;
    }

}
