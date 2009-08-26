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
package com.javahotel.client.mvc.recordviewdef;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ServiceType;
import java.util.Map;
import com.javahotel.client.IResLocator;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetIToSFactory {

    private GetIToSFactory() {
    }

    private static class CFieldToS implements AbstractTo.IFieldToS {

        private final IResLocator rI;

        CFieldToS(IResLocator rI) {
            this.rI = rI;
        }

        public String toS(final IField f, final Object va) {
            if (va instanceof ServiceType) {
                ServiceType se = (ServiceType) va;
                Map<String, String> ma = rI.getLabels().Services();
                String v = ma.get(se.toString());
                return v;
            }
            return null;
        }
    }

    public static AbstractTo.IFieldToS getI(IResLocator rI) {
        return new CFieldToS(rI);
    }
}
