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

import java.util.List;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.client.mvc.validator.IErrorMessageContext;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;

public class PriceListValidatorService implements IRecordValidator {

    private final IResLocator rI;
    private final DictEmptyFactory eEmpty;
    private PriceListErrorContext pC;

    @Inject
    PriceListValidatorService(IResLocator rI, DictEmptyFactory eEmpty) {
        this.rI = rI;
        this.eEmpty = eEmpty;
    }

    public boolean isEmpty(RecordModel a) {
        return false;
    }

    public void setErrContext(IErrorMessageContext iCo) {
        pC = (PriceListErrorContext) iCo;
    }

    public void validateS(int action, RecordModel a, ISignalValidate sig) {
        OfferPriceP oP = (OfferPriceP) a.getA();
        List<OfferServicePriceP> oList = oP.getServiceprice();
        List<IField> eList = eEmpty.getEmptyPriceList();
        for (OfferServicePriceP p : oList) {
//            List<InvalidateMess> iList = ValidUtil.checkEmpty(p, eList);
            List<InvalidateMess> iList = null;
            if (iList == null) {
                continue;
            }
            pC.addErr(p.getService(), iList);
        }
    }

}
