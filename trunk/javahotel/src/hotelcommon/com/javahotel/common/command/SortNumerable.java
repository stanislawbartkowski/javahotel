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
package com.javahotel.common.command;

import com.javahotel.types.INumerable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class SortNumerable {

    private SortNumerable() {
    }

    private static class sortC implements Comparator<Object> {

        public int compare(final Object o1, final Object o2) {
            INumerable i1 = (INumerable) o1;
            INumerable i2 = (INumerable) o2;
            return i1.getLp().compareTo(i2.getLp());
        }
    }

    public static <T>void sortN(final List<T> col) {
        ArrayList<T> a = (ArrayList<T>) col;
        Collections.sort(a, new sortC());
    }
}
