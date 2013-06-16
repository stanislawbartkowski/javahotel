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
package com.jython.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Test22 extends TestHelper {
    
    private static final String REALM="TESTREALM";
    private static final String REALM1="TESTREALM1";
    private static final String KEY1="KEY1";
    private static final String KEY2="KEY2";
    
    @Before
    public void clear() {
        iSeq.remove(REALM, KEY1);
        iSeq.remove(REALM, KEY2);
        iSeq.remove(REALM1, KEY1);
        iSeq.remove(REALM1, KEY2);
    }
    
    @Test
    public void test1() {
        Long no = iSeq.genNext(REALM, KEY1);
        System.out.println(no);
        assertEquals(1,no.longValue());
        assertEquals(2,iSeq.genNext(REALM, KEY1).longValue());
        assertEquals(1,iSeq.genNext(REALM, KEY2).longValue());
        for (int i=1; i<100; i++) {
            assertEquals(i,iSeq.genNext(REALM1, KEY1).longValue());            
        }
        assertEquals(3,iSeq.genNext(REALM, KEY1).longValue());
        
    }

}
