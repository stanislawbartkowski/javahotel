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

package com.javahotel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.types.LId;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite12 extends TestHelper {
    
    
    private void runtest(DictType d) {
       DictionaryP a = getDict(d,HOTEL1);
       a.setName("vat1");
       ReturnPersist re = hot.persistDic(se, d , a);
       System.out.println(re.getId());
       LId id = re.getId();
       assertNotNull(id);
       CommandParam p = new CommandParam();
       p.setHotel(HOTEL1);
       p.setDict(d);
       p.setRecId(id);
       AbstractTo aa = list.getOne(se, RType.ListDict, p);
       a = (DictionaryP) aa;
//       assertEquals(id,a.getId());
       assertEquals("vat1",a.getName());
       p = new CommandParam();
       p.setHotel(HOTEL1);
       p.setDict(d);
       p.setRecName("vat1");
       aa = list.getOne(se, RType.ListDict, p);
       a = (DictionaryP) aa;
       assertEquals("vat1",a.getName());       
    }
    
    @Test
    public void Test1() {
       loginuser();
       runtest(DictType.VatDict);
       runtest(DictType.BookingList);
       runtest(DictType.CustomerList);
       runtest(DictType.RoomFacility);
    }

}
