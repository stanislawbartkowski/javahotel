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
package com.jythonui.server.logmess;

import java.net.URL;
import java.util.Map;

import javax.inject.Provider;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getbundle.ReadBundle;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;

public class MessProvider implements Provider<IGetLogMess> {

    private static final String dirName = ISharedConsts.RESOURCES + "/mess";

    // mess
    private Map<String, String> mess = null;

    private void readProp() {
        if (mess != null)
            return;
        URL u = MessProvider.class.getClassLoader().getResource(dirName);
        mess = ReadBundle.getBundle(null, u.getFile(), "mess");
    }

    @Override
    public IGetLogMess get() {
        readProp();
        return GetLogMessFactory.construct(mess);
    }

}
