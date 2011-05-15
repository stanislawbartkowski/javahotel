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
package com.javahotel.client.user.season;

import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SeasonNames {

    private SeasonNames() {
    }

    static String getName(final IResLocator rI, final SeasonPeriodT t) {

        String sId = "HIGH";
        if (t != null) {
            sId = t.toString();
        }
        return (String) rI.getLabels().SeasonNames().get(sId);
    }
}
