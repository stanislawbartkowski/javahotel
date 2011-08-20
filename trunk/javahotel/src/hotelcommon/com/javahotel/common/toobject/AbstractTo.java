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
package com.javahotel.common.toobject;

import java.io.Serializable;
import java.util.Date;

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.util.StringU;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public abstract class AbstractTo implements Serializable {

    public interface IFieldToS {

        String toS(IField f, Object va);
    }

    public abstract Object getF(IField f);

    public abstract Class<?> getT(IField f);

    public abstract void setF(IField f, Object o);

    public abstract IField[] getT();

    public IField getF(String s) {
        IField[] ft = getT();
        for (int i = 0; i < ft.length; i++) {
            if (ft[i].toString().equals(s)) {
                return ft[i];
            }
        }
        return null;
    }

    protected boolean emptySpecialTrue(final IField f) {
        return false;
    }

    // TODO : remove
    private String getDispS(final IField f, final IFieldToS i) {
        if (emptySpecialTrue(f)) {
            return null;
        }
        Object va = getF(f);
        if (va == null) {
            return null;
        }
        if (i != null) {
            String s = i.toS(f, va);
            if (s != null) {
                return s;
            }
        }
        if (va instanceof String) {
            return (String) va;
        }
        if (va instanceof DictionaryP) {
            DictionaryP p = (DictionaryP) va;
            return p.getName();
        }
        if (va instanceof Date) {
            String s = DateFormatUtil.toS((Date) va);
            return s;

        }
        return va.toString();
    }

    // TODO : REMOVE
    private String getS(final IField f) {
        return getDispS(f, null);
    }

    // TODO: REMOVE
    private boolean emptyS(final IField f) {
        String val = getS(f);
        return StringU.isEmpty(val);
    }

    public void copyFrom(final AbstractTo from) {
        CommandUtil.copyA(from, this, getT());
    }

    public boolean isField(final IField f) {
        for (IField t : getT()) {
            if (t == f) {
                return true;
            }
        }
        return false;
    }

    public LId getLId() {
        return (LId) getF(DictionaryP.F.id);
    }

    public void setLId(LId id) {
        setF(DictionaryP.F.id, id);
    }
}
