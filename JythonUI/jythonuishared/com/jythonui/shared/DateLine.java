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
    private List<FormDef> formList = new ArrayList<FormDef>();

    public List<FieldItem> getColList() {
        return colList;
    }

    public List<FormDef> getFormList() {
        return formList;
    }

    public int getColNo() {
        return getInt(ICommonConsts.COLNO, ICommonConsts.DEFAULTCOLNO);
    }

    public int getRowNo() {
        return getInt(ICommonConsts.PAGESIZE, ICommonConsts.DEFAULTROWNO);
    }

    public String getListId() {
        return getAttr(ICommonConsts.DATELINEID);
    }

    public String getDateColId() {
        return getAttr(ICommonConsts.DATELINEDATEID,
                ICommonConsts.DATELINEDATEIDDEFAULT);
    }

    public String getDateFile() {
        return getAttr(ICommonConsts.DATALINEFILE,
                ICommonConsts.DATALINEFILEDEFAULT);
    }

    public String getDefaFile() {
        return getAttr(ICommonConsts.DATELINEDEFAFILE);
    }
    
    public String getForm() {
        return ICommonConsts.DATELINEFORMDEFAULT;
    }

    public FieldItem getFieldId() {
        return DialogFormat.findE(getColList(), getListId());
    }

    public List<FieldItem> constructDataLine() {
        List<FieldItem> colList = new ArrayList<FieldItem>();
        colList.add(getFieldId());
        DialogFormat.addDefStringCols(colList,
                getForm());
        DialogFormat.addDefDataCols(colList, getDateColId());
        return colList;
    }

    public List<FieldItem> constructQueryLine() {
        List<FieldItem> colList = new ArrayList<FieldItem>();
        colList.add(getFieldId());
        DialogFormat.addDefDataCols(colList, ICommonConsts.JDATELINEQUERYFROM,
                ICommonConsts.JDATELINEQEURYTO);
        return colList;
    }

}
