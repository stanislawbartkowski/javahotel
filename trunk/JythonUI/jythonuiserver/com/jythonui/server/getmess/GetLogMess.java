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
package com.jythonui.server.getmess;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Properties;
import com.jythonui.server.Util;

import com.jythonui.shared.CustomMessages;

class GetLogMess implements IGetLogMess {

    private final Properties mess;
    private final Properties messdefa;

    GetLogMess(Properties mess, Properties messdefa) {
        this.mess = mess;
        this.messdefa = messdefa;
    }

    public String getMess(String errCode, String key, String... params) {
        String m = mess.getProperty(key);
        if (m == null && messdefa != null)
            m = messdefa.getProperty(key);
        if (errCode == null)
            return MessageFormat.format(m, params);
        return errCode + " " + MessageFormat.format(m, params);
    }

    public String getMessN(String key, String... params) {
        return getMess(null, key, params);
    }

    @Override
    public CustomMessages getCustomMess() {
        CustomMessages cust = new CustomMessages();
        Util.toElem(cust, mess);
        if (messdefa != null)
            Util.toElem(cust, messdefa);
        return cust;
    }

}
