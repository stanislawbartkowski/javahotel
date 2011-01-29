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

import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GenSymbolData {

    public static class SymbolContainer {

        private int year;
        private int maxno;

        public SymbolContainer(final int y) {
            year = y;
            maxno = 0;
        }

        public SymbolContainer(final int y, final int m) {
            year = y;
            maxno = m;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMaxno() {
            return maxno;
        }

        public void setMaxno(int maxno) {
            this.maxno = maxno;
        }
    }
    private final Map<Integer, SymbolContainer> ma =
            new HashMap<Integer, SymbolContainer>();
    private int maxNumb;

    public GenSymbolData(final List<SymbolContainer> col,
            final int maxNo) {
        for (SymbolContainer s : col) {
            ma.put(s.year, s);
        }
        maxNumb = maxNo;
    }

    public Collection<SymbolContainer> getCol() {
        return ma.values();
    }

    public int maxNo() {
        return maxNumb;
    }

    public int getNextMaxNo() {
        maxNumb++;
        return maxNo();
    }

    public int getNextYear(int year) {
        SymbolContainer sy = ma.get(year);
        if (sy == null) {
            sy = new SymbolContainer(year);
        }
        sy.maxno++;
        ma.put(year, sy);
        return sy.getMaxno();
    }
}
