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
package com.javahotel.types;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class DecimalP implements Serializable {

    private String decim;

    public DecimalP() {
        decim = null;
    }

    public BigDecimal getDecim() {
        if (decim == null) {
            return null;
        }
        return new BigDecimal(decim);
    }

    public void setDecim(BigDecimal decim) {
        if (decim == null) {
            this.decim = null;
            return;
        }
        this.decim = decim.toString();
    }
}
