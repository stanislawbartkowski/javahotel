/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.crud.controler;

import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.client.mvc.record.model.IRecordDef;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RecordModel {

    private AbstractTo beforea;
    private AbstractTo a;
    private final Object auxData;
    private final Object auxData1;
    private IRecordDef rDef;
    private List<? extends AbstractTo> aList;
    private List<? extends AbstractTo> beforeaList;

    public RecordModel(Object auxData, Object auxData1) {
        this.a = null;
        this.beforea = null;
        this.auxData = auxData;
        this.auxData1 = auxData1;
        aList = null;
        beforeaList = null;
    }

    /**
     * @return the a
     */
    public AbstractTo getA() {
        return a;
    }

    /**
     * @return the auxData
     */
    public Object getAuxData() {
        return auxData;
    }

    /**
     * @return the auxData1
     */
    public Object getAuxData1() {
        return auxData1;
    }

    /**
     * @param a
     *            the a to set
     */
    public void setA(AbstractTo a) {
        this.a = a;
    }

    /**
     * @return the beforea
     */
    public AbstractTo getBeforea() {
        return beforea;
    }

    /**
     * @param beforea
     *            the beforea to set
     */
    public void setBeforea(AbstractTo beforea) {
        this.beforea = beforea;
    }

    /**
     * @return the rDef
     */
    public IRecordDef getRDef() {
        return rDef;
    }

    /**
     * @param rDef
     *            the rDef to set
     */
    public void setRDef(IRecordDef rDef) {
        this.rDef = rDef;
    }

    /**
     * @return the aList
     */
    public List<? extends AbstractTo> getAList() {
        return aList;
    }

    /**
     * @param aList
     *            the aList to set
     */
    public void setAList(List<? extends AbstractTo> aList) {
        this.aList = aList;
    }

    /**
     * @return the beforeaList
     */
    public List<? extends AbstractTo> getBeforeaList() {
        return beforeaList;
    }

    /**
     * @param beforeaList
     *            the beforeaList to set
     */
    public void setBeforeaList(List<? extends AbstractTo> beforeaList) {
        this.beforeaList = beforeaList;
    }

}
