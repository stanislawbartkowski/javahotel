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
package com.gwtmodel.table;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class WChoosedLine implements ICustomObject {

    private final int choosedLine;
    private final WSize wSize;
    private final IVField vField;

    public WChoosedLine(int choosedLine, WSize wSize, IVField vField) {
        this.choosedLine = choosedLine;
        this.wSize = wSize;
        this.vField = vField;
    }

    public WChoosedLine(int choosedLine, WSize wSize) {
        this(choosedLine, wSize, null);
    }

    public WChoosedLine() {
        this(-1, null, null);
    }

    public int getChoosedLine() {
        return choosedLine;
    }

    public boolean isChoosed() {
        return choosedLine != -1;
    }

    public WSize getwSize() {
        return wSize;
    }

    /**
     * @return the vField
     */
    public IVField getvField() {
        return vField;
    }
}
