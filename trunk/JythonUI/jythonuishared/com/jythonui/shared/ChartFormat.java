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
package com.jythonui.shared;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.common.CUtil;

public class ChartFormat extends ElemDescription {

    private static final long serialVersionUID = 1L;
    private List<FieldItem> colList = new ArrayList<FieldItem>();

    public enum ChartType {
        PIE, AREA, BARS, COLUMNS, LINE, SCATTER
    }

    public List<FieldItem> getColList() {
        return colList;
    }

    public int getOptionsWidth() {
        if (!isAttr(ICommonConsts.WIDTH))
            return ICommonConsts.CHART_DEFAULTWIDTH;
        return CUtil.toInteger(getWidth());
    }

    public int getOptionsHeight() {
        if (!isAttr(ICommonConsts.CHARTHEIGHT))
            return ICommonConsts.CHART_DEFAULTHEIGHT;
        return CUtil.toInteger(getAttr(ICommonConsts.CHARTHEIGHT));
    }

    public boolean isPie3D() {
        return !isAttr(ICommonConsts.CHARTPIENOT3D);
    }

    public ChartType getChartType() {
        if (!isAttr(ICommonConsts.CHARTTYPE))
            return ChartType.PIE;
        return ChartType.valueOf(getAttr(ICommonConsts.CHARTTYPE));
    }

}
