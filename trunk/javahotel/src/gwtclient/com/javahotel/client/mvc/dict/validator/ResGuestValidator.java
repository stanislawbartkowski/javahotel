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

package com.javahotel.client.mvc.dict.validator;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ResGuestValidator extends DictValidator {

    ResGuestValidator(final IResLocator rI, final DictData d) {
        super(rI, d, false);
    }

    @Override
    public boolean isEmpty(RecordModel a) {
        List<IField> emptyC = new ArrayList<IField>();
        emptyC.add(CustomerP.F.firstName);
        emptyC.add(CustomerP.F.lastName);
        List<InvalidateMess> errMess = ValidUtil.checkEmpty(a, emptyC);
        if (errMess == null) {
            return false;
        }
        return (emptyC.size() == errMess.size());
    }

}
