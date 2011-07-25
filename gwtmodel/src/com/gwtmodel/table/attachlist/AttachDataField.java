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
package com.gwtmodel.table.attachlist;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.IConstUtil;

/**
 *
 * @author perseus
 */
public class AttachDataField implements IVField {

    public static final IVField vfilename = new AttachDataField(F.FILENAME);
    public static final IVField vcomment = new AttachDataField(F.COMMENT);
    public static final IVField vid = new AttachDataField(F.ID);
    public static final IVField vadddate = new AttachDataField(F.ADDDATE);
    public static final IVField vmodifdate = new AttachDataField(F.MODIFDATE);

    /**
     * @return the fie
     */
    public F getFie() {
        return fie;
    }

    @Override
    public FieldDataType getType() {
        switch (fie) {
            case ADDDATE:
            case MODIFDATE:
                return FieldDataType.constructDate();
            case COMMENT:
            case FILENAME:
                return FieldDataType.constructString();
            case ID:
                return FieldDataType.constructInt();
        }
        return null;
    }

    @Override
    public String getId() {
        if (fie == F.FILENAME) {
            return IConstUtil.FILENAMEID;
        }
        return fie.toString();
    }

    public enum F {

        ID, COMMENT, FILENAME, ADDDATE, MODIFDATE
    };
    private final F fie;

    @Override
    public boolean eq(IVField o) {
        AttachDataField d = (AttachDataField) o;
        return d.getFie() == getFie();
    }

    AttachDataField(F fie) {
        this.fie = fie;
    }
}
