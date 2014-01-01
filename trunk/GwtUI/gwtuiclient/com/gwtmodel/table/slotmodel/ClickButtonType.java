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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.LogT;

public class ClickButtonType {

    /**
     * @return the htmlElementName
     */
    public String getHtmlElementName() {
        return htmlElementName;
    }

    public enum StandClickEnum {

        ADDITEM, REMOVEITEM, MODIFITEM, SHOWITEM, ACCEPT, RESIGN, CHOOSELIST, RESIGNLIST, CUSTOM, FILTRLIST, SETFILTER, REMOVEFILTER, FIND, FINDNOW, FINDNEXT, FINDFROMBEGINNING, CLEARFIND, CLEARFILTER, TABLEDEFAULTMENU, MENUTITLE, ALL
    };

    private final StandClickEnum clickEnum;
    private final String customButt;
    private final String htmlElementName;

    public ClickButtonType(StandClickEnum clickEnum, String customButt) {
        assert clickEnum != StandClickEnum.CUSTOM : LogT.getT()
                .mustBeConnectedToString();
        this.clickEnum = clickEnum;
        this.customButt = customButt;
        this.htmlElementName = clickEnum.name();
    }

    public ClickButtonType(StandClickEnum clickEnum) {
        this(clickEnum, null);
    }

    public ClickButtonType(String customButt) {
        this(customButt, null);
    }

    public ClickButtonType(String customButt, String htmlElementName) {
        assert customButt != null : LogT.getT().cannotBeNull();
        this.clickEnum = StandClickEnum.CUSTOM;
        this.customButt = customButt;
        if (htmlElementName == null) {
            this.htmlElementName = customButt;
        } else {
            this.htmlElementName = htmlElementName;
        }
    }

    public StandClickEnum getClickEnum() {
        return clickEnum;
    }

    public String getCustomButt() {
        return customButt;
    }

    public boolean isMenuTitle() {
        return clickEnum == clickEnum.MENUTITLE;
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

    // rewrite hashCode and equals to use it as a key for Map
    @Override
    public int hashCode() {
        String b = clickEnum.toString() + customButt;
        int h = b.hashCode();
        return h;
    }

    @Override
    public boolean equals(Object o) {
        ClickButtonType b = (ClickButtonType) o;
        return eq(b);
    }
}
