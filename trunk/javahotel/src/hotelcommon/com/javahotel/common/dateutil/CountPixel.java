/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

package com.javahotel.common.dateutil;

import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CountPixel {

    private CountPixel() {
    }

    private static int[] count(final int pixw, final int maxw, 
            final List<Integer> sou) {
        int[] aI = new int[sou.size()];
        int i = 0;
        for (Integer ii : sou) {
            float f = (ii.intValue() * pixw) / maxw;
            Integer io = new Integer(Math.round(f));
            aI[i++] = io;
        }
        return aI;
    }

    private static int sum(final int[] sou) {
        int sum = 0;
        for (int i : sou) {
            sum += i;
        }
        return sum;
    }

    public static int[] countP(final int pixw, final int maxw, 
            final List<Integer> sou) {
        int[] c1 = count(pixw, maxw, sou);
        int restw = pixw - sum(c1);
        int[] c2 = count(restw, sum(c1), sou);
        for (int i = 0; i < c1.length; i++) {
            c1[i] += c2[i];
        }
        restw = pixw - sum(c1);
        while (restw != 0) {
            for (int i = 0; i < c1.length; i++) {
                if (restw < 0) {
                    restw++;
                    c1[i]--;
                }
                if (restw > 0) {
                    restw--;
                    c1[i]++;
                }
            }
        }
        return c1;
    }
}
