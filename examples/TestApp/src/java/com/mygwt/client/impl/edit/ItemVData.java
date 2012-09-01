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
package com.mygwt.client.impl.edit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.LogT;
import com.mygwt.common.data.TOItemRecord;
import com.mygwt.common.data.ToEditRecord;

/**
 * @author hotel
 * 
 */
public class ItemVData extends AVModelData {

    private static final int NUMBER = 0;
    private static final int DATE = 1;
    private static final int NAME = 2;
    private static final int ID = 3;
    private static final int MARK = 4;

    private final ToEditRecord re;

    static final IVField fID = new VField(ID);
    static final IVField fNUMB = new VField(NUMBER);
    static final IVField fDATE = new VField(DATE);
    static final IVField fNAME = new VField(NAME);
    static final IVField fMARK = new VField(MARK);

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
                return FieldDataType.constructBigDecimal();
            case ID:
                return FieldDataType.constructInt();
            case MARK:
                return FieldDataType.constructBoolean();
            }
            return null;
        }

        @Override
        public String getId() {
            return CUtil.NumbToS(t);
        }

    }

    public ItemVData(ToEditRecord re) {
        this.re = re;
    }

    public ItemVData() {
        this.re = new ToEditRecord();
    }

    @Override
    public Object getF(IVField fie) {
        VField v = (VField) fie;
        switch (v.t) {
        case NUMBER:
            return re.getNumber();
        case DATE:
            return re.getDate();
        case NAME:
            return re.getName();
        case ID:
            return re.getRecId();
        case MARK:
            return new Boolean(re.isMark());
        }
        return null;
    }

    @Override
    public void setF(IVField fie, Object o) {
        VField v = (VField) fie;
        switch (v.t) {
        case NUMBER:
            re.setNumber((BigDecimal) o);
            break;
        case DATE:
            re.setDate((Date) o);
            break;
        case NAME:
            re.setName((String) o);
            break;
        case ID:
            assert o != null : LogT.getT().cannotBeNull();
            re.setRecId((Integer) o);
            break;
        case MARK:
            re.setMark((Boolean) o);
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
        i.add(fID);
        i.add(fMARK);
        return i;
    }

    /**
     * @return the re
     */
    public ToEditRecord getRe() {
        return re;
    }

}
