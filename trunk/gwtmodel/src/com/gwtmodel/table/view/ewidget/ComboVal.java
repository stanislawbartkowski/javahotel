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
package com.gwtmodel.table.view.ewidget;

import com.gwtmodel.table.common.CUtil;

/**
 *
 * @author perseus
 */
public class ComboVal {

    private final String dispVal;

    public ComboVal(String dispVal) {
        this.dispVal = dispVal;
    }

    /**
     * @return the dispVal
     */
    public String getDispVal() {
        return dispVal;
    }

    boolean eqS(String val) {
       return CUtil.EqNS(this.dispVal, val);
    }
}
