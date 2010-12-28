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
package com.javahotel.client.mvc.table.model;

import com.gwtmodel.table.FieldDataType;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author hotel
 */
public class ColTitle {

    private final IField f;
    private final String cTitle;
    private final FieldDataType cType;

    public ColTitle(final IField f, final String cTitle) {
        this.f = f;
        this.cTitle = cTitle;
        cType = AbstractToFactory.getT(f).getT();
    }

    public ColTitle(final IField f, final String cTitle, FieldDataType cType) {
        this.f = f;
        this.cTitle = cTitle;
        this.cType = cType;
    }

    public FieldDataType getcType() {
        return cType;
    }

    /**
     * @return the f
     */
    public IField getF() {
        return f;
    }

    /**
     * @return the cTitle
     */
    public String getCTitle() {
        return cTitle;
    }
}
