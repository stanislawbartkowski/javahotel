/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.common;

import java.util.Date;

/**
 * Class for keeping period of time
 * 
 * @author stanislawbartkowski@gmail.com
 */

public class PeriodT {

    /** The beginning of period. */
    private final Date from;
    /** The end of period. */
    private Date to;
    /** Additional object attached to period. */
    private Object i;

    public PeriodT(final Date d1, final Date d2, final Object i) {
        this.from = d1;
        this.to = d2;
        this.i = i;
    }

    public PeriodT(final Date d1, final Date d2) {
        this.from = d1;
        this.to = d2;
        this.i = null;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public Object getI() {
        return i;
    }

    public void setI(Object i) {
        this.i = i;
    }

    public void setTo(final Date to) {
        this.to = to;
    }
}
