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
package com.gwtmodel.table.common;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class MaxI {

    private MaxI() {
    }

    public static int min(int m1, int m2) {
        if (m1 < m2) {
            return m1;
        }
        return m2;
    }

    public static int max(int m1, int m2) {
        if (m1 > m2) {
            return m1;
        }
        return m2;
    }

    public static Long min(Long m1, Long m2) {
        if (m1 < m2) {
            return m1;
        }
        return m2;
    }

    public static Long max(Long m1, Long m2) {
        if (m1 > m2) {
            return m1;
        }
        return m2;
    }

}
