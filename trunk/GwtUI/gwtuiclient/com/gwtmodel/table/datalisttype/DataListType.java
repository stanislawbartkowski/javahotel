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
package com.gwtmodel.table.datalisttype;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import java.util.List;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
class DataListType implements IDataListType {

    protected final List<IVModelData> dList;
    private final IVField comboFie;
    private final IVField levelFie;
    private final IVField displayFie;

    DataListType(List<IVModelData> dList, IVField comboFie, IVField levelFie, IVField displayFie) {
        this.dList = dList;
        this.comboFie = comboFie;
        this.levelFie = levelFie;
        this.displayFie = displayFie;
    }

    @Override
    public IVField comboField() {
        return comboFie;
    }

    @Override
    public void add(int row, IVModelData vData) {
        dList.add(row, vData);
    }

    @Override
    public void remove(int row) {
        dList.remove(row);
    }

    @Override
    public List<IVModelData> getList() {
        return dList;
    }

    @Override
    public int treeLevel(int row) {
        if (levelFie == null) {
            return -1;
        }
        IVModelData v = dList.get(row);
        return FUtils.getValueInteger(v, levelFie);
    }

    @Override
    public boolean isTreeEnabled() {
        return levelFie != null;
    }

    @Override
    public IVField displayComboField() {
        return displayFie;
    }
}
