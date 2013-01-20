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
package com.jythonui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jythonui.client.M;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public class ExecuteAction {

    private ExecuteAction() {
    }

    public static void action(IVariablesContainer iCon, String name,
            String actionId, AsyncCallback<DialogVariables> callback) {
        DialogVariables v = iCon.getVariables();
        M.JR().runAction(v, name, actionId, callback);
    }

    public static void action(DialogVariables v, String name, String actionId,
            AsyncCallback<DialogVariables> callback) {
        M.JR().runAction(v, name, actionId, callback);
    }

}
