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
package com.javahotel.client.mvc.crudtable.controler;

import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.crud.controler.ITableModelSignalRead;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CrudTableControlerParam {

    private ITableSignalClicked sc;
    private ICrudReadModel iRead;
    private ITableModelSignalRead sRead;
    private ITableCallBackSetField iB;
    private IContrButtonView cView;

    public CrudTableControlerParam() {
        sc = null;
        iRead = null;
        sRead = null;
        iB = null;
        cView = null;
    }

    /**
     * @return the sc
     */
    public ITableSignalClicked getSc() {
        return sc;
    }

    /**
     * @param sc the sc to set
     */
    public void setSc(ITableSignalClicked sc) {
        this.sc = sc;
    }

    /**
     * @return the iRead
     */
    public ICrudReadModel getIRead() {
        return iRead;
    }

    /**
     * @param iRead the iRead to set
     */
    public void setIRead(ICrudReadModel iRead) {
        this.iRead = iRead;
    }

    /**
     * @return the sRead
     */
    public ITableModelSignalRead getSRead() {
        return sRead;
    }

    /**
     * @param sRead the sRead to set
     */
    public void setSRead(ITableModelSignalRead sRead) {
        this.sRead = sRead;
    }

    /**
     * @return the iB
     */
    public ITableCallBackSetField getIB() {
        return iB;
    }

    /**
     * @param iB the iB to set
     */
    public void setIB(ITableCallBackSetField iB) {
        this.iB = iB;
    }

    /**
     * @return the cView
     */
    public IContrButtonView getCView() {
        return cView;
    }

    /**
     * @param cView the cView to set
     */
    public void setCView(IContrButtonView cView) {
        this.cView = cView;
    }
}
