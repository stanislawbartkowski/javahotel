/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.json.IJsonConvert;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.interfaces.IExecuteJS;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

public class ExecuteJS implements IExecuteJS {

    private class JSResult implements IJSResult {

        private final DialogVariables v;

        JSResult(DialogVariables v) {
            this.v = v;
        }

        @Override
        public DialogVariables getV() {
            return v;
        }

        @Override
        public boolean isContinue() {
            FieldValue val = v.getValue(IUIConsts.JYTHONCONTINUE);
            if (val == null)
                return false;
            if (val.getType() == TT.STRING)
                return CUtil.EqNS(IConsts.JSTRUE, val.getValueS());
            if (val.getType() != TT.BOOLEAN)
                return false;
            return val.getValueB();
        }
    }

    @Override
    public IJSResult execute(String id, String jsA, DialogVariables v) {
        IJsonConvert iJson = GwtGiniInjector.getI().getJsonConvert();
        v.setValueS(ICommonConsts.JSACTION, id);
        String par = iJson.construct(new VVData(v));
        DialogVariables res = JSOToVariables.toV(Utils
                .callJsObjectFun(jsA, par));
        return new JSResult(res);
    }

}
