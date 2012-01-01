/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class BankAccountP extends AbstractToILd {

    @Override
    public Class<?> getT(IField f) {
        F fi = (F) f;
        Class<?> cla = String.class;
        switch (fi) {
        case id:
            cla = LId.class;
            break;
        }
        return cla;
    }

    public enum F implements IField {
        id, accountNumber
    };

    private LId id;

    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LId getId() {
        return id;
    }

    public void setId(LId id) {
        this.id = id;
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return id;
        case accountNumber:
            return getAccountNumber();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case id:
            this.id = (LId) o;
            break;
        case accountNumber:
            setAccountNumber((String) o);
        }
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

}
