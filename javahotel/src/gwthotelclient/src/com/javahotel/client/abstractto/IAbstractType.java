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
package com.javahotel.client.abstractto;

import com.gwtmodel.table.FieldDataType;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
public interface IAbstractType {

    public static class T {

        private final Class<?> c;
        private final FieldDataType t;

        public T(Class<?> c, FieldDataType t) {
            this.c = c;
            this.t = t;
        }

        /**
         * @return the c
         */
        public Class<?> getC() {
            return c;
        }

        /**
         * @return the t
         */
        public FieldDataType getT() {
            return t;
        }
    }

    T construct(IField i);

}
