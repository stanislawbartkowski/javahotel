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
package com.gwtmodel.table.daytimeline;

import com.gwtmodel.table.common.MaxI;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PosData {

    private int panelW;
    private int firstD;
    private int lastD;
    private int dSize;

    void setD(int d) {
        firstD = MaxI.max(0, d - panelW / 2);
        lastD = MaxI.min(firstD + panelW - 1, dSize - 1);
        firstD = lastD - panelW + 1;
    }

    void createW(int dSize, int panelW) {
        this.panelW = MaxI.min(panelW, dSize);
        this.dSize = dSize;
    }

    PanelDesc getDayScrollStatus() {
        boolean l = getFirstD() > 0;
        boolean r = getLastD() < (dSize - 1);
        return new PanelDesc(l, r);
    }

    boolean skipPos(MoveSkip move, int no) {
        switch (move) {
        case BEG:
        case LEFT:
            if (getFirstD() == 0) {
                return false;
            }
            if (move == MoveSkip.BEG) {
                firstD = 0;
            } else {
                firstD = MaxI.max(0, getFirstD() - no);
            }
            lastD = getFirstD() + getPanelW() - 1;
            break;

        case RIGHT:
        case END:
            if (getLastD() == dSize - 1) {
                return false;
            }
            if (move == MoveSkip.END) {
                lastD = dSize - 1;
            } else {
                lastD = MaxI.min(dSize - 1, getLastD() + no);
            }
            firstD = getLastD() - getPanelW() + 1;
            break;
        }
        return true;
    }

    /**
     * @return the firstD
     */
    public int getFirstD() {
        return firstD;
    }

    /**
     * @return the lastD
     */
    public int getLastD() {
        return lastD;
    }

    /**
     * @return the panelW
     */
    public int getPanelW() {
        return panelW;
    }
}
