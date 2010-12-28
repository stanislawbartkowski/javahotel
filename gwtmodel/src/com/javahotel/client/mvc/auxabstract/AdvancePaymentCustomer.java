/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AdvancePaymentCustomer extends DownPaymentP {

    private CustomerP cust;

    public AdvancePaymentCustomer() {
        super();
        cust = null;
    }

    @Override
    public Object getF(IField f) {
        if (isField(f)) {
            return super.getF(f);
        }
        if (getCust() != null) {
            return getCust().getF(f);
        }
        return null;
    }

    /**
     * @param cust the cust to set
     */
    public void setCust(CustomerP cust) {
        this.cust = cust;
    }

    /**
     * @return the cust
     */
    public CustomerP getCust() {
        return cust;
    }
}

