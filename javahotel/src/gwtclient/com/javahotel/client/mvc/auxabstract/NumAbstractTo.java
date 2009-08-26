/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;
import com.javahotel.types.INumerable;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public abstract class NumAbstractTo extends AbstractTo implements INumerable {

    protected transient final AbstractTo a;
    protected transient final IField[] l;

    protected NumAbstractTo(final AbstractTo pa, final IField[] pl) {
        this.a = pa;
        this.l = pl;
    }

    @Override
    public IField[] getT() {
        return l;
    }

    @Override
    public Object getF(final IField f) {
        return a.getF(f);
    }

    @Override
    public void setF(final IField f, final Object val) {
        a.setF(f, val);
    }

    public AbstractTo getO() {
        return a;
    }

    public void setO(final AbstractTo sou) {
        CommandUtil.copyA(sou, getO(), l);
    }
}
