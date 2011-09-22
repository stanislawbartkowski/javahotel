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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;
import com.javahotel.client.M;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */
public class CheckinGuest {

    private final RunCompose rCompose;

    public CheckinGuest() {
        RunCompose.IRunComposeFactory iFactory = new RunCompose.IRunComposeFactory() {

            @Override
            public IFormTitleFactory getTitle() {
                return new TitleFactory();
            }

            @Override
            public ISlotable constructS(ICallContext iContext, IDataType dType,
                    BookingP p, BoxActionMenuOptions bOptions, SlotType slType) {
                ISlotable iSlo = new CheckGuestWidget(dType, p, bOptions,
                        slType);
                return iSlo;
            }
        };
        rCompose = new RunCompose(iFactory);

    }

    private class TitleFactory implements IFormTitleFactory {

        @Override
        public String getFormTitle(ICallContext iContext) {
            return M.L().CheckinGuests();
        }

    }

    public void CheckIn(final BookingP p, WSize wSize) {
        IDataType dType = Empty.getDataType();
        rCompose.runDialog(dType, p, wSize);
    }

}
