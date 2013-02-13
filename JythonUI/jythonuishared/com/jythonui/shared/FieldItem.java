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

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;

/**
 * @author hotel
 * 
 */
public class FieldItem extends ElemDescription {

    public String getTypeName() {
        return getAttr(ICommonConsts.TYPE);
    }

    public int getAfterDot() {
        String t = getAttr(ICommonConsts.AFTERDOT);
        if (CUtil.EmptyS(t)) {
            return ICommonConsts.DEFAULTAFTERDOT;
        }
        return CUtil.getNumb(t.trim());
    }

    public static String getCustomT(String t) {
        int p = t.indexOf(ICommonConsts.CUSTOMTYPE);
        if (p != 0) {
            return null;
        }
        return t.substring(ICommonConsts.CUSTOMTYPE.length());
    }

    public String getCustom() {
        String t = getTypeName();
        if (CUtil.EmptyS(t)) {
            return null;
        }
        return getCustomT(t);
    }

    public TT getFieldType() {
        String t = getTypeName();
        if (CUtil.EmptyS(t) || CUtil.EqNS(t, ICommonConsts.STRINGTYPE)
                || CUtil.EqNS(t, ICommonConsts.TEXTAREA)
                || CUtil.EqNS(t, ICommonConsts.RICHTEXT)) {
            return TT.STRING;
        }
        if (getCustom() != null) {
            return TT.STRING;
        }
        if (CUtil.EqNS(t, ICommonConsts.BOOLTYPE)) {
            return TT.BOOLEAN;
        }
        if (CUtil.EqNS(t, ICommonConsts.DATETIMETYPE)) {
            return TT.DATETIME;
        }
        if (CUtil.EqNS(t, ICommonConsts.DATETYPE)) {
            return TT.DATE;
        }
        if (CUtil.EqNS(t, ICommonConsts.LONGTYPE)) {
            return TT.LONG;
        }
        if (CUtil.EqNS(t, ICommonConsts.INTTYPE)) {
            return TT.INT;
        }
        if (CUtil.EqNS(t, ICommonConsts.DECIMALTYPE)) {
            return TT.BIGDECIMAL;
        }
        throw new JythonUIFatal(t + " unexpected type name");
    }

    public boolean isNotEmpty() {
        return isAttr(ICommonConsts.NOTEMPTY);
    }

    public boolean isHidden() {
        return isAttr(ICommonConsts.HIDDEN);
    }

    public boolean isReadOnly() {
        return isAttr(ICommonConsts.READONLY);
    }

    public boolean isReadOnlyAdd() {
        return isAttr(ICommonConsts.READONLYADD);
    }

    public boolean isReadOnlyChange() {
        return isAttr(ICommonConsts.READONLYCHANGE);
    }

    public String getActionId() {
        return getAttr(ICommonConsts.ACTIONID);
    }

    public boolean isSignalChange() {
        return isAttr(ICommonConsts.SIGNALCHANGE);
    }

    public boolean isHelper() {
        return isAttr(ICommonConsts.HELPER);
    }

    public boolean isHelperRefresh() {
        return isAttr(ICommonConsts.HELPERREFRESH);
    }

    public boolean isTextArea() {
        return CUtil.EqNS(getTypeName(), ICommonConsts.TEXTAREA);
    }

    public boolean isRichText() {
        return CUtil.EqNS(getTypeName(), ICommonConsts.RICHTEXT);
    }

    public String getFrom() {
        return getAttr(ICommonConsts.FROM);
    }

    public String getAlign() {
        return getAttr(ICommonConsts.ALIGN);
    }

}
