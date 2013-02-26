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

package com.javahotel.common.rescache;

import java.util.List;

import com.gwtmodel.table.common.PeriodT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ReadResParam {

    private final List<String> resList;
    private final PeriodT pe;
    
    public ReadResParam(final List<String> co, final PeriodT pe) {
        this.resList = co;
        this.pe = pe;
    }

    public List<String> getResList() {
        return resList;
    }

    public PeriodT getPe() {
        return pe;
    }

}