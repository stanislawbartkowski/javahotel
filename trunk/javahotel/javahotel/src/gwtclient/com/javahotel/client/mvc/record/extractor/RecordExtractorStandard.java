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
package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.RoomStandardP;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RecordExtractorStandard extends AbstractRecordDictExtractor {

    RecordExtractorStandard(IResLocator rI) {
        super(rI,DictType.RoomStandard);
    }

    @Override
    protected Collection<? extends DictionaryP> getDic(IRecordView view, RecordModel a) {
        RoomStandardP rp = (RoomStandardP) a.getA();
        return rp.getServices();
    }
}
