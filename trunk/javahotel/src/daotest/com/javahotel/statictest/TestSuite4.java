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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.PeriodT;
import com.javahotel.common.dateutil.GetPeriodsTemplate;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite4 {

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }
    
    private class myC extends GetPeriodsTemplate {
        
        private int no;
        
        myC(final Date startD,List<Date>dLine,List<PeriodT> coP) {
            super(startD,dLine,coP);
            no = 0;
        }

        @Override
        protected int startF(final Date dd) {
            return 10;
        }

        @Override
        protected void addElem(PeriodT pr, int wi) {
            TestDUtil.drawP(pr);
            switch (no) {
                case 0: TestDUtil.checkP(pr, 2008, 9, 7, 2008, 9, 7); break;
                case 1: TestDUtil.checkP(pr, 2008, 9, 8, 2008, 9, 8); break;
                default: fail();
            }
            no++;
        
        }

        @Override
        protected void endF() {
        }
        
    }

    @Test
    public void Test1() throws Exception {

        System.out.println("Test create table day more");
        Integer i = new Integer(1);
        PeriodT p = new PeriodT(createD(2008,9,7),createD(2008,9,7),i);
        PeriodT p1 = new PeriodT(createD(2008,9,8),createD(2008,9,8),i);
        List<PeriodT> coP = new ArrayList<PeriodT>();
        coP.add(p);
        coP.add(p1);
        List<Date> dLine = new ArrayList<Date>();
        Date d1 = createD(2008,9,7);
        Date d2 = createD(2008,9,8);
        dLine.add(d2);
        myC c = new myC(d1,dLine,coP);
        c.drawD(0, 10);
    }
}
