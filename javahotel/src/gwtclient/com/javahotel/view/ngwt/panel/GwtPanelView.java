/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.ngwt.panel;

import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.common.util.MaxI;

class GwtPanelView implements IGwtPanelView  {
    
    private final Grid g;
    
    GwtPanelView(List<PanelRowDesc> rowDesc) {
        int colNo = 0;
        for (PanelRowDesc ro : rowDesc) {
            colNo = MaxI.max(colNo,ro.getNoCells());                    
        }
        g = new Grid(rowDesc.size(),colNo);
        
    }

    public void setWidget(int row, int column, IGwtWidget gwtWidget) {
        g.setWidget(row, column, gwtWidget.getMWidget().getWidget());        
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(g);
    }

}
