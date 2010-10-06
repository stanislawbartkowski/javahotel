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
package com.gwtmodel.table.common;

import java.util.List;

public class GetMaxUtil {

    private GetMaxUtil() {

    }

    public interface IGetLp<T> {
        Long getLp(T t);
    }

    public static <T> Long getMax(List<T> li, IGetLp<T> i, Long defa) {
        Long l = null;
        if (li.isEmpty()) {
            return defa;
        }
        for (T t : li) {
            Long ll = i.getLp(t);
            if (l == null) {
                l = ll;
            } else {
                if (l.longValue() < ll.longValue()) {
                    l = ll;
                }
            }
        }
        return l;
    }

    public static <T> Long getNextMax(List<T> li, IGetLp<T> i, Long first) {
        Long ma = getMax(li, i, null);
        if (ma == null) {
            return first;
        }
        return new Long(ma.longValue() + 1);
    }

}
