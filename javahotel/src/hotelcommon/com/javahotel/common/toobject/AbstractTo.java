/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
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

    protected boolean isOnList(IField f, IField[] ft) {
        for (IField t : ft) {
            if (t == f) {
                return true;
            }
        }
        return false;
    }

    public boolean isField(final IField f) {
        return isOnList(f, getT());
    }

    public LId getLId() {
        return (LId) getF(DictionaryP.F.id);
    }

    public void setLId(LId id) {
        setF(DictionaryP.F.id, id);
    }
}
