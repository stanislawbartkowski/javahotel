/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwtmodel.table.rdef;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.VListHeaderContainer;

/**
 *
 * @author perseus
 */
public class DataListModelView implements IGwtTableModel {

    private VListHeaderContainer heList;
    private IDataListType dataList;
    private final IListClicked lClicked;

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

    public IVModelData getRow(int row) {
        return dataList.getRow(row);
    }

    public int getRowsNum() {
        return dataList.rowNo();
    }

    /**
     * @param dataList the dataList to set
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
}
