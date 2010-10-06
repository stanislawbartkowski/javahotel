/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.util;

import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.types.INumerable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetMaxUtil {

    private GetMaxUtil() {
    }

    public interface IGetLp {

        Integer getLp(Object o);
    }

    public static int getMax(final List col, final IGetLp i) {
        int no = -1;
        for (Object o : col) {
            int lp = i.getLp(o);
            if (lp > no) {
                no = lp;
            }
        }
        return no;
    }

    public static <T> List<T> getListLp(final List<T> col,
            final IGetLp i) {
        int no = getMax(col, i);
        List<T> out = new ArrayList<T>();
        for (T t : col) {
            int l = i.getLp(t);
            if (l == no) {
                out.add(t);
            }
        }
        return out;
    }

    public static <T> T getOneLp(final List<T> col, final IGetLp i) {
        int no = getMax(col, i);
        T obj = null;
        for (T t : col) {
            int l = i.getLp(t);
            if (l == no) {
                assert obj == null : "Should be only one max";
                obj = t;
            }
        }
        return obj;
    }

    private static IGetLp createI() {
        IGetLp in = new IGetLp() {

            public Integer getLp(final Object o) {
                INumerable ii = (INumerable) o;
                return ii.getLp();
            }
        };
        return in;
    }

    public static int getMax(final List<? extends INumerable> col) {
        IGetLp in = createI();
        return getMax(col, in);
    }

    public static Integer getMaxL(final List<? extends INumerable> col) {
        int m = getMax(col);
        if (m == -1) {
            return null;
        }
        return new Integer(m);
    }

    public static <T> T getLast(
            final List<? extends INumerable> col) {
        IGetLp in = createI();
        return (T) getOneLp(col, in);
    }

    public static BookRecordP getLastBookRecord(final BookingP p) {

        IGetLp i = new IGetLp() {

            public Integer getLp(final Object o) {
                BookRecordP b = (BookRecordP) o;
                return b.getLp();
            }
        };
        INumerable in = getLast(p.getBookrecords());
        return (BookRecordP) in;
    }

    public static AdvancePaymentP getLastValidationRecord(final BookingP p) {

        IGetLp i = new IGetLp() {

            public Integer getLp(final Object o) {
                AdvancePaymentP b = (AdvancePaymentP) o;
                return b.getLp();
            }
        };
        BillP bi = BillUtil.getBill(p);
        if (bi == null) {
            return null;
        }
        INumerable in = getLast(bi.getAdvancePay());
        return (AdvancePaymentP) in;
    }

    public static BookingStateP getLastStateRecord(final BookingP p) {

        IGetLp i = new IGetLp() {

            public Integer getLp(final Object o) {
                BookingStateP b = (BookingStateP) o;
                return b.getLp();
            }
        };
        INumerable in = getLast(p.getState());
        return (BookingStateP) in;
    }

    public static void setFirstLp(INumerable i) {
        i.setLp(new Integer(1));
    }

    public static void setNextLp(final List<? extends INumerable> col,
            final INumerable dest) {
        int no = GetMaxUtil.getMax(col);
        Integer lp;
        if (no == -1) {
            lp = new Integer(1);
        } else {
            lp = new Integer(no + 1);
        }
        dest.setLp(lp);
    }

    public static <T extends INumerable> void addNextLp(final List<T> col,
            final T dest) {
        setNextLp(col, dest);
        col.add(dest);
    }
}
