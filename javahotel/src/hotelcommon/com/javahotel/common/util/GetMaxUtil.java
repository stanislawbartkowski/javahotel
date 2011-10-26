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
package com.javahotel.common.util;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.types.INumerable;

/**
 * Some utilities related to get something 'max'
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetMaxUtil {

    private GetMaxUtil() {
    }

    public interface IGetLp {

        Integer getLp(Object o);
    }

    /**
     * Find maximum number in the list
     * 
     * @param col
     *            Input list of elements
     * @param i
     *            Method for getting the numbue from the element
     * @return Number or -i if the list is empty
     */
    public static <T> int getMax(final List<T> col, final IGetLp i) {
        int no = -1;
        for (T t : col) {
            int lp = i.getLp(t);
            if (lp > no) {
                no = lp;
            }
        }
        return no;
    }

    /**
     * Filter out from the list col only the element equal to the max element
     * 
     * @param col
     *            Input list
     * @param i
     *            Method for getting the number for the element
     * @return New list
     */
    public static <T> List<T> getListLp(final List<T> col, final IGetLp i) {
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

    @SuppressWarnings("unchecked")
    public static <T> T getLast(final List<? extends INumerable> col) {
        IGetLp in = createI();
        return (T) getOneLp(col, in);
    }

    public static AdvancePaymentP getLastValidationRecord(final BookingP p) {

        INumerable in = getLast(p.getAddpayments());
        return (AdvancePaymentP) in;
    }

    public static BookingStateP getLastStateRecord(final BookingP p) {

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
