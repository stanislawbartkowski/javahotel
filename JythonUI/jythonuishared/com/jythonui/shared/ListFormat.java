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

import java.util.List;

/**
 * @author hotel
 * 
 */
public class ListFormat extends ElemDescription {

    private List<FieldItem> colList;
    private DialogFormat fElem;

    public String getElemFormat() {
        return getAttr(ICommonConsts.ELEMFORMAT);
    }

    public void setColumns(List<FieldItem> colList) {
        this.colList = colList;
    }

    public List<FieldItem> getColumns() {
        return colList;
    }

    /**
     * @return the fElem
     */
    public DialogFormat getfElem() {
        return fElem;
    }

    /**
     * @param fElem
     *            the fElem to set
     */
    public void setfElem(DialogFormat fElem) {
        this.fElem = fElem;
    }

}
