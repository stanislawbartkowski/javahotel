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
package com.javahotel.db.patternreg;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.javahotel.common.gensymbol.GenSymbol;
import com.javahotel.common.gensymbol.GenSymbolData;
import com.javahotel.common.gensymbol.GenSymbolData.SymbolContainer;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.regparam.RegParam;
import java.util.Collection;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetNextSym {

    private GetNextSym() {
    }

    private final static String MAXPATT = "MAX-PATTERN-SYM-";

    private static String getS(final String kId, final boolean withy) {
        String s = MAXPATT + kId;
        if (withy) {
            s += "YY-";
        }
        return s;
    }

    private static GenSymbolData getSy(final ICommandContext iC,
            final String kId) {
        String s = getS(kId, false);
        int maxN = RegParam.getParam(iC, s, 0);
        String s1 = getS(kId, true);
        List<ParamRegistry> col = RegParam.getParams(iC, s1);

        List<SymbolContainer> out = new ArrayList<SymbolContainer>();
        for (ParamRegistry pa : col) {
            String y = pa.getName().substring(s1.length());
            int year = Integer.parseInt(y);
            int maxw = Integer.parseInt(pa.getDescription());
            SymbolContainer sy = new SymbolContainer(year, maxw);
            out.add(sy);
        }
        GenSymbolData ge = new GenSymbolData(out, maxN);
        return ge;
    }

    private static void putSy(final ICommandContext iC, final String kId,
            final GenSymbolData sy) {
        String s = getS(kId, false);
        int maxN = sy.maxNo();
        RegParam.setParam(iC, s, maxN);

        Collection<SymbolContainer> col = sy.getCol();
        for (SymbolContainer ss : col) {
            String ke = getS(kId, true) + ss.getYear();
            RegParam.setParam(iC, ke, ss.getMaxno());
        }
    }

    public static String NextSym(final ICommandContext iC, final String kId,
            final Date da, final String pattern) {
        GenSymbolData sy = getSy(iC, kId);
        String outS = GenSymbol.nextSymbol(pattern, da, sy);
        putSy(iC, kId, sy);
        return outS;
    }

    public static int NextIntValue(final ICommandContext iC, final String kId) {
        int no = RegParam.getParam(iC, kId, 0);
        no++;
        RegParam.setParam(iC, kId, no);
        return no;
    }

}
