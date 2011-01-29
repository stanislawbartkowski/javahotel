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

package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.common.command.DictType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class RecordExtractorFactory {

    private RecordExtractorFactory() {

    }

    public static IRecordExtractor getExtractor(final IResLocator rI,
            final DictData da) {
        if ((da.getD() != null) && (da.getD() == DictType.RoomStandard)) {
            return new RecordExtractorStandard(rI);
        }
        if ((da.getD() != null) && (da.getD() == DictType.RoomObjects)) {
            return new RecordExtractorResObject(rI);
        }
        if ((da.getD() != null) && (da.getD() == DictType.OffSeasonDict)) {
            return new RecordExtractorSeasonOffer();
        }
        return new RecordExtractor(rI,da);
    }

}
