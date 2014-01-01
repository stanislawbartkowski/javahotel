/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import java.util.List;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;

public class ReadChunkSignal implements ICustomObject {

    private final int start;
    private final int size;
    private final ISuccess signal;
    private final IVField fSort;
    private final boolean asc;
    private List<IVModelData> vList;
    private final IOkModelData okData;

    ReadChunkSignal(int start, int size, IVField fSort, boolean asc,
            ISuccess signal, IOkModelData okData) {
        this.start = start;
        this.size = size;
        this.signal = signal;
        this.fSort = fSort;
        this.asc = asc;
        this.okData = okData;
    }

    public int getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    private static final String SIGNAL_ID = ReadChunkSignal.class.getName()
            + "TABLE_PUBLIC_READ_CHUNK_ROWS";

    public static CustomStringSlot constructReadChunkSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, SIGNAL_ID);
    }

    public void signalRowsRead(List<IVModelData> vList) {
        this.vList = vList;
        this.signal.success();
    }

    List<IVModelData> getvList() {
        return vList;
    }

    public IVField getfSort() {
        return fSort;
    }

    public boolean isAsc() {
        return asc;
    }

    public IOkModelData getOkData() {
        return okData;
    }

}
