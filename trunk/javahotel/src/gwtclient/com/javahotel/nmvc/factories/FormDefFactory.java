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
package com.javahotel.nmvc.factories;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.nmvc.common.FormLineDef;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.factories.impl.RecordFormDefFactory;

public class FormDefFactory extends HelperFactory implements IFormDefFactory {

    private final GetRecordDefFactory gFactory;
    private final RecordFormDefFactory dFactory;

    FormDefFactory(GetRecordDefFactory gFactory,RecordFormDefFactory dFactory) {
        this.gFactory = gFactory;
        this.dFactory = dFactory;
    }

    public FormLineContainer construct(IDataType dType) {
        FormLineContainer fe = dFactory.construct(dType);
        if (fe != null) { return fe; }
        DictData da = getDa(dType);
        List<RecordField> def = gFactory.getDef(da);
        List<FormField> formList = new ArrayList<FormField>();
        for (RecordField re : def) {
            FormField fo = new FormField(re.getPLabel(), new FormLineDef(re
                    .getELine()), new VField(re.getFie()), !re.isCanChange());
            formList.add(fo);
        }
        return new FormLineContainer(formList);
    }

    public String getFormTitle(IDataType dType) {
        DictData da = getDa(dType);
        return gFactory.getTitle(da);
   }

}
