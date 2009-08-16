/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javahotel.statictest;

import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriodsTemplate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

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
        
        myC(final Date startD,Collection<Date>dLine,Collection<PeriodT> coP) {
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
        Collection<PeriodT> coP = new ArrayList<PeriodT>();
        coP.add(p);
        coP.add(p1);
        Collection<Date> dLine = new ArrayList<Date>();
        Date d1 = createD(2008,9,7);
        Date d2 = createD(2008,9,8);
        dLine.add(d2);
        myC c = new myC(d1,dLine,coP);
        c.drawD(0, 10);
    }
}
