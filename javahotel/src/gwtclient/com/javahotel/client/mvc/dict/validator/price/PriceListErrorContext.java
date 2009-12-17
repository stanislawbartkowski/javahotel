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
package com.javahotel.client.mvc.dict.validator.price;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.validator.IErrorMessageContext;

public class PriceListErrorContext implements IErrorMessageContext {
    
    private final List<SPriceInvalidateMess> mList;
    
    private class SPriceInvalidateMess {
        private final String service;
        private final List<InvalidateMess> mess;
        
        SPriceInvalidateMess(String service,List<InvalidateMess> mess) {
            this.service = service;
            this.mess = mess;
        }
    }
    
    private final List<SPriceInvalidateMess> eMess = new ArrayList<SPriceInvalidateMess>();
    
    public PriceListErrorContext() {
        mList = new ArrayList<SPriceInvalidateMess>();
        
    }
    
    void addErr(String service,List<InvalidateMess> m) {
        mList.add(new SPriceInvalidateMess(service,m));
    }
}


