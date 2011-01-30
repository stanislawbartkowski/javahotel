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
package com.javahotel.common.scrollseason.model;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class YearMonthPe {

    private final int year;
    private final int month;
    private final int noDays;
    private final int startT;

    YearMonthPe(int year, int month, int noDays, int startT) {
        this.year = year;
        this.month = month;
        this.noDays = noDays;
        this.startT = startT;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the noDays
     */
    public int getNoDays() {
        return noDays;
    }

    /**
     * @return the startT
     */
    public int getStartT() {
        return startT;
    }
}
