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
package com.javahotel.client.mvc.dictcrud.controler;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudChooseTable;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.table.model.ITableConverter;
import com.javahotel.client.mvc.table.model.ITableFilter;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class RecordAuxParam {

    private IAuxRecordPanel auxV;
    private Object auxO;
    private Object auxO1;
    private Widget auxW;
    private IAuxInfoPanel infoP;
    private IModifRecordDef modifD;
    private ICrudPersistSignal pSignal;
    private boolean modifPanel;
    private ITableSignalClicked sClicked;
    private IBeforeViewSignal bSignal;
    private ICrudChooseTable iChoose;
    private ITableFilter iFilter;
    private IContrPanel cPanel;
    private ITableConverter iConv;
    private IControlClick iClick;
    private ICustomConnector iCon;

    public ICustomConnector getiCon() {
        return iCon;
    }

    public void setiCon(ICustomConnector iCon) {
        this.iCon = iCon;
    }

    public RecordAuxParam() {
        auxV = null;
        auxO = null;
        auxO1 = null;

        auxW = null;
        infoP = null;

        modifD = null;
        pSignal = null;

        modifPanel = true;
        sClicked = null;
        bSignal = null;
        iChoose = null;
        iFilter = null;
        iConv = null;
        iClick = null;
    }

    public IControlClick getIClick() {
		return iClick;
	}

	public void setIClick(IControlClick click) {
		iClick = click;
	}

	/**
     * @param auxV the auxV to set
     */
    public void setAuxV(IAuxRecordPanel auxV) {
        this.auxV = auxV;
    }

    /**
     * @param auxO the auxO to set
     */
    public void setAuxO(Object auxO) {
        this.auxO = auxO;
    }

    /**
     * @param auxO1 the auxO1 to set
     */
    public void setAuxO1(Object auxO1) {
        this.auxO1 = auxO1;
    }

    /**
     * @param auxW the auxW to set
     */
    public void setAuxW(Widget auxW) {
        this.auxW = auxW;
    }

    /**
     * @param infoP the infoP to set
     */
    public void setInfoP(IAuxInfoPanel infoP) {
        this.infoP = infoP;
    }

    /**
     * @param modifD the modifD to set
     */
    public void setModifD(IModifRecordDef modifD) {
        this.modifD = modifD;
    }

    /**
     * @param pSignal the pSignal to set
     */
    public void setPSignal(ICrudPersistSignal pSignal) {
        this.pSignal = pSignal;
    }

    /**
     * @param modifPanel the modifPanel to set
     */
    public void setModifPanel(boolean modifPanel) {
        this.modifPanel = modifPanel;
    }

    /**
     * @return the auxV
     */
    public IAuxRecordPanel getAuxV() {
        return auxV;
    }

    /**
     * @return the auxO
     */
    public Object getAuxO() {
        return auxO;
    }

    /**
     * @return the auxO1
     */
    public Object getAuxO1() {
        return auxO1;
    }

    /**
     * @return the auxW
     */
    public Widget getAuxW() {
        return auxW;
    }

    /**
     * @return the infoP
     */
    public IAuxInfoPanel getInfoP() {
        return infoP;
    }

    /**
     * @return the modifD
     */
    public IModifRecordDef getModifD() {
        return modifD;
    }

    /**
     * @return the pSignal
     */
    public ICrudPersistSignal getPSignal() {
        return pSignal;
    }

    /**
     * @return the modifPanel
     */
    public boolean isModifPanel() {
        return modifPanel;
    }

    /**
     * @return the sClicked
     */
    public ITableSignalClicked getSClicked() {
        return sClicked;
    }

    /**
     * @param sClicked the sClicked to set
     */
    public void setSClicked(ITableSignalClicked sClicked) {
        this.sClicked = sClicked;
    }

    /**
     * @return the bSignal
     */
    public IBeforeViewSignal getBSignal() {
        return bSignal;
    }

    /**
     * @param bSignal the bSignal to set
     */
    public void setBSignal(IBeforeViewSignal bSignal) {
        this.bSignal = bSignal;
    }

    /**
     * @return the iChoose
     */
    public ICrudChooseTable getIChoose() {
        return iChoose;
    }

    /**
     * @param iChoose the iChoose to set
     */
    public void setIChoose(ICrudChooseTable iChoose) {
        this.iChoose = iChoose;
    }

    /**
     * @return the iFilter
     */
    public ITableFilter getIFilter() {
        return iFilter;
    }

    /**
     * @param iFilter the iFilter to set
     */
    public void setIFilter(ITableFilter iFilter) {
        this.iFilter = iFilter;
    }

    /**
     * @return the cPanel
     */
    public IContrPanel getCPanel() {
        return cPanel;
    }

    /**
     * @param cPanel the cPanel to set
     */
    public void setCPanel(IContrPanel cPanel) {
        this.cPanel = cPanel;
    }

    /**
     * @return the iConv
     */
    public ITableConverter getIConv() {
        return iConv;
    }

    /**
     * @param iConv the iConv to set
     */
    public void setIConv(ITableConverter iConv) {
        this.iConv = iConv;
    }
}
