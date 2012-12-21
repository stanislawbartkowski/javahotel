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

import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.rescache.ResObjectCache;
import com.javahotel.common.rescache.ResObjectCache.IReadResCallBack;
import com.javahotel.common.rescache.ResObjectCache.IReadResData;
import com.javahotel.common.rescache.ResObjectCache.ISetResState;
import com.javahotel.common.rescache.ResObjectElem;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite9 {

    @Test
    public void Test1() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 10);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        IReadResData ir = new IReadResData() {

            public int no = 0;

            public void getResData(ReadResParam pa, IReadResCallBack col) {
                assertEquals(0, no);
                System.out.println(pa.getResList());
                TestDUtil.drawP(pa.getPe());
                String sx = null;
                for (final String s : pa.getResList()) {
                    sx = s;
                }
                assertEquals("1p", sx);
                assertEquals("2008/10/01", DateFormatUtil.toS(pa.getPe().getFrom()));
                assertEquals("2008/10/10", DateFormatUtil.toS(pa.getPe().getTo()));
                List<ResDayObjectStateP> out = new ArrayList<ResDayObjectStateP>();
                ResDayObjectStateP re = new ResDayObjectStateP();
                re.setD(TestDUtil.createD(2008, 10, 1));
                re.setResObject("1p");
                BookingStateP bo = new BookingStateP();
                out.add(re);
                re.setLState(bo);
                col.setCol(out);
                no++;
            }
        };

        ResObjectCache ca = new ResObjectCache(ir);

        ISetResState iget = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNotNull(p.getLState());
            }
        };

        ISetResState igetn = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNull(p.getLState());
            }
        };

        ca.getResState("1p", d1, r, iget);
        ca.getResState("1p", d2, r, igetn);
        
        PeriodT per = new PeriodT(TestDUtil.createD(2008, 10, 8),TestDUtil.createD(2008, 10, 9));
        ResObjectElem el = new ResObjectElem("1p",per);
        List<ResDayObjectStateP> res = ca.isConflict(el);
        assertEquals(0,res.size());
        
        per = new PeriodT(TestDUtil.createD(2008, 10, 1),TestDUtil.createD(2008, 10, 9));
        el = new ResObjectElem("1p",per);
        res = ca.isConflict(el);
        assertEquals(1,res.size());

    }
}