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
package com.jythonui.server.logmess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Provider;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;

public class MessProvider implements Provider<IGetLogMess> {

    private static final String fileName = ISharedConsts.RESOURCES
            + "/mess/mess.properties";

    private Properties mess = null;

    private void readProp() {
        if (mess != null)
            return;
        mess = new Properties();
        InputStream i = MessProvider.class.getClassLoader()
                .getResourceAsStream(fileName);
        try {
            mess.load(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IGetLogMess get() {
        readProp();
        return GetLogMessFactory.construct(mess);
    }

}
