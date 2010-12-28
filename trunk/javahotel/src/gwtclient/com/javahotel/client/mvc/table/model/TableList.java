/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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
package com.javahotel.client.mvc.table.model;


import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author hotel
 */
class TableList implements ITableModel {

    private final IResLocator rI;

    // private List<? extends AbstractTo> oList;
    private List<AbstractTo> aList;
    private final List<ColTitle> cTitle;
    private final AbstractTo.IFieldToS iToS;
    private final String header;
    private final ITableFilter iF;
    private final ITableConverter iConv;

    TableList(final IResLocator rI, final List<ColTitle> cTitle,
            final AbstractTo.IFieldToS iToS, final String header,
            final ITableFilter iF,final ITableConverter iConv) {
        this.rI = rI;
        this.cTitle = cTitle;
        this.iToS = iToS;
        this.header = header;
        this.iF = iF;
        this.iConv = iConv;
    }

    @Override
    public int rowNum() {
        if (aList == null) { return 0; }
        return aList.size();
    }

    @Override
    public String getField(int row, IField f) {
        AbstractTo a = getRow(row);
        String s = a.getDispS(f, iToS);
        return s;
    }

    @Override
    public void setList(List<? extends AbstractTo> tList) {
        //oList = tList;
        aList = (List<AbstractTo>) tList;
        if ((iF != null) || (iConv != null)) {
          aList = new ArrayList<AbstractTo>();
          for (AbstractTo a : tList) {
              AbstractTo aa = a;
              if (iConv != null) {
                  aa = iConv.convertA(a);
              }
              if ((iF == null) || iF.isOk(aa)) {
                  aList.add(aa);
              }
          }
        }
    }

    @Override
    public List<ColTitle> colList() {
        return cTitle;
    }

    @Override
    public AbstractTo getRow(int row) {
        AbstractTo a = aList.get(row);
        return a;

    }

    /**
     * @return the iToS
     */
    public AbstractTo.IFieldToS getIToS() {
        return iToS;
    }

    @Override
    public List<? extends AbstractTo> getList() {
        return aList;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public IField getCol(int col) {
        return cTitle.get(col).getF();
    }

    @Override
    public boolean isTableDefined() {
        return aList != null;
    }

    @Override
    public int colNum() {
        return cTitle.size();
    }
}
