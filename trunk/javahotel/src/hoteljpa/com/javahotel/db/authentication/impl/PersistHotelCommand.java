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
package com.javahotel.db.authentication.impl;

import com.javahotel.db.authentication.jpa.Hotel;
import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistHotelCommand extends CommandTra {

    private final String description;
    private final String database;

    PersistHotelCommand(final SessionT sessionId, final HotelT hotel,
            final String description, final String database)
            {
        super(sessionId, null, hotel, true, true, false, false);
        this.description = description;
        this.database = database;
    }

    @Override
    protected void command() {
        String e = IMessId.CHANGEHOTEL;
        if (ha == null) {
            ha = new Hotel();
            ha.setName(hotel.getName());
            e = IMessId.ADDHOTEL;
        }
        String logs = ELog.logEventS(e, IMessId.DESCHOTEL, iC.getSession().getName(),
                iC.getHP().getUser(), hotel.getName(), description);
        HLog.getLo().info(logs);
        ha.setDatabase(database);
        ha.setDescription(description);
        iC.getJpa().changeRecord(ha);
        HotelStore.invalidateCache();
    }
}
