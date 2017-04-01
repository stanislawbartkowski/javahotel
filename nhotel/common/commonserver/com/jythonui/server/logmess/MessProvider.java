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
package com.jythonui.server.logmess;

import java.util.Map;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.jythonui.server.getbundle.ReadBundle;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

public class MessProvider implements Provider<IGetLogMess> {

    private static final String dirName = "mess";

    @Inject
    private IReadResourceFactory iFactory;

    // mess
    private Map<String, String> mess = null;

    private void readProp() {
        if (mess != null)
            return;
        IReadResource iRes = iFactory.constructLoader(MessProvider.class
                .getClassLoader());
        mess = ReadBundle.getBundle(iRes, null, dirName, "mess");
    }

    @Override
    public IGetLogMess get() {
        readProp();
        return GetLogMessFactory.construct(mess);
    }

}
