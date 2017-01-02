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
package com.jythonui.client.dialog.execute;

import com.google.inject.Inject;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.interfaces.IExecuteBackAction;
import com.jythonui.client.interfaces.IExecuteJS;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogVariables;

public class ExecuteBackAction implements IExecuteBackAction {

    private final IExecuteJS executeJS;

    @Inject
    public ExecuteBackAction(IExecuteJS executeJS) {
        this.executeJS = executeJS;
    }

    @Override
    public void execute(IBackFactory bFactory, DialogVariables v,
            ButtonItem bItem, String dialogName, String actionId) {

        if (bItem != null) {
            String jsA = Utils.getJS(bItem.getJsAction());
            if (!CUtil.EmptyS(jsA)) {
                IExecuteJS.IJSResult res = executeJS.execute(actionId, jsA, v);
                if (!res.isContinue()) {
                    bFactory.construct().onSuccess(res.getV());
                    return;
                }
            }
        }
        ExecuteAction.action(v, dialogName, actionId, bFactory.construct());
    }

}
