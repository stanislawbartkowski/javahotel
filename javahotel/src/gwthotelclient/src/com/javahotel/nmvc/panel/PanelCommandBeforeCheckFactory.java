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
package com.javahotel.nmvc.panel;

import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PanelCommandBeforeCheckFactory {

    private PanelCommandBeforeCheckFactory() {
    }

    static IPanelCommandBeforeCheck getPanelCheck(EPanelCommand command) {
        IPanelCommandBeforeCheck i = null;
        IResLocator rI = HInjector.getI().getI();
        // clear all caches to force reading from database directly
        // refresh
        rI.getR().invalidateCacheList();

        switch (command) {
        case REMOVEDATA:
            i = new VerifyNumberOfDict(RType.AllHotels,
                    "cannotdisplayhotels.jsp");
            break;
        case BOOKINGPANEL:
        case ADVANCEPAYMENT:
            // invalidate/refresh cache at the beginning
            // force reading data from database
            rI.getR().invalidateResCache();
            i = new VerifyNumberOfDict(new DictType[] { DictType.RoomObjects,
                    DictType.OffSeasonDict, DictType.PriceListDict },
                    "cannotdisplaypanel.jsp");
            break;
        case ROOMS:
            i = new VerifyNumberOfDict(new DictType[] { DictType.RoomFacility,
                    DictType.ServiceDict, DictType.RoomStandard },
                    "cannotdisplayrooms.jsp");
            break;
        case STANDARD:
            i = new VerifyNumberOfDict(new DictType[] { DictType.ServiceDict },
                    "cannotdisplaystandard.jsp");
            break;
        case PRICES:
            i = new VerifyNumberOfDict(new DictType[] { DictType.ServiceDict,
                    DictType.OffSeasonDict }, "cannotdisplayprice.jsp");
            break;
        case BOOKING:
            i = new VerifyNumberOfDict(new DictType[] { DictType.ServiceDict,
                    DictType.OffSeasonDict, DictType.RoomObjects,
                    DictType.PriceListDict }, "cannotdisplaybooking.jsp");
            break;

        }
        return i;
    }
}
