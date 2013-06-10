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

public class DateLine extends ElemDescription {

    private static final long serialVersionUID = 1L;

    private List<FieldItem> colList = new ArrayList<FieldItem>();

    public List<FieldItem> getColList() {
        return colList;
    }

    public int getColNo() {
        return getInt(ICommonConsts.COLNO, ICommonConsts.DEFAULTCOLNO);
    }

    public int getRowNo() {
        return getInt(ICommonConsts.PAGESIZE, ICommonConsts.DEFAULTROWNO);
    }
    
    public String getListId() {
        return getAttr(ICommonConsts.DETELINEID);
    }

}
