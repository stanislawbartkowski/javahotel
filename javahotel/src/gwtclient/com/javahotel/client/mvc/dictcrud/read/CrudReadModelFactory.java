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
package com.javahotel.client.mvc.dictcrud.read;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.dictdata.model.IAdvancePaymentModel;
import com.javahotel.common.command.DictType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CrudReadModelFactory {

    private final IResLocator rI;
    
    @Inject
    public CrudReadModelFactory(IResLocator rI) {
        this.rI = rI;
    }

    public ICrudReadModel getRead(DictData da,
            ICrudReadDictModel mo) {

        if (da.getRt() != null) {
            switch (da.getRt()) {
                case ListDict:
                    break;
                case DownPayments:
                    return new CrudReadAdvancePayModel(rI,
                            (IAdvancePaymentModel) mo);
                case AllHotels :
                case AllPersons :
                    break;
                default:
                    return null;
            }
        }

        if (da.getSE() != null) {
            switch (da.getSE()) {
                default:
                    return null;
                case ResTablePanel:
                    return new CrudReadModel(rI,
                            new DictData(DictType.RoomObjects));
            }
        }

        return new CrudReadModel(rI, da);

    }

    public ICrudReadModel getRead(DictData da) {
        return getRead(da, null);
    }
}
