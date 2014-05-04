/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.common.CUtil;

/**
 * @author hotel
 * 
 */
public class ListFormat extends ElemDescription {

    private static final long serialVersionUID = 1L;
    private List<FieldItem> colList = new ArrayList<FieldItem>();
    private List<ValidateRule> valList = new ArrayList<ValidateRule>();
    private DialogFormat fElem;
    private SecurityInfo elemSec;

    public String getElemFormat() {
        return getAttr(ICommonConsts.ELEMFORMAT);
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

    public SecurityInfo getElemSec() {
        return elemSec;
    }

    public void setElemSec(SecurityInfo elemSec) {
        this.elemSec = elemSec;
    }

    public String getStandButt() {
        return getAttr(ICommonConsts.STANDBUTT);
    }

    public int getPageSize() {
        if (CUtil.EmptyS(getAttr(ICommonConsts.PAGESIZE))) {
            return -1;
        }
        return CUtil.getInteger(getAttr(ICommonConsts.PAGESIZE));

    }

    public boolean isChunked() {
        return isAttr(ICommonConsts.CHUNKED);
    }

    public FieldItem getColumn(String id) {
        return DialogFormat.findE(colList, id);
    }

    public boolean isBeforeRowSignal() {
        return isAttr(ICommonConsts.SIGNALBEFOREROW);
    }

    public boolean isAfterRowSignal() {
        return isAttr(ICommonConsts.SIGNALAFTERROW);
    }

    public List<ValidateRule> getValList() {
        return valList;
    }

    public String getListButtonsWithList() {
        return getAttr(ICommonConsts.LISTBUTTONSLIST);
    }

    public String getListButtonsValidate() {
        return getAttr(ICommonConsts.LISTBUTTONSVALIDATE);
    }

    public String getJSModifRow() {
        return getAttr(ICommonConsts.JSMODIFROW);
    }

}
