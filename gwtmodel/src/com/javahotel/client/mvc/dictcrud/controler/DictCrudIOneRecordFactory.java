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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.controller.onearecord.IOneARecord;
import com.javahotel.client.mvc.controller.onearecord.OneRecordFactory;
import com.javahotel.client.mvc.controller.onerecordmodif.IOneRecordModifWidget;
import com.javahotel.client.mvc.controller.onerecordmodif.OneRecordModifWidgetFactory;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordDefFactory;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.RecordViewFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.table.model.ITableFilter;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictCrudIOneRecordFactory {

    private final OneRecordModifWidgetFactory oFactory;
    private final IResLocator rI;
    private final GetRecordDefFactory gFactory;

    @Inject
    public DictCrudIOneRecordFactory(IResLocator rI,
            OneRecordModifWidgetFactory oFactory, GetRecordDefFactory gFactory) {
        this.rI = rI;
        this.oFactory = oFactory;
        this.gFactory = gFactory;
    }

    public IOneARecord getTableLineR(final DictData da,
            final IRecordView vTable, final ITableFilter tFilter) {
        ICrudRecordFactory fa = new DictRecordControler(rI, da,
                new RecordAuxParam());
        IOneARecord aR = OneRecordFactory.getR(rI, da, fa, vTable);
        IOneRecordModifWidget iM = oFactory.getTableWi(aR.getClick(tFilter));
        aR.setMWidget(iM);
        return aR;
    }

    public IRecordView createNViewCopy(final DictData da,
            final IRecordView vTable) {
        List<RecordField> dict = gFactory.getDef(da);
        List<RecordField> newList = new ArrayList<RecordField>();
        List<RecordField> iList = vTable.getModel().getFields();
        for (RecordField r : iList) {
            for (RecordField rr : dict) {
                if (r.getFie() == rr.getFie()) {
                    newList.add(r);
                }
            }
        }
        IRecordDef newM = RecordDefFactory.getRecordDef(rI, "", newList);
        IRecordView vi = RecordViewFactory.getTableViewRecord(rI, null, newM);
        return vi;
    }
}
