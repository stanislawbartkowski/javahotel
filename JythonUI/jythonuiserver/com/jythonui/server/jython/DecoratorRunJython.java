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
package com.jythonui.server.jython;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jythonui.server.IConsts;
import com.jythonui.server.IExecuteJython;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class DecoratorRunJython implements IExecuteJython {

    private final IExecuteJython iJython;
    private final IJythonUIServerProperties p;

    @Inject
    public DecoratorRunJython(
            @Named(IConsts.GENERICRUNJYTHON) IExecuteJython iJython,
            IJythonUIServerProperties p) {
        this.iJython = iJython;
        this.p = p;
    }

    @Override
    public void executeJython(DialogVariables v, DialogFormat d, String actionId) {
        if (p.isSerialized())
            sexecuteJython(v, d, actionId);
        else
            iJython.executeJython(v, d, actionId);
    }

    private synchronized void sexecuteJython(DialogVariables v, DialogFormat d,
            String actionId) {
        iJython.executeJython(v, d, actionId);
    }

}
