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
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.json.IJsonConvert;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;

public class ExecuteJS {

    private ExecuteJS() {

    }

    public static DialogVariables execute(String id, String jsA,
            DialogVariables v) {
        IJsonConvert iJson = GwtGiniInjector.getI().getJsonConvert();
        v.setValueS(ICommonConsts.JSACTION, id);
        VVData vData = new VVData(v);
        String par = iJson.construct(vData);
        JavaScriptObject resO = Utils.callJsObjectFun(jsA, par);
        DialogVariables res = JSOToVariables.toV(resO);
        return res;
    }

}
