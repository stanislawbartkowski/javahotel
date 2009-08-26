/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.auxabstract;

import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class NumAddPaymentP extends NumAbstractTo {

    public NumAddPaymentP(AddPaymentP p) {
        super(p,p.getT());
    }

    @Override
    public Class getT(IField f) {
        AddPaymentP pa = (AddPaymentP) getO();
        return pa.getT(f);

    }

    public Integer getLp() {
        AddPaymentP pa = (AddPaymentP) getO();
        return pa.getLp();
    }

    public void setLp(Integer lP) {
        AddPaymentP pa = (AddPaymentP) getO();
        pa.setLp(lP);
    }

}
