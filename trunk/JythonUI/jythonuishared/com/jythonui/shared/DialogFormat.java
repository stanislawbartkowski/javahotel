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
public class DialogFormat extends ElemDescription {

    private List<FieldItem> fieldList;

    private List<ButtonItem> leftButtonList;

    private List<ListFormat> listList;

    /**
     * @return the actionList
     */
    public List<FieldItem> getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList
     *            the fieldList to set
     */
    public void setFieldList(List<FieldItem> fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * @param actionList
     *            the actionList to set
     */
    public void setActionList(List<FieldItem> fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * @return the leftButtonList
     */
    public List<ButtonItem> getLeftButtonList() {
        return leftButtonList;
    }

    /**
     * @param leftButtonList
     *            the leftButtonList to set
     */
    public void setLeftButtonList(List<ButtonItem> leftButtonList) {
        this.leftButtonList = leftButtonList;
    }

    public String getJythonMethod() {
        return getAttr(ICommonConsts.METHOD);
    }

    public String getJythonImport() {
        return getAttr(ICommonConsts.IMPORT);
    }

    public static <T extends ElemDescription> T findE(List<T> eList, String id) {
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

    public void setListList(List<ListFormat> lList) {
        this.listList = lList;
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

}
