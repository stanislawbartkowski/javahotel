/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

public class CheckListElem extends ElemDescription {

    private static final long serialVersionUID = 1L;

    public CheckListElem() {
        setId(ICommonConsts.ID);
        setAttr(ICommonConsts.DISPLAYNAME, ICommonConsts.DISPLAYNAME);
    }

    public List<FieldItem> constructCol() {
        List<FieldItem> colList = new ArrayList<FieldItem>();
        FieldItem fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(getId());
        colList.add(fItem);
        fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(getDisplayName());
        colList.add(fItem);
        return colList;
    }

}
