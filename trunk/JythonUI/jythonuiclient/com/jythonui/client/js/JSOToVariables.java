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
package com.jythonui.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

class JSOToVariables {

    private JSOToVariables() {

    }

    abstract static class JSOModel extends JavaScriptObject {

        protected JSOModel() {

        }

        public final native JsArrayString keys() /*-{
			var a = new Array();
			for ( var p in this) {
				a.push(p);
			}
			return a;
        }-*/;

        public final native String get(String key) /*-{
			return "" + this[key];
        }-*/;
    }

    static DialogVariables toV(JavaScriptObject o) {
        DialogVariables v = new DialogVariables();
        JSOModel mo = (JSOModel) o;
        JsArrayString a = mo.keys();
        for (int i = 0; i < a.length(); i++) {
            String key = a.get(i);
            String val = mo.get(key);
            String type = mo.get(key + IUIConsts.JSADDTYPE);
            FieldDataType ff = null;
            if (IConsts.JSUNDEFINED.equals(type))
                ff = FieldDataType.constructString();
            else if (ICommonConsts.INTTYPE.equals(type))
                ff = FieldDataType.constructInt();
            else if (ICommonConsts.LONGTYPE.equals(type))
                ff = FieldDataType.constructLong();
            else if (ICommonConsts.BOOLTYPE.equals(type))
                ff = FieldDataType.constructBoolean();
            else if (ICommonConsts.DATETIMETYPE.equals(type))
                ff = FieldDataType.constructDate();
            else if (ICommonConsts.DATETYPE.equals(type))
                ff = FieldDataType.constructDate();
            else if (ICommonConsts.DECIMALTYPE.equals(type))
                ff = FieldDataType.constructBigDecimal();
            if (ff == null) {
                String mess = M.M().JavaScriptInvalideType(type,
                        key + IUIConsts.JSADDTYPE);
                Utils.errAlert(mess);
            }
            IVField vv = VField.construct(key, ff.getType());
            Object oVal = FUtils.getValue(vv, val);
            FieldValue fVal = new FieldValue();
            fVal.setValue(ff.getType(), oVal, ff.getAfterdot());
            v.setValue(key, fVal);
        }
        return v;
    }
}
