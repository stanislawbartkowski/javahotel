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

package com.javahotel.common.rescache;

import com.javahotel.common.dateutil.PeriodT;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ReadResParam {

    private final Collection<String> resList;
    private final PeriodT pe;
    
    public ReadResParam(final Collection<String> co, final PeriodT pe) {
        this.resList = co;
        this.pe = pe;
    }

    public Collection<String> getResList() {
        return resList;
    }

    public PeriodT getPe() {
        return pe;
    }

}
