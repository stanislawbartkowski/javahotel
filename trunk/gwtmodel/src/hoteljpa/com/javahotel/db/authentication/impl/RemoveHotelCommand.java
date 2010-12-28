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
package com.javahotel.db.authentication.impl;

import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.GetLMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RemoveHotelCommand extends CommandTra {

    RemoveHotelCommand(final SessionT sessionId, final HotelT hotel)
            {
        super(sessionId, null, hotel, true, true, false, false);
    }

    @Override
    protected void command() {
        String l = ELog.logEventS(IMessId.REMOVEHOTEL, IMessId.DESCHOTEL,
                iC.getSession().getName(), iC.getHP().getUser(), ha.getName(),
                ha.getDescription());
        if (ha == null) {
            String e = GetLMess.getM(GetLMess.CANNOTREMOVE);
            HLog.getL().getL().info(l + e);
            trasuccess = false;
            return;
        }
        HLog.getLo().info(l);
        iC.getJpa().removeObject(ha);
        HotelStore.invalidateCache();
    }
}
