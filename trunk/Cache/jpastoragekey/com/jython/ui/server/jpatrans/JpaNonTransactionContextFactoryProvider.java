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
package com.jython.ui.server.jpatrans;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManagerFactory;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class JpaNonTransactionContextFactoryProvider implements
        Provider<ITransactionContextFactory> {

    @Inject
    private EntityManagerFactory eFactory;

    @Inject
    @Named(ISharedConsts.JYTHONMESSSERVER)
    IGetLogMess gMess;

    @Override
    public ITransactionContextFactory get() {
        return new ITransactionContextFactory() {

            @Override
            public IGetLogMess getMess() {
                return gMess;
            }

            @Override
            public ITransactionContext construct() {
                return new JpaNonTransactionContext(eFactory);
            }
        };
    }
}