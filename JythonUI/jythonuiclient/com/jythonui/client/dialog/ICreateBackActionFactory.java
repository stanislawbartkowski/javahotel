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
package com.jythonui.client.dialog;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.MapDialogVariable;

public interface ICreateBackActionFactory {

    CommonCallBack<DialogVariables> construct(String id, WSize w,
            MapDialogVariable addV, ICommand iAfter);

}
