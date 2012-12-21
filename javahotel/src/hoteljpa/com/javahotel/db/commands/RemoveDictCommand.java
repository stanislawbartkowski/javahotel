/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RemoveDictCommand extends CommandAbstract {

    private final DictType d;
    private final DictionaryP a;

    public RemoveDictCommand(final SessionT se, final DictType d,
            final DictionaryP a) {
//        super(se, true, new HotelT(a.getHotel()), false);
        super(se, true, new HotelT(a.getHotel()));
        this.d = d;
        this.a = a;
    }

    private void logS(GetObjectRes o) {
        IPureDictionary oo = o.getO();
        String logs = iC.getRecordDescr(d, oo);
        String logm = iC.logEvent(IMessId.DELETEDICTRECORD, logs);
        iC.getLog().getL().info(logm);

    }

    @Override
    protected void command() {
        GetObjectRes o = getObject(d, a);
        startTra();
        o.refresh();
        logS(o);
        iC.getJpa().removeObject(o.getO());
    }
}
