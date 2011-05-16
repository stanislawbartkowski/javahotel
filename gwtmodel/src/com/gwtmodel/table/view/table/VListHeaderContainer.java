/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.IConsts;
import java.util.ArrayList;
import java.util.List;

public class VListHeaderContainer {

    private final List<VListHeaderDesc> heList;
    private final String listTitle;
    private final int pageSize;
    private final String jsModifRow;

    public VListHeaderContainer(List<VListHeaderDesc> heList, String listTitle,
            int pageSize, String jsModifRow) {
        this.heList = heList;
        this.listTitle = listTitle;
        this.pageSize = pageSize <= 0 ? IConsts.defaultPage : pageSize;
        this.jsModifRow = jsModifRow;
    }

    public VListHeaderContainer(List<VListHeaderDesc> heList, String listTitle) {
        this(heList, listTitle, IConsts.defaultPage, null);
    }

    public List<VListHeaderDesc> getVisHeList() {
        List<VListHeaderDesc> h = new ArrayList<VListHeaderDesc>();
        for (VListHeaderDesc v : heList) {
            if (v.isHidden()) {
                continue;
            }
            h.add(v);
        }
        return h;
    }

    public List<VListHeaderDesc> getAllHeList() {
        return heList;
    }

    public String getListTitle() {
        return listTitle;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return the jsModifRow
     */
    public String getJsModifRow() {
        return jsModifRow;
    }
}
