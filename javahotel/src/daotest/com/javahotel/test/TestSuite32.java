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
package com.javahotel.test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.DictionaryP;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite32 extends TestHelper {

    /**
     * Test CommandParam and NumberOfDictRecords command
     * Step 1: Add one customer
     * Step 2: Run CommandParam and NumberOfDictRecords
     * Expected result: exactly one
     * Step 3: Run Command Param i test number of record RoomObject
     * Expected result: 0
     */
    @Test
    public void Test1() {
        loginuser();
        logInfo("Test number of record");
        // Step 1
        DictionaryP a = getDict(DictType.CustomerList, HOTEL1);
        a.setName("C001");
        hot.persistDic(seu, DictType.CustomerList, a);
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setDict(DictType.CustomerList);
        pa.setoP(HotelOpType.NumberOfDictRecords);
        // Step 2
        ReturnPersist ret = hotop.hotelOp(se, pa);
        // Expected result
        assertEquals(1,ret.getNumberOf());
        // Step 3
        pa.setDict(DictType.RoomObjects);
        pa.setoP(HotelOpType.NumberOfDictRecords);
        ret = hotop.hotelOp(se, pa);
        // Expected result
        assertEquals(0,ret.getNumberOf());
    }

    /**
     * (Test1)
     * Step1: Number of CustomerList in HOTEL2
     * Expected result:
     * Equal 0:
     */
    @Test
    public void Test2() {
        Test1();
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL2);
        pa.setDict(DictType.CustomerList);
        pa.setoP(HotelOpType.NumberOfDictRecords);
        // Step 1
        ReturnPersist ret = hotop.hotelOp(se, pa);
        // Expected result
        assertEquals(0,ret.getNumberOf());
    }

    /**
     * (Test1)
     * Step1: Add two customers to HOTEL2
     * Step2: Number of customers in HOTEL2
     * Expected result: equals 2
     * Step3: Number of customers in HOTEL1
     * Expected result: equals 1
     */

    @Test
    public void Test3() {
        Test1();

        // Step 1
        DictionaryP a = getDict(DictType.CustomerList, HOTEL2);
        a.setName("C001");
        hot.persistDic(seu, DictType.CustomerList, a);
        a = getDict(DictType.CustomerList, HOTEL2);
        a.setName("C002");
        hot.persistDic(seu, DictType.CustomerList, a);

        // Step 2
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL2);
        pa.setDict(DictType.CustomerList);
        pa.setoP(HotelOpType.NumberOfDictRecords);
        ReturnPersist ret = hotop.hotelOp(se, pa);

        // Expected result
        assertEquals(2,ret.getNumberOf());

        // Step3
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setDict(DictType.CustomerList);
        pa.setoP(HotelOpType.NumberOfDictRecords);

        ret = hotop.hotelOp(se, pa);
        // Expected result
        assertEquals(1,ret.getNumberOf());
    }

    /**
     * Test for list of CommandParams
     * (Test3)
     * Step1: Add one RoomFacility to HOTEL2
     * Step2: Prepare two element list
     * Step3: Run : List of CommandParam
     * Expected result:
     * (1) List of 2 elements
     * (2) First size equals to 2
     * (3) Second size equals to 1
     */

    @Test
    public void Test4() {
        Test3();

        // Step1
        DictionaryP a = getDict(DictType.RoomFacility, HOTEL2);
        a.setName("C001");
        hot.persistDic(seu, DictType.RoomFacility, a);

        // Step2
        List li = new ArrayList<CommandParam>();
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL2);
        pa.setDict(DictType.CustomerList);
        pa.setoP(HotelOpType.NumberOfDictRecords);
        li.add(pa);
        pa = new CommandParam();
        pa.setHotel(HOTEL2);
        pa.setDict(DictType.RoomFacility);
        li.add(pa);

        // Step3
        pa.setoP(HotelOpType.NumberOfDictRecords);

        // Expected result
        List<ReturnPersist> res = hotop.hotelOp(se, li);

        // (1)
        assertEquals(2,res.size());

        // (2)
        ReturnPersist re = res.get(0);
        assertEquals(2,re.getNumberOf());

        // (3)
        re = res.get(1);
        assertEquals(1,re.getNumberOf());
    }

}
