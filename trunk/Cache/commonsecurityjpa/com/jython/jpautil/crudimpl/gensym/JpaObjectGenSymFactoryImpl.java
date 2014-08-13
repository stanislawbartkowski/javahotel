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
package com.jython.jpautil.crudimpl.gensym;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaEmTransactionContext;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.objectgensymimpl.CrudObjectGenSym;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.gensym.ISymGeneratorFactory;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.storage.seq.ISequenceRealmGenFactory;

public class JpaObjectGenSymFactoryImpl implements IJpaObjectGenSymFactory {

    private final ISequenceRealmGenFactory seqFactory;
    private final ISymGeneratorFactory symFactory;
    private final IStorageJpaRegistryFactory regFactory;
    private final ISemaphore iSem;
    private final IGetLogMess gMess;

    @Inject
    public JpaObjectGenSymFactoryImpl(ISequenceRealmGenFactory seqFactory,
            ISymGeneratorFactory symFactory,
            IStorageJpaRegistryFactory regFactory,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            ISemaphore iSem) {
        this.seqFactory = seqFactory;
        this.symFactory = symFactory;
        this.regFactory = regFactory;
        this.iSem = iSem;
        this.gMess = gMess;
    }

    @Override
    public ICrudObjectGenSym construct(final EntityManager em) {

        ITransactionContextFactory tFactory = new ITransactionContextFactory() {

            @Override
            public ITransactionContext construct() {
                return new JpaEmTransactionContext(em);
            }
        };
        IStorageRealmRegistry iReg = regFactory.construct(tFactory);
        ISequenceRealmGen iSeq = seqFactory.construct(iReg, iSem);
        ISymGenerator iSym = symFactory.construct(iSeq);
        return new CrudObjectGenSym(iSym, gMess);
    }

}
