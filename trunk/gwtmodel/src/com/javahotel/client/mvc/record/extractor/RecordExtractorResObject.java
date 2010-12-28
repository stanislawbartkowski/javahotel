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

package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IRecordView;
import java.util.List;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RecordExtractorResObject extends AbstractRecordDictExtractor {

    RecordExtractorResObject(IResLocator rI) {
        super(rI,DictType.RoomObjects);
    }

    @Override
    protected List<? extends DictionaryP> getDic(IRecordView view, RecordModel a) {
        ResObjectP rp = (ResObjectP) a.getA();
        return rp.getFacilities();
    }

}
