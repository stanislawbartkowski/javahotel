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
package com.javahotel.client.types;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.javahotel.client.abstractto.IAbstractType;
import com.javahotel.client.injector.HInjector;
import com.javahotel.common.toobject.IField;

public class VField implements IVField {

    private final IField fie;
    private final FieldDataType fType;

    public VField(IField fie, FieldDataType fType) {
        this.fie = fie;
        this.fType = fType;
    }

    public VField(IField fie) {
        this.fie = fie;
        IAbstractType i = HInjector.getI().getAbstractType();
        fType = i.construct(fie).getT();
    }

    public IField getFie() {
        return fie;
    }

    @Override
    public boolean eq(IVField mFie) {
        VField de = (VField) mFie;
        return fie.equals(de.fie);
    }

    @Override
    public FieldDataType getType() {
        return fType;
    }

    @Override
    public String getId() {
        return fie.toString();
    }
}
