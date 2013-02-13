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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;

/**
 * 
 * @author perseus
 */
class DataListModelView implements IGwtTableModel {

    interface IReadChunk {
        void readChunk(Chunk c);
    }

    private VListHeaderContainer heList;
    private IDataListType dataList = null;
    private final IListClicked lClicked;
    private boolean unSelectAtOnce = false;
    private IRowEditAction rAction;
    private long size = 0;
    private final IReadChunk iRead;

    class Chunk {

        final int start, len;
        final IVField fSort;
        final boolean asc;
        final ISuccess signal;

        Chunk(int start, int len, IVField fSort, boolean asc, ISuccess signal) {
            this.start = start;
            this.len = len;
            this.fSort = fSort;
            this.asc = asc;
            this.signal = signal;
        }

        List<IVModelData> vList;
    }

    private final static int SIZE = 100;

    private final List<Chunk> cList = new ArrayList<Chunk>();

    DataListModelView(IReadChunk iRead) {
        this.lClicked = null;
        this.iRead = iRead;
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
        for (Chunk c : cList) {
            if (c.vList == null) {
                continue;
            }
            if (c.start <= i && c.start + c.len > i) {
                return c.vList.get(i - c.start);
            }
        }
        Utils.errAlert(LogT.getT().CannotFindChunkForIndex(i));
        return null;
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
        for (Chunk c : cList) {
            if (c.start == startw && c.len == rangew) {
                boolean eqSort = false;
                if (fSort == null && c.fSort == null) {
                    eqSort = true;
                } else if (fSort != null && c.fSort != null)
                    eqSort = fSort.eq(c.fSort);
                if (eqSort)
                    eqSort = c.asc == asc;
                if (eqSort) {

                    cList.remove(c);
                    cList.add(0, c);
                    if (c.vList == null) {
                        return;
                    }
                    signal.success();
                    return;
                }
            }
        } // for
        if (cList.size() >= SIZE) {
            cList.remove(cList.size() - 1);
        }
        Chunk c = new Chunk(startw, rangew, fSort, asc, signal);
        c.vList = null;
        cList.add(0, c);
        iRead.readChunk(c);
    }

}
