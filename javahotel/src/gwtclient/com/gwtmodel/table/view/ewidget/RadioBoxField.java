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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class RadioBoxField extends AbstractField {

    private final List<ComboVal> wy;
    private final VerticalPanel vP;
    private final List<RadioButton> ra;

    RadioBoxField(TableFactoriesContainer tFactories,String zName, List<ComboVal> wy) {
        super(tFactories);
        this.wy = wy;
        vP = new VerticalPanel();
        ra = new ArrayList<RadioButton>();
        for (ComboVal w : wy ) {
            RadioButton r = new RadioButton(zName,w.getDispVal());
            vP.add(r);
            ra.add(r);
        }
        initWidget(vP);
    }


    public void setVal(String v) {
        int no = 0;
        boolean found = false;
        for (ComboVal w : wy) {
            if (w.eqS(v)) {
                found = true;
                break;
            }
            no++;
        }
        if (!found) { return; }
        RadioButton r = ra.get(no);
        r.setValue(true);
    }

    public String getVal() {
        int no = 0;
        for (RadioButton r : ra) {
            if (r.getValue()) {
                ComboVal w = wy.get(no);
                return w.getVal();
            }
            no++;
        }
        return "";
    }

    public void setReadOnly(boolean readOnly) {
        for (RadioButton r : ra) {
            r.setEnabled(!readOnly);
        }
    }

}
