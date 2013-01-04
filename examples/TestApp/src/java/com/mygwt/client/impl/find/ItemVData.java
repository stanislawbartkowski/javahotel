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
package com.mygwt.client.impl.find;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.mygwt.common.data.TOItemRecord;

/**
 * @author hotel
 * 
 */
public class ItemVData extends AVModelData {

    private static final int NUMBER = 0;
    private static final int DATE = 1;
    private static final int NAME = 2;
    private static final int ROOTLEVEL = 3;

    private final TOItemRecord re;

    static final IVField fNUMB = new VField(NUMBER);
    static final IVField fDATE = new VField(DATE);
    static final IVField fNAME = new VField(NAME);
    static final IVField fLEVEL = new VField(ROOTLEVEL);

    private static class VField implements IVField {

        private final int t;

        VField(int t) {
            this.t = t;
        }

        @Override
        public boolean eq(IVField o) {
            VField v = (VField) o;
            return t == v.t;
        }

        @Override
        public FieldDataType getType() {
            switch (t) {
            case NAME:
                return FieldDataType.constructString();
            case DATE:
                return FieldDataType.constructDate();
            case NUMBER:
            case ROOTLEVEL:
                return FieldDataType.constructInt();
            }
            return null;
        }

        @Override
        public String getId() {
            return CUtil.NumbToS(t);
        }

    }

    public ItemVData(TOItemRecord re) {
        this.re = re;
    }

    @Override
    public Object getF(IVField fie) {
        VField v = (VField) fie;
        switch (v.t) {
        case NUMBER:
            return re.getiNumber();
        case DATE:
            return re.getiDate();
        case NAME:
            return re.getiName();
        case ROOTLEVEL:
            return re.getRootLevel();
        }
        return null;
    }

    @Override
    public void setF(IVField fie, Object o) {
        VField v = (VField) fie;
        switch (v.t) {
        case NUMBER:
            re.setiNumber((Integer) o);
            break;
        case DATE:
            re.setiDate((Date) o);
            break;
        case NAME:
            re.setiName((String) o);
            break;
        case ROOTLEVEL:
            re.setRootLevel((Integer) o);
            break;
        }

    }

    @Override
    public boolean isValid(IVField fie) {
        return true;
    }

    @Override
    public List<IVField> getF() {
        List<IVField> i = new ArrayList<IVField>();
        i.add(fDATE);
        i.add(fNAME);
        i.add(fNUMB);
        i.add(fLEVEL);
        return i;
    }

    /**
     * @return the re
     */
    public TOItemRecord getRe() {
        return re;
    }

}
