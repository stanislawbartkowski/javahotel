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

/**
 * @author hotel
 * 
 */
public class DialogFormat extends ElemDescription {

    private static final long serialVersionUID = 1L;

    private List<FieldItem> fieldList = new ArrayList<FieldItem>();

    private List<ButtonItem> leftButtonList = new ArrayList<ButtonItem>();

    private List<ListFormat> listList = new ArrayList<ListFormat>();

    private List<ButtonItem> buttonList = new ArrayList<ButtonItem>();

    private List<ButtonItem> actionList = new ArrayList<ButtonItem>();

    private List<TypesDescr> typeList = new ArrayList<TypesDescr>();

    private List<ValidateRule> valList = new ArrayList<ValidateRule>();

    private List<CheckList> checkList = new ArrayList<CheckList>();

    /**
     * @return the actionList
     */
    public List<FieldItem> getFieldList() {
        return fieldList;
    }

    /**
     * @return the leftButtonList
     */
    public List<ButtonItem> getLeftButtonList() {
        return leftButtonList;
    }

    public String getJythonMethod() {
        return getAttr(ICommonConsts.METHOD);
    }

    public String getJythonImport() {
        return getAttr(ICommonConsts.IMPORT);
    }

    public static <T extends ElemDescription> T findE(List<T> eList, String id) {
        if (eList == null) {
            return null;
        }
        for (T e : eList) {
            if (e.eqId(id)) {
                return e;
            }
        }
        return null;
    }

    public FieldItem findFieldItem(String id) {
        return findE(fieldList, id);
    }

    public List<ListFormat> getListList() {
        return listList;
    }

    public ListFormat findList(String id) {
        return findE(listList, id);
    }

    public boolean isBefore() {
        return isAttr(ICommonConsts.BEFORE);
    }

    public String getParent() {
        return getAttr(ICommonConsts.PARENT);
    }

    public List<ButtonItem> getButtonList() {
        return buttonList;
    }

    public List<ButtonItem> getActionList() {
        return actionList;
    }

    public List<TypesDescr> getTypeList() {
        return typeList;
    }

    public List<ValidateRule> getValList() {
        return valList;
    }

    public List<CheckList> getCheckList() {
        return checkList;
    }

    public TypedefDescr findCustomType(String customType) {
        for (TypesDescr ty : typeList) {
            TypedefDescr te = findE(ty.getTypeList(), customType);
            if (te != null) {
                return te;
            }
        }
        return null;
    }

    public CheckList findCheckList(String id) {
        return findE(checkList, id);
    }

}
