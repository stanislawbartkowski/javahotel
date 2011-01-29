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

package com.javahotel.client.mvc.record.model;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.MvcWindowSize;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RecordDef implements IRecordDef {
    
    private final List<RecordField> rList;
    private final String dTitle;
    private final IResLocator rI;
    private final MvcWindowSize mSize;
    
    RecordDef(final IResLocator rI, final String dTitle, 
            final List<RecordField> rList) {
        this.rList = rList;
        this.dTitle = dTitle;
        this.rI = rI;
        mSize = null;
    }

    RecordDef(final IResLocator rI, final String dTitle,
            final List<RecordField> rList,MvcWindowSize m) {
        this.rList = rList;
        this.dTitle = dTitle;
        this.rI = rI;
        mSize = m;
    }

    @Override
    public List<RecordField> getFields() {
        return rList;
    }

    @Override
    public String getDTitle() {
        return dTitle;
    }

    /**
     * @return the mSize
     */
    @Override
    public MvcWindowSize getMSize() {
        return mSize;
    }

}
