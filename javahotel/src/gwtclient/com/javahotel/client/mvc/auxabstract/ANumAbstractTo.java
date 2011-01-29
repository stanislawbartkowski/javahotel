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
package com.javahotel.client.mvc.auxabstract;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ANumAbstractTo<T extends AbstractTo> extends NumAbstractTo {

    private Integer lp = null;

    @Override
    public void copyFrom(final AbstractTo from) {
        ANumAbstractTo<T> f = (ANumAbstractTo<T>) from;
        lp = f.getLp();
        this.setO(f.getO());
    }

    public ANumAbstractTo(final T a, final IField[] l) {
        super(a, l);
    }

    @Override
    public Class getT(IField f) {
        return a.getT(f);
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lP) {
        this.lp = lP;
    }
}
