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
package com.gwtmodel.table;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class Empty {

    private Empty() {
    }

    private static class EmptyDataType implements IDataType {

        public boolean eq(IDataType dType) {
            return true;
        }
    }

    private static class EmptyFieldType implements IVField {

        public boolean eq(IVField o) {
            return true;
        }
    }
    private static IDataType eType;
    private static IVField eField;

    static {
        eType = new EmptyDataType();
        eField = new EmptyFieldType();
    }

    public static IVField getFieldType() {
        return eField;
    }

    public static IDataType getDataType() {
        return eType;
    }
}
