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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;

/**
 * 
 * @author perseus
 */
class DataListModelView implements IGwtTableModel {

    private VListHeaderContainer heList;
    private IDataListType dataList = null;
    private final IListClicked lClicked;
    private boolean unSelectAtOnce = false;
    private IRowEditAction rAction;
    private long size = 0;
    private final ChunkReader cRead;

    DataListModelView(ChunkReader cRead) {
        this.lClicked = null;
        this.cRead = cRead;
    }

    void setSize(long size) {
        this.size = size;
    }

    void setHeaderList(VListHeaderContainer heList) {
        this.heList = heList;
    }

    @Override
    public VListHeaderContainer getHeaderList() {
        return heList;
    }

    /**
     * @param dataList
     *            the dataList to set
     */
    void setDataList(IDataListType dataList) {
        this.dataList = dataList;
        this.size = dataList.getList().size();
    }

    @Override
    public IListClicked getIClicked() {
        return lClicked;
    }

    @Override
    public boolean containsData() {
        if (dataList != null) {
            return true;
        }
        return size > 0;
    }

    IVField getComboField() {
        return dataList.comboField();
    }

    @Override
    public IVModelData get(int i) {
        if (dataList != null) {
            return dataList.getList().get(i);
        }
        return cRead.get(i);
    }

    @Override
    public boolean unSelectAtOnce() {
        return unSelectAtOnce;
    }

    void setUnSelectAtOnce(boolean unSelectAtOnce) {
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
    void setrAction(IRowEditAction rAction) {
        this.rAction = rAction;
    }

    @Override
    public IRowEditAction getRowEditAction() {
        return rAction;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void readChunkRange(int startw, int rangew, IVField fSort,
            boolean asc, ISuccess signal) {
        cRead.readChunkRange(startw, rangew, fSort, asc, signal);
    }

}
