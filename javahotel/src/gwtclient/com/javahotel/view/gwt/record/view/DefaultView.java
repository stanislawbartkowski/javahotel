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
package com.javahotel.view.gwt.record.view;

import java.util.Set;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.common.toobject.IField;

class DefaultView {

    private DefaultView() {

    }

    static void createWDialog(final VerticalPanel iPanel, IRecordDef model,
            final Set<IField> filter, ELineStore eStore) {

        int rows = 0;
        for (RecordField d : model.getFields()) {
            if (filter != null) {
                if (!filter.contains(d.getFie())) {
                    continue;
                }
            }
            rows++;
        }
        Grid g = new Grid(rows, 2);
        rows = 0;
        for (RecordField d : model.getFields()) {
            if (filter != null) {
                if (!filter.contains(d.getFie())) {
                    continue;
                }
            }
            Label la = new Label(d.getPLabel());
            g.setWidget(rows, 0, la);
            g.setWidget(rows, 1, d.getELine().getMWidget().getWidget());
            if (eStore != null) {
              d.getELine().setKLi(new DefaultListener(eStore));
            }
            rows++;
        }
        iPanel.add(g);

    }

}
