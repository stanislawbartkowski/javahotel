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

import com.gwtmodel.table.common.CUtil;

public class CheckList extends ElemDescription {

    private static final long serialVersionUID = 1L;
    private CheckListElem lines = new CheckListElem();
    private CheckListElem columns = new CheckListElem();

    public CheckListElem getLines() {
        return lines;
    }

    public CheckListElem getColumns() {
        return columns;
    }

    public boolean isBoolean() {
        if (!isAttr(ICommonConsts.TYPE))
            return true;
        return CUtil.EqNS(getAttr(ICommonConsts.TYPE), ICommonConsts.BOOLTYPE);
    }

    public boolean isDecimal() {
        if (!isAttr(ICommonConsts.TYPE))
            return false;
        return CUtil.EqNS(getAttr(ICommonConsts.TYPE),
                ICommonConsts.DECIMALTYPE);
    }

    public List<FieldItem> constructValLine() {
        List<FieldItem> valList = new ArrayList<FieldItem>();
        FieldItem fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(columns.getId());
        valList.add(fItem);
        fItem = new FieldItem();
        if (isBoolean())
            fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.BOOLTYPE);
        else {
            fItem.setAttr(ICommonConsts.TYPE, getAttr(ICommonConsts.TYPE));
            fItem.setAttr(ICommonConsts.AFTERDOT,
                    getAttr(ICommonConsts.AFTERDOT));
        }
        fItem.setId(ICommonConsts.CHECKLINEVALUE);
        valList.add(fItem);
        return valList;
    }

    public List<FieldItem> constructErrLine() {
        List<FieldItem> valList = new ArrayList<FieldItem>();

        FieldItem fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(ICommonConsts.CHECKERRORROW);
        valList.add(fItem);

        fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(ICommonConsts.CHECKERRORCOL);
        valList.add(fItem);

        fItem = new FieldItem();
        fItem.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
        fItem.setId(ICommonConsts.CHECKERRORMESS);
        valList.add(fItem);
        return valList;
    }

    public int getAfterDot() {
        return FieldItem.getAfterDot(getAttr(ICommonConsts.AFTERDOT));
    }

}
