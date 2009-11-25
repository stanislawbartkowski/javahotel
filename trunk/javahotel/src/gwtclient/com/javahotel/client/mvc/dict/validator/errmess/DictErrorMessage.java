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

package com.javahotel.client.mvc.dict.validator.errmess;

import java.util.List;

import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IErrorMessageContext;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictErrorMessage implements IErrorMessage {

    private final List<InvalidateMess> errmess;
    private final IErrorMessageContext iCo;

    public DictErrorMessage(List<InvalidateMess> errmess,
            IErrorMessageContext iCo) {
        this.errmess = errmess;
        this.iCo = iCo;
    }

    public void addMess(InvalidateMess m) {
        getErrmess().add(m);
    }

    /**
     * @return the errmess
     */
    public List<InvalidateMess> getErrmess() {
        return errmess;
    }

    public IErrorMessageContext getC() {
        return iCo;
    }

}
