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

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.command.ServiceType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class ServiceDictionaryP extends DictionaryP {

    private ServiceType servType;
    private VatDictionaryP vat;
    private Integer noPerson;

    /**
     * @return the noPerson
     */
    public Integer getNoPerson() {
        return noPerson;
    }

    /**
     * @param noPerson
     *            the noPerson to set
     */
    public void setNoPerson(Integer noPerson) {
        this.noPerson = noPerson;
    }

    public ServiceType getServType() {
        return servType;
    }

    public void setServType(ServiceType servType) {
        this.servType = servType;
    }

    public VatDictionaryP getVat() {
        return vat;
    }

    public void setVat(VatDictionaryP vat) {
        this.vat = vat;
    }

    public enum F implements IField {

        servtype, vat, noPerson
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
        case noPerson:
            return Integer.class;
        case servtype:
            return ServiceType.class;
        case vat:
            return VatDictionaryP.class;
        }
        return null;
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;

        switch (fi) {
        case servtype:
            return getServType();
        case vat:
            return getVat();
        case noPerson:
            return getNoPerson();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            super.setF(pfi, o);
            return;
        }

        F fi = (F) f;

        switch (fi) {
        case servtype:
            setServType((ServiceType) o);
            break;
        case vat:
            setVat((VatDictionaryP) o);
            break;
        case noPerson:
            setNoPerson((Integer) o);
            break;
        }
    }
}