/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.util;

/**
 * 
 * @author perseus
 */
public class SolidPos {

    private final int startl;
    private final int startcol;
    private final int maxstartl;
    private final int maxstartcol;

    public SolidPos(int startl, int startcol, int maxstartl, int maxstartcol) {
        this.startl = startl;
        this.startcol = startcol;
        this.maxstartl = maxstartl;
        this.maxstartcol = maxstartcol;

    }

    public SolidPos() {
        this(-1, -1, -1, -1);
    }

    /**
     * @return the startl
     */
    int getStartl() {
        return startl;
    }

    /**
     * @return the startcol
     */
    int getStartcol() {
        return startcol;
    }

    public int getMaxstartl() {
        return maxstartl;
    }

    public int getMaxstartcol() {
        return maxstartcol;
    }

}
