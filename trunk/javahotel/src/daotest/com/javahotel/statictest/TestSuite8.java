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

package com.javahotel.statictest;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.gensymbol.GenSymbol;
import com.javahotel.common.gensymbol.GenSymbolData;
import com.javahotel.common.gensymbol.GenSymbolData.SymbolContainer;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite8 {
    
    @Test
    public void Test1() {
        String R = "NN/MM/YY";
        boolean b = GenSymbol.sMatch(R, "aaaa");
        assertFalse(b);
        b = GenSymbol.sMatch(R,"1/02/2008");
        assertTrue(b);
        b = GenSymbol.sMatch(R,"1/02/20008");
        assertFalse(b);
        b = GenSymbol.sMatch(R,"1/2/2008");
        assertFalse(b);
 
    }
    
    @Test
    public void Test2() {
        String R = "N4/MM/YY";
        boolean b = GenSymbol.sMatch(R, "aaaa");
        assertFalse(b);
        b = GenSymbol.sMatch(R,"0001/02/2008");
        assertTrue(b);
        b = GenSymbol.sMatch(R,"0001/02/20008");
        assertFalse(b);
        b = GenSymbol.sMatch(R,"0001/2/2008");
        assertFalse(b);
        b = GenSymbol.sMatch(R,"00001/02/2008");
        assertFalse(b); 
    }
    
    @Test
    public void Test3() {
        String R = "N4/MM/YY";
        GenSymbolData sy = new GenSymbolData(new ArrayList<SymbolContainer>(),0);
        Date da = DateFormatUtil.toD("2008/10/01");
        Date da1 = DateFormatUtil.toD("2007/10/01");
        String s = GenSymbol.nextSymbol(R, da, sy);
        System.out.println(s);
        assertEquals("0001/10/2008",s);
        s = GenSymbol.nextSymbol("NN/MM/YY", da, sy);
        System.out.println(s);
        assertEquals("2/10/2008",s);
        s = GenSymbol.nextSymbol("DD/MM/YY", da, sy);
        System.out.println(s);
        assertEquals("1/10/2008",s);
        s = GenSymbol.nextSymbol("DD/MM/YY", da1, sy);
        System.out.println(s);
        assertEquals("1/10/2007",s);
        s = GenSymbol.nextSymbol("DD/MM/YY", da, sy);
        System.out.println(s);
        assertEquals("2/10/2008",s);
        s = GenSymbol.nextSymbol("D5/MM/YY", da, sy);
        System.out.println(s);
        assertEquals("00003/10/2008",s);      
    }


}
