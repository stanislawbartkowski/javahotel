/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table;

/**
 * @author hotel
 * 
 */
public class MutableInteger {

    private int val;

    public MutableInteger(int val) {
        this.val = val;
    }

    public MutableInteger(Integer val) {
        this.val = val;
    }

    public int intValue() {
        return val;
    }

    public void inc() {
        val++;
    }

    public void dec() {
        val--;
    }

    @Override
    public boolean equals(Object o) {
        MutableInteger m = (MutableInteger) o;
        return val == m.val;
    }

    @Override
    public int hashCode() {
        return new Integer(val).hashCode();
    }

}
