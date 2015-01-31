/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.disclosure;

import javax.inject.Inject;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDisclosurePanelFactory;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.smessage.IGetStandardMessage;

public class DisclosurePanelFactory implements IDisclosurePanelFactory {

    private final IGetStandardMessage iMess;

    @Inject
    public DisclosurePanelFactory(IGetStandardMessage iMess) {
        this.iMess = iMess;
    }

    @Override
    public ISlotable construct(IDataType publishType, IDataType dType,
            String header, String html) {
        return new HTMLDisc(publishType, dType, header, html, iMess);
    }

}
