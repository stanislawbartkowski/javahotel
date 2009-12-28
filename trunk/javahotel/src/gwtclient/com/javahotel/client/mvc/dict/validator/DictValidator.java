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
package com.javahotel.client.mvc.dict.validator;

import java.util.List;

import com.javahotel.client.CommonUtil;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DictValidator extends AbstractValidator {

    private final DictData d;
    private final boolean omitEmpty;

    DictValidator(final IResLocator rI, final DictData d, boolean omitEmpty) {
        super(rI);
        this.d = d;
        this.omitEmpty = omitEmpty;
    }

    public void validateS(final int action, final RecordModel a,
            final ISignalValidate sig) {
        List<InvalidateMess> errMess = null;
        if (!omitEmpty) {
            errMess = ValidUtil
                    .validateEmpty(d, action, a);
            if (errMess != null) {
                ValidUtil.callSig(errMess, sig, iCo);
                return;
            }
        }
        AbstractTo to = a.getA();
        if (d.getD() != null) {
            DictionaryP dp = (DictionaryP) to;
            String hotel = rI.getR().getHotel();
            dp.setHotel(hotel);
            GWTGetService.getService().testDictPersist(
                    CommonUtil.getPType(action), d.getD(), dp,
                    new ValidUtil.ValidCallBack(rI, sig, iCo, to));
            return;
        }
        ValidUtil.callSig(errMess, sig, iCo);
    }

}
