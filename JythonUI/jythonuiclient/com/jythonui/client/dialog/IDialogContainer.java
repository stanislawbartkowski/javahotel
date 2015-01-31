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
package com.jythonui.client.dialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;

public interface IDialogContainer extends ISlotable {

    DialogInfo getInfo();

    IVariablesContainer getiCon();

    boolean okCheckListError(DialogVariables v);

    DialogFormat getD();

    void executeAction(String actionId, AsyncCallback<DialogVariables> callback);

    void close();
}
