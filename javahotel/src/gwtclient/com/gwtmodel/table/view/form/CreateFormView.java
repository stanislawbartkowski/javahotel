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
package com.gwtmodel.table.view.form;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;

class CreateFormView {

    private CreateFormView() {

    }

    static Grid construct(final FormLineContainer model) {

        int rows = model.getfList().size();
        Grid g = new Grid(rows, 2);
        rows = 0;
        for (FormField d : model.getfList()) {
            Label la = new Label(d.getPLabel());
            g.setWidget(rows, 0, la);
            g.setWidget(rows, 1, d.getELine().getGWidget());
            rows++;
        }
        return g;
    }
}
