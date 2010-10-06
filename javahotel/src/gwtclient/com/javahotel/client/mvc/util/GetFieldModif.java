/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.util;

import com.javahotel.client.mvc.dictcrud.controler.booking.*;
import com.google.gwt.user.client.ui.ChangeListener;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.dictcrud.controler.IModifRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.common.toobject.IField;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetFieldModif {

    private final IField eField;
    private ILineField eres;
    private IChangeListener changeL;

    /**
     * @param changeL the changeL to set
     */
    public void setChangeL(IChangeListener changeL) {
        this.changeL = changeL;
        if (eres != null) {
            eres.setChangeListener(changeL);
        }
    }

    private class ModifE implements IModifRecordDef {

        public void modifRecordDef(List<RecordField> dict) {
            for (RecordField re : dict) {
                if (re.getFie() == eField) {
                    eres = re.getELine();
                    if (changeL != null) {
                        eres.setChangeListener(changeL);
                    }
                }
            }
        }
    }

    public IModifRecordDef getModif() {
        return new ModifE();
    }

    public IGetELineDialog getE() {
        return new IGetELineDialog() {

            public ILineField getE() {
                return eres;
            }
        };
    }

    public GetFieldModif(IField f) {
        this.eField = f;
        changeL = null;
        eres = null;
    }
}
