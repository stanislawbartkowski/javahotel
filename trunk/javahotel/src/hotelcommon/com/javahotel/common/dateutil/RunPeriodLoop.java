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
package com.javahotel.common.dateutil;

import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public abstract class RunPeriodLoop {

    protected abstract void visit(Date d);

    public void run(final PeriodT pe) {
        Date d = DateUtil.copyDate(pe.getFrom());
        visit(d);
        if (pe.getTo() == null) {
            return;
        }
        while (true) {
            if (DateUtil.eqDate(d, pe.getTo())) {
                break;
            }
            DateUtil.NextDay(d);
            visit(d);
        }
    }
}

