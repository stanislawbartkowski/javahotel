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
package com.jythonui.server.ressession;

import javax.inject.Provider;
import javax.mail.Session;

import com.google.inject.Inject;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.envvar.IGetResourceJNDI;

public class ResGetMailSessionProvider extends UtilHelper implements
        Provider<Session> {

    private final IGetEnvVariable iEnv;
    private final IGetResourceJNDI iRes;

    @Inject
    public ResGetMailSessionProvider(IGetEnvVariable iEnv, IGetResourceJNDI iRes) {
        this.iEnv = iEnv;
        this.iRes = iRes;
    }

    @Override
    public Session get() {
        if (iRes.getMailName() == null)
            return null;
        return iEnv.getEnvString(iRes.getMailName(),
                IGetEnvVariable.ResType.MAIL, true).getSession();
    }
}
