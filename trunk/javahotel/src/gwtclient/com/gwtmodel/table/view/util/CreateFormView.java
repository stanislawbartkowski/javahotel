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
package com.gwtmodel.table.view.util;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.rdef.FormField;
import java.util.NoSuchElementException;

public class CreateFormView {

    private CreateFormView() {
    }

    public static HTMLPanel setHtml(String html, List<FormField> fList) {
        HTMLPanel pa = new HTMLPanel(html);
        for (FormField d : fList) {
            String htmlId = d.getHtmlId();
            if (CUtil.EmptyS(htmlId)) {
                continue;
            }
            try {
                Widget w = d.getELine().getGWidget();
                w.getElement().setId(htmlId);
                pa.addAndReplaceElement(w, htmlId);
            } catch (NoSuchElementException e) {
                // expected
            }
        }
        return pa;
    }

    public static Grid construct(final List<FormField> fList, Set<IVField> add) {

        int rows = 0;
        for (FormField d : fList) {
            IVField v = d.getFie();
            if (add != null) {
                if (!add.contains(v)) {
                    continue;
                }
            }
            rows++;
        }
        Grid g = new Grid(rows, 2);
        rows = 0;
        for (FormField d : fList) {
            if (d.isRange()) {
                continue;
            }
            IVField v = d.getFie();
            if (add != null) {
                if (!add.contains(v)) {
                    continue;
                }
            }
            FormField fRange = null;
            for (FormField fo : fList) {
                if (!fo.isRange()) {
                    continue;
                }
                if (fo.getFRange().eq(d.getFie())) {
                    fRange = fo;
                    break;
                }
            }
            Label la = new Label(d.getPLabel());
            Widget w1;
            Widget w2;
            if (fRange == null) {
                w1 = la;
                w2 = d.getELine().getGWidget();
            } else {
                HorizontalPanel vp1 = new HorizontalPanel();
                HorizontalPanel vp2 = new HorizontalPanel();
                vp1.add(la);
                vp2.add(d.getELine().getGWidget());
                w1 = vp1;
//                Label la2 = new Label(fRange.getPLabel());
                Label la2 = new Label(" - ");
                vp2.add(la2);
                vp2.add(fRange.getELine().getGWidget());
                w2 = vp2;
            }
            g.setWidget(rows, 0, w1);
            g.setWidget(rows, 1, w2);
            rows++;
        }
        return g;
    }

    public static Grid construct(final List<FormField> fList) {
        return construct(fList, null);
    }
}
