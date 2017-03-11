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
package com.jythonui.client.util;

import com.jythonui.client.M;
import com.jythonui.client.gini.UIGiniInjector;
import com.jythonui.shared.DialogVariables;
import com.polymerui.client.callback.CommonCallBack;

/**
 * @author hotel
 * 
 */
public class ExecuteAction {

    private ExecuteAction() {
    }


    public static void action(DialogVariables v, String dialogName, String actionId,
    		CommonCallBack<DialogVariables> callback) {
        M.JR().runAction(UIGiniInjector.getI().getRequestContext(), v, dialogName,
                actionId, callback);
    }

}
