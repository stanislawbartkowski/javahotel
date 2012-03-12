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
package com.javahotel.nmvc.factories.bookingpanel;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;

/**
 * Local implementation for IVField. Contains only column number.
 * 
 * @author hotel
 * 
 */
class RField implements IVField {

    /**
     * @return the i
     */
    int getI() {
        return i;
    }

    private final int i;

    RField(int i) {
        this.i = i;
    }

    @Override
    public boolean eq(IVField o) {
        return false;
    }

    @Override
    public FieldDataType getType() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

}
