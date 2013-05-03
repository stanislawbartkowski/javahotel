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

import java.io.Serializable;

public class DialogInfo implements Serializable {

    private DialogFormat dialog;
    private SecurityInfo security;
    private CustomMessages custMess;

    // default constructor
    public DialogInfo() {
    }

    public DialogInfo(DialogFormat dialog, SecurityInfo security,
            CustomMessages custMess) {
        this.dialog = dialog;
        this.security = security;
        this.custMess = custMess;
    }

    public DialogFormat getDialog() {
        return dialog;
    }

    public SecurityInfo getSecurity() {
        return security;
    }

    public CustomMessages getCustMess() {
        return custMess;
    }

}
