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
package com.javahotel.common.gensymbol;

import com.javahotel.common.dateutil.DateFormatUtil;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GenSymbol {

    /*
     * Package to generate symbol.
     * Pattern: YY - year, MM - month, NN new number 
     * N{digit} - with leading zero
     * DD new number starting with year
     * D{digit} - with leading zero
     * Examples:
     * NN/MM/YY
     *    1/01/2008  2/01/2008 ... 34/04/2008
     * N4/MM/YY
     *    0001/01/2008 0002/01/2998 ... 0034/04/2008
     * YY/DD/MM
     *    2008/567/12, 2008/568/1,2009/1/1, 2009/2/1 ... 2009/456/10
     * YY/D5/MM
     *    2008/00567/12, 2008/00568/1,2009/001/1, 2009/00002/1 ... 2009/01034/10
     */
    private GenSymbol() {
    }
    private static final String RR = "YY";
    private static final String REGMM = "\\\\" + 'd' + "\\\\" + 'd';
    private static final String REGRR = REGMM + REGMM;
    private static final String REGDIG = "\\\\" + 'd';
    private static final String REGNU = "\\\\d?";
    private static final String MM = "MM";
    private static final String DD = "DD";
    private static final String NN = "NN";
    private static final char D = 'D';
    private static final char N = 'N';

    private static class AnalizePattern {

        AnalizePattern() {
            dNumber = -1;
            isNN = false;
            isDD = false;
        }
        // Numer of digits or -1 if no digits
        int dNumber;
        boolean isNN;
        boolean isDD;
        String regExpr;
    }

    private static String regC(final char c) {
        return c + "\\d";
    }

    private static int mMatch(final String r, final char c) {
        final String R = "qqqqqq";
        String reg = regC(c);
        String rr = r.replaceAll(reg, R);
        int ii = rr.indexOf(R);
        if (ii == -1) {
            return -1;
        }
        char d = r.charAt(ii + 1);
//        int no = Character.getNumericValue(d) - Character.getNumericValue('0');
        String s = "" + d;
        return Integer.parseInt(s);
    }

    private static AnalizePattern parsePattern(final String pattern) {
        AnalizePattern a = new AnalizePattern();
        final int LE = 100;

        String r = pattern.replaceAll(RR, REGRR);
        r = r.replaceAll(MM, REGMM);
        r = r.replaceAll(DD, REGNU);
        r = r.replaceAll(NN, REGNU);
        a.isDD = (pattern.indexOf(DD) != -1);
        a.isNN = (pattern.indexOf(NN) != -1);
        int nD = mMatch(r, D);
        int nN = mMatch(r, N);
        if (nD != -1) {
            a.dNumber = nD;
            a.isDD = true;
        }
        if (nN != -1) {
            a.dNumber = nN;
            a.isNN = true;
        }
        if (a.dNumber != -1) {
            String re = "";
            for (int i = 0; i < a.dNumber; i++) {
                re += REGDIG;
            }
            String rC;
            if (a.isDD) {
                rC = regC(D);
            } else {
                rC = regC(N);
            }
            r = r.replaceAll(rC, re);
        }
        a.regExpr = r;
        return a;
    }

    public static boolean sMatch(final String pattern, final String s) {
        AnalizePattern a = parsePattern(pattern);
        return s.matches(a.regExpr);
    }
    
    public static String nextSymbol(final String pattern, final Date da,
            final GenSymbolData col) {
        int year = da.getYear() + 1900;
        int month = da.getMonth();
        String yearS = DateFormatUtil.toNS(year,4);
        String monthS = DateFormatUtil.toNS(month+1, 2);
        AnalizePattern a = parsePattern(pattern);
        int no =  -1;
        String n = null;
        if (a.isNN) {
            no = col.getNextMaxNo();
        }
        else {
            no = col.getNextYear(year);
        }
        if (a.dNumber != -1) {
            n = DateFormatUtil.toNS(no, a.dNumber);
        }
        else {
            n = Integer.toString(no);
        }
        String aS = pattern.replaceAll(RR,yearS);
        aS = aS.replaceAll(MM, monthS);
        aS = aS.replaceAll(DD,n);
        aS = aS.replaceAll(NN,n);
        aS = aS.replaceAll(regC(D),n);
        aS = aS.replaceAll(regC(N),n);        
        return aS;
    }
    
    
}
