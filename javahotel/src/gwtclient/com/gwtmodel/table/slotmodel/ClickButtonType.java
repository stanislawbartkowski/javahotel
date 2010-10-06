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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.common.CUtil;

public class ClickButtonType {

    public enum StandClickEnum {

        ADDITEM, REMOVEITEM, MODIFITEM, SHOWITEM, ACCEPT, RESIGN, CHOOSELIST, RESIGNLIST,
        CUSTOM, ALL
    };
    private StandClickEnum clickEnum;
    private String customButt;

    public ClickButtonType(StandClickEnum clickEnum) {
        assert clickEnum != StandClickEnum.CUSTOM : "Must be connected to custom string";
        this.clickEnum = clickEnum;
        customButt = null;
    }

    public ClickButtonType(String customButt) {
        this.clickEnum = StandClickEnum.CUSTOM;
        this.customButt = customButt;
    }

    public StandClickEnum getClickEnum() {
        return clickEnum;
    }

    public String getCustomButt() {
        return customButt;
    }

    public boolean eq(ClickButtonType bType) {
        if (clickEnum == StandClickEnum.ALL) {
            return true;
        }
        if (bType.clickEnum == StandClickEnum.ALL) {
            return true;
        }
        if (clickEnum != bType.clickEnum) {
            return false;
        }
        if (clickEnum != StandClickEnum.CUSTOM) {
            return true;
        }
        return CUtil.EqNS(customButt, bType.customButt);
    }
}
