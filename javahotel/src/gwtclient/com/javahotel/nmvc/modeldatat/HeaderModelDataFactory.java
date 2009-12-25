/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.modeldatat;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.dialog.DictData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.listheadermodel.ListHeaderDesc;

public class HeaderModelDataFactory {
    
    private HeaderModelDataFactory() {       
    }
    
    public static List<ListHeaderDesc> constructList(DataType dType) {
        DictData dicData = new DictData(dType.getdType());
        ColListFactory cFactory =  HInjector.getI().getColListFactory();
        List<ColTitle> coList = cFactory.getColList(dicData);
        List<ListHeaderDesc> heList = new ArrayList<ListHeaderDesc>();
        for (ColTitle co : coList) {
            ListHeaderDesc he = new ListHeaderDesc(co.getCTitle(),co.getF());
            heList.add(he);
        }
        return heList;
    }

}
