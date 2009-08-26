/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.dictcrud.controler;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.controller.onearecord.IOneARecord;
import com.javahotel.client.mvc.controller.onearecord.OneRecordFactory;
import com.javahotel.client.mvc.controller.onerecordmodif.IOneRecordModifWidget;
import com.javahotel.client.mvc.controller.onerecordmodif.OneRecordModifWidgetFactory;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import java.util.ArrayList;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.record.model.RecordDefFactory;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.RecordViewFactory;
import com.javahotel.client.mvc.table.model.ITableFilter;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DictCrudIOneRecordFactory {

    private DictCrudIOneRecordFactory() {
    }

    public static IOneARecord getTableLineR(final IResLocator rI,
            final DictData da, final IRecordView vTable,
            final  ITableFilter tFilter) {
        ICrudRecordFactory fa = new DictRecordControler(rI, da,
                new RecordAuxParam());
        IOneARecord aR = OneRecordFactory.getR(rI, da, fa, vTable);
        IOneRecordModifWidget iM = OneRecordModifWidgetFactory.getTableWi(rI,
                aR.getClick(tFilter));
        aR.setMWidget(iM);
        return aR;
    }

    public static IRecordView createNViewCopy(final IResLocator rI,
            final DictData da, final IRecordView vTable) {
       ArrayList<RecordField> dict = GetRecordDefFactory.getDef(rI, da);
       ArrayList<RecordField> newList = new ArrayList<RecordField>();
       ArrayList<RecordField> iList = vTable.getModel().getFields();
       for (RecordField r : iList) {
           for (RecordField rr: dict) {
               if (r.getFie() == rr.getFie()) {
                   newList.add(r);
               }
           }
       }
       IRecordDef newM = RecordDefFactory.getRecordDef(rI, "", newList);
       IRecordView vi = RecordViewFactory.getTableViewRecord(rI, newM);
       return vi;
    }
}
