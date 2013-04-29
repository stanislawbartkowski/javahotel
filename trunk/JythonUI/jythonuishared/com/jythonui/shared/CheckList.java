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
package com.jythonui.shared;

import java.util.ArrayList;
import java.util.List;

public class CheckList extends ElemDescription {

    private CheckListElem lines = new CheckListElem();
    private CheckListElem columns = new CheckListElem();

    public CheckListElem getLines() {
        return lines;
    }

    public CheckListElem getColumns() {
        return columns;
    }
    
    public List<FieldItem> constructValLine() {
        List<FieldItem> valList = new ArrayList<FieldItem>();
        FieldItem fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(columns.getId());
        valList.add(fItem);
        fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.BOOLTYPE);
        fItem.setId(ICommonConsts.CHECKLINEVALUE);
        valList.add(fItem);
        return valList;
    }


}
