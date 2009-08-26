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
package com.javahotel.db.authentication.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.javahotel.common.toobject.HotelP;
import com.javahotel.db.authentication.jpa.Hotel;
import com.javahotel.db.copy.FieldList;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbres.log.HLog;
import com.javahotel.remoteinterfaces.SessionT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class GetHotelListCommand extends CommandTra {

    final Collection<HotelP> res;

    GetHotelListCommand(final SessionT sessionId) {
        super(sessionId, null, null, false, false, false, false);
        res = new ArrayList<HotelP>();
    }

    @Override
    protected void command() {
        Collection<Hotel> li =
                iC.getJpa().getAllListOrdered(Hotel.class, "name", true);
        for (Hotel ha : li) {
            HotelP hp = new HotelP();
            CopyBean.copyBean(ha, hp, HLog.getL(), FieldList.HotelList);
            res.add(hp);
        }

    }
}
