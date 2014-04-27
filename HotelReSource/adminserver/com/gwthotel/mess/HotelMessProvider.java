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
package com.gwthotel.mess;

import java.util.Map;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.jythonui.server.getbundle.ReadBundle;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

public class HotelMessProvider implements Provider<IGetLogMess> {

    private static final String dirName = "hmess";

    private Map<String, String> mess = null;

    @Inject
    private IReadResourceFactory iFactory;

    private void readProp() {
        if (mess != null)
            return;
        IReadResource iRead = iFactory.constructLoader(HotelMessProvider.class
                .getClassLoader());
        mess = ReadBundle.getBundle(iRead, null, dirName, "mess");
    }

    @Override
    public IGetLogMess get() {
        readProp();
        return GetLogMessFactory.construct(mess);
    }

}
