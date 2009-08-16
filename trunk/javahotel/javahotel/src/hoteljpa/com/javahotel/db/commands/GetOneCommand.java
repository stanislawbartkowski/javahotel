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
package com.javahotel.db.commands;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.util.AbstractObjectFactory;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetOneCommand extends CommandAbstract {

    private final DictType d;
    private AbstractTo res;
    private final CommandParam pa;

    public GetOneCommand(final SessionT se, final DictType d,
            final HotelT hotel, final CommandParam pa) {
        super(se, false, hotel);
        this.d = d;
        this.pa = pa;
    }

    @Override
    protected void command() {
        LId id = pa.getRecId();
        String na = pa.getRecName();
//        String hotel = pa.getHotel();
        Class<?> cla = ObjectFactory.getC(d);
        Object o;

        if (d == DictType.PriceListDict) {
            String season = pa.getSeasonName();
            o = CommonHelper.getOneOffer(iC, season, na);
        } else {

            if (na != null) {
                o = CommonHelper.getA(iC, cla, na);
            } else {
                o = iC.getJpa().getRecord(cla, id);
            }
        }
        if (o == null) {
            res = null;
            return;
        }
        res = AbstractObjectFactory.getAbstract(d);
        CommonCopyBean.copyB(iC, o, res);
    }

    public AbstractTo getRes() {
        return res;
    }
}
