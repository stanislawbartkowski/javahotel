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
package com.mygwt.client.impl.mark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.mygwt.common.data.TOMarkRecord;

/**
 * @author hotel
 * 
 */
public class ItemVData extends AVModelData {

    private static final int NAME = 0;
    private static final int DATE = 1;
    private static final int MARKED = 2;
    private static final int NUMBER = 3;
    private static final int EDITMARKED = 4;

    private final TOMarkRecord re;

    static final IVField fNAME = new VField(NAME);
    static final IVField fDATE = new VField(DATE);
    static final IVField fMARKED = new VField(MARKED);
    static final IVField fNUMBER = new VField(NUMBER);
    static final IVField fEDITMARKED = new VField(EDITMARKED);

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
            case MARKED:
            case EDITMARKED:
                return FieldDataType.constructBoolean();
            case NUMBER:
                return FieldDataType.constructInt();
            }
            return null;
        }

        @Override
        public String getId() {
            return CUtil.NumbToS(t);
        }

    }

    public ItemVData(TOMarkRecord re) {
        this.re = re;
    }

    @Override
    public Object getF(IVField fie) {
        VField v = (VField) fie;
        switch (v.t) {
        case MARKED:
            return re.isMarked();
        case EDITMARKED:
            return re.isEditMark();
        case DATE:
            return re.getiDate();
        case NAME:
            return re.getiName();
        case NUMBER:
            return re.getNumber();
        }
        return null;
    }

    @Override
    public void setF(IVField fie, Object o) {
        VField v = (VField) fie;
        switch (v.t) {
        case NUMBER:
            re.setNumber((Integer) o);
            break;
        case DATE:
            re.setiDate((Date) o);
            break;
        case NAME:
            re.setiName((String) o);
            break;
        case EDITMARKED:
            re.setEditMark((Boolean) o);
            break;
        case MARKED:
            re.setMarked((Boolean) o);
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
        i.add(fNUMBER);
        i.add(fMARKED);
        i.add(fEDITMARKED);
        return i;
    }

    /**
     * @return the re
     */
    public TOMarkRecord getRe() {
        return re;
    }

}
