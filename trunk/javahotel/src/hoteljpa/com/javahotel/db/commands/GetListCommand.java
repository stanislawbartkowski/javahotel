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

import java.util.List;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.db.hotelbase.jpa.VatDictionary;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetListCommand extends CommandAbstract {

    private final DictType d;
    private List<AbstractTo> col;
    private final CommandParam para;

    public GetListCommand(final SessionT se, final DictType d,
            final HotelT hotel, CommandParam para) {
        super(se, false, hotel);
        this.d = d;
        this.para = para;
    }

    @Override
    protected void command() {
        List<?> c = GetQueries.getDList(iC, ObjectFactory.getC(d), d, para);
        if ((d == DictType.VatDict) && (c.size() == 0)) {
            c = readDefault(VatDictionary.class, IMess.VATDICTTAGNAME,
                    IMess.VATDICTXMLFILE);
        }
        col = HotelHelper.toA(iC, c, d);
        String s = "null";
        if (col != null) {
            s = "" + col.size();
        }
        String msg = iC.logEvent(IMessId.GETLISTDICT, d.toString(), s);
        iC.getLog().getL().info(msg);
    }

    public List<AbstractTo> getCol() {
        return col;
    }
}