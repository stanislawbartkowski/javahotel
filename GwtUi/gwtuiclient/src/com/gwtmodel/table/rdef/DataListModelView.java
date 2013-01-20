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
package com.gwtmodel.table.rdef;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import java.util.List;

/**
 * 
 * @author perseus
 */
public class DataListModelView implements IGwtTableModel {

    private VListHeaderContainer heList;
    private IDataListType dataList;
    private final IListClicked lClicked;
    private boolean unSelectAtOnce = false;
    private IRowEditAction rAction;

    public DataListModelView() {
        this.lClicked = null;
    }

    public DataListModelView(IListClicked lClicked) {
        this.lClicked = lClicked;
    }

    public void setHeaderList(VListHeaderContainer heList) {
        this.heList = heList;
    }

    public VListHeaderContainer getHeaderList() {
        return heList;
    }

    /**
     * @param dataList
     *            the dataList to set
     */
    public void setDataList(IDataListType dataList) {
        this.dataList = dataList;
    }

    public IListClicked getIClicked() {
        return lClicked;
    }

    public boolean containsData() {
        return dataList != null;
    }

    public IVField getComboField() {
        return dataList.comboField();
    }

    public List<IVModelData> getRows() {
        return dataList.getList();
    }

    public boolean unSelectAtOnce() {
        return unSelectAtOnce;
    }

    public void setUnSelectAtOnce(boolean unSelectAtOnce) {
        this.unSelectAtOnce = unSelectAtOnce;
    }

    @Override
    public int treeLevel(int row) {
        return dataList.treeLevel(row);
    }

    /**
     * @param rAction
     *            the rAction to set
     */
    public void setrAction(IRowEditAction rAction) {
        this.rAction = rAction;
    }

    @Override
    public IRowEditAction getRowEditAction() {
        return rAction;
    }

}
