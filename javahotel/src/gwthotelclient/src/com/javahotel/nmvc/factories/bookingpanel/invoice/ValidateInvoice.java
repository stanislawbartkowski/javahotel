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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;

/**
 * @author hotel
 *
 */
class ValidateInvoice extends AbstractSlotContainer {
    
    ValidateInvoice(IDataType dType) {
        this.dType = dType;
        getSlContainer().registerSubscriber(dType,
                DataActionEnum.ValidateAction, new ValidateSignal());
    }
    
    private class ValidateSignal implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            publish(dType,DataActionEnum.PersistDataAction);            
        }
        
    }

    
    


}
