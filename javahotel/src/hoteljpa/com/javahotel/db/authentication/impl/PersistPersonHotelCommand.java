/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.javahotel.db.authentication.jpa.GroupD;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.ELog;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.security.login.PersonHotelRules;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistPersonHotelCommand extends CommandTra {

    private final List<String> principals;

    PersistPersonHotelCommand(final SessionT sessionId, final String person,
            final HotelT hotel, List<String> principals)
            {
        super(sessionId, person, hotel, true, true, true, true);
        this.principals = principals;
    }

    @Override
    protected void command() {
        List<GroupD> col = new ArrayList<GroupD>();
        for (GroupD d : pe.getGroup()) {
            if (PersonHotelRules.onHotel(d.getGroupname(), hotel) != null) {
                col.add(d);
            }
        }

        for (GroupD d : col) {
            pe.getGroup().remove(d);
            iC.getJpa().removeObject(d); // TODO: remove
        }

        String logRole = null;
        // add new permissions
        if (principals != null) {
            for (String s : principals) {
                if (logRole == null) {
                    logRole = s;
                } else {
                    logRole += " , " + s;
                }
                String ro = PersonHotelRules.createRol(hotel, s);
                GroupD d = new GroupD();
                d.setNameid(pe);
                d.setName(pe.getName());
                d.setGroupname(ro);
                pe.getGroup().add(d);
            }
        }

        String logs;
        if (logRole == null) {
            logs = ELog.logEventS(IMessId.REMOVEGRANT, IMessId.REMOVEGRANT,
                    iC.getSession().getName(), iC.getHP().getUser(), person,
                    hotel.getName());
        } else {
            logs = ELog.logEventS(IMessId.SETGRANT, IMessId.SETGRANT,
                    iC.getSession().getName(), iC.getHP().getUser(), person,
                    hotel.getName(), logRole);
        }
        HLog.getLo().info(logs);
        iC.getJpa().changeRecord(pe);
    }
}
