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

import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.command.RRoom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ResObjectP extends DictionaryP {

    public enum F implements IField {

        rtype, noperson, maxperson, standard
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
            case rtype:
                return RRoom.class;
            case noperson:
                return Integer.class;
            case maxperson:
                return Integer.class;
            case standard:
                return DictionaryP.class;
        }
        return null;
    }
    
    private RRoom rType;
    private Integer noPerson;
    private Integer maxPerson;
    private DictionaryP rStandard;
    private List<DictionaryP> facilities;

//    @Override
//    protected boolean emptySpecialTrue(final IField f) {
//        if (f instanceof ResObjectP.F) {
//            F fie = (F) f;
//            switch (fie) {
//                case maxperson: return ((maxPerson == 0) || (maxPerson == -1));
//                case noperson: return ((noPerson == 0) || (noPerson == -1));
//            }
//        }
//        return false;
//    }


    public ResObjectP() {
        facilities = new ArrayList<DictionaryP>();
    }

    public RRoom getRType() {
        return rType;
    }

    public void setRType(RRoom rType) {
        this.rType = rType;
    }

    public Integer getNoPerson() {
        return noPerson;
    }

    public void setNoPerson(Integer noPerson) {
        this.noPerson = noPerson;
    }

    public Integer getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(Integer maxPerson) {
        this.maxPerson = maxPerson;
    }

    public DictionaryP getRStandard() {
        return rStandard;
    }

    public void setRStandard(DictionaryP rStandard) {
        this.rStandard = rStandard;
    }

    public List<DictionaryP> getFacilities() {
        return facilities;
    }

    public void setFacilities(final List<DictionaryP> facilities) {
        this.facilities = facilities;
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;

        switch (fi) {
            case rtype:
                return getRType();
            case noperson:
                return getNoPerson();
            case maxperson:
                return getMaxPerson();
            case standard:
                return getRStandard();
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
        Integer i;

        switch (fi) {
            case rtype:
                setRType((RRoom) o);
                break;
            case noperson:
                i = (Integer) o;
                setNoPerson(i);
                break;
            case maxperson:
                i = (Integer) o;
                setMaxPerson(i);
                break;
            case standard:
                setRStandard((DictionaryP) o);
                break;
        }
    }
}
