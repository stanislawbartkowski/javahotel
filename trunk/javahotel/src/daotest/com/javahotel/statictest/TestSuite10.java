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
package com.javahotel.statictest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.javahotel.dbres.entityconstr.EntityConstr;
import com.javahotel.dbres.entityconstr.EntityConstrFactory;
import com.javahotel.dbres.entityconstr.IEntityConstr;
import com.javahotel.dbres.log.HLog;

public class TestSuite10 {

    @Test
    public void Test1() throws Exception {
        IEntityConstr co = EntityConstrFactory.createEntityConstr(HLog.getL());
        assertNotNull(co);
        EntityConstr e1 = co.getEntityC("rybka");
        assertNull(e1);
        e1 = co.getEntityC("Hotel");
        assertNotNull(e1);
        assertEquals("name", e1.getSymname());
        assertEquals("name", e1.getViewname());
        e1 = co.getEntityC("Person");
        assertNotNull(e1);
        assertEquals("DuplicateSym", e1.getKomname());
        assertEquals("login", e1.getViewname());

    }
}
