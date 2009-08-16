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

import com.javahotel.db.authentication.jpa.GroupD;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.dbjpa.ejb3.JpaEntity;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class AutUtil {

    static Person getPerson(final JpaEntity jpa, final String name)
            {
        Person pe = jpa.getOneWhereRecord(Person.class, "name", name);
        if (pe == null) {
            return pe;
        }
// if fetched not necessary        
// (does not work on JBoss (hibernate)        
        if (pe.getGroup() == null) {
            pe.setGroup(new ArrayList<GroupD>());
        }
        return pe;
    }
}

