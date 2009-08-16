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

import com.javahotel.common.toobject.PersonP;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.db.copy.FieldList;
import com.javahotel.dbjpa.copybean.CopyBean;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.log.HLog;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class GetPersonListCommand extends CommandTra {

    final Collection<PersonP> out;

    GetPersonListCommand(final SessionT sessionId) {
        super(sessionId, null, null, false, false, false, false);
        out = new ArrayList<PersonP>();

    }

    @Override
    protected void command() {
        Collection<Person> li = iC.getJpa().getAllListOrdered(Person.class,
                "name", true);
        for (Person ha : li) {
            PersonP hp = new PersonP();
            CopyBean.copyBean(ha, hp, HLog.getL(), FieldList.PersonList);
            out.add(hp);
        }
    }
}
