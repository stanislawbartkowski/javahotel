/*
 *  Copyright 2012 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.injector;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 *
 * @author hotel
 */
public class CallContext implements ICallContext {

    private final TablesFactories tFactories;
    private IDataType dType;
    private ISlotable iSlo;
    private final ITableCustomFactories cFactories;
    private PersistTypeEnum persistTypeEnum;

    /**
     * @return the persistTypeEnum
     */
    public PersistTypeEnum getPersistTypeEnum() {
        return persistTypeEnum;
    }

    /**
     * @param persistTypeEnum the persistTypeEnum to set
     */
    public void setPersistTypeEnum(PersistTypeEnum persistTypeEnum) {
        this.persistTypeEnum = persistTypeEnum;
    }

    @Inject
    public CallContext(TablesFactories tFactories, ITableCustomFactories cFactories) {
        this.tFactories = tFactories;
        this.cFactories = cFactories;
    }

    @Override
    public IDataType getDType() {
        return dType;
    }

    @Override
    public ISlotable iSlo() {
        return iSlo;
    }

    @Override
    public TablesFactories getT() {
        return tFactories;
    }

    /**
     * @param dType the dType to set
     */
    @Override
    public void setdType(IDataType dType) {
        this.dType = dType;
    }

    /**
     * @param iSlo the iSlo to set
     */
    @Override
    public void setiSlo(ISlotable iSlo) {
        this.iSlo = iSlo;
    }

    @Override
    public ITableCustomFactories getC() {
        return cFactories;
    }

    @Override
    public ICallContext construct(IDataType dType) {
        CallContext c = (CallContext) GwtGiniInjector.getI().getCallContext();
        c.iSlo = iSlo;
        c.dType = dType;
        return c;
    }

    @Override
    public ICallContext construct(ISlotable iSlo, IDataType dType) {
        CallContext c = (CallContext) GwtGiniInjector.getI().getCallContext();
        c.iSlo = iSlo;
        c.dType = dType;
        return c;
    }

    @Override
    public ICallContext construct(ISlotable iSlo) {
        CallContext c = (CallContext) GwtGiniInjector.getI().getCallContext();
        c.iSlo = iSlo;
        c.dType = dType;
        return c;
    }
}
