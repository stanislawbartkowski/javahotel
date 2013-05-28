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
package com.jythonui.server;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.holder.Holder;
import com.jythonui.shared.RequestContext;

public class Util {

    private Util() {

    }

    public static void setLocale(RequestContext context) {
        String locale = context.getLocale();
        if (!CUtil.EmptyS(locale))
            Holder.SetLocale(locale);
    }

}