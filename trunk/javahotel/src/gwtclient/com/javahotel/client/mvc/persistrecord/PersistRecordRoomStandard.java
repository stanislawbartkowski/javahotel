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
package com.javahotel.client.mvc.persistrecord;

import com.javahotel.common.command.DictType;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import java.util.ArrayList;
import java.util.List;
import com.javahotel.common.toobject.RoomStandardP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordRoomStandard extends AbstractPersistRecordDict {

    PersistRecordRoomStandard(final IResLocator rI) {
        super(rI, DictType.RoomStandard, DictType.ServiceDict);
    }

    @Override
    protected List<? extends DictionaryP> createNew() {
        List<ServiceDictionaryP> dcol =
                new ArrayList<ServiceDictionaryP>();
        return dcol;
    }

    @Override
    protected void setRes(RecordModel mo, List<? extends DictionaryP> col) {
        RoomStandardP dp = (RoomStandardP) mo.getA();
        dp.setServices((List<ServiceDictionaryP>) col);
    }

}
