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

package com.javahotel.db.commands;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DictNumberRecord extends CommandAbstract {

    private final DictType dType;
    private int res;

    public DictNumberRecord(final SessionT se, final DictType dType,
            final HotelT hotel) {
        super(se, false, hotel);
        this.dType = dType;
    }

    @Override
    protected void command() {

        Class<?> cla = ObjectFactory.getC(dType);
        res = GetQueries.getDictNumber(iC, cla);
    }

    public ReturnPersist getRet() {
        ReturnPersist ret = new ReturnPersist();
        ret.setNumberOf(res);
        return ret;
    }
}
