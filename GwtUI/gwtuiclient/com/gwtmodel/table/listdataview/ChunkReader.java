/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.LogT;

class ChunkReader {

    interface IReadChunk {
        void readChunk(Chunk c);
    }

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

    private final static int SIZE = IConsts.CHUNK_SIZE;

    private final List<Chunk> cList = new ArrayList<Chunk>();

    private final IReadChunk iRead;
    private boolean clearCache = false;

    ChunkReader(IReadChunk iRead) {
        this.iRead = iRead;
    }

    IVModelData get(int i) {
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

    void readChunkRange(int startw, int rangew, IVField fSort, boolean asc,
            ISuccess signal) {
        if (!clearCache)
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

    void clearIfSignalled(Chunk c) {
        if (clearCache) {
            cList.clear();
            cList.add(c);
        }
        clearCache = false;
    }

    void signalClearCache() {
        clearCache = true;
    }

}
