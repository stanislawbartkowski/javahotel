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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.TableFactoriesContainer;

class ListBoxWithHelp extends GetValueLB {

    private final WidgetWithPopUpTemplate wHelp;
    private final ChooseListHelper cHelper;
    private final HorizontalPanel hP = new HorizontalPanel();

    private class RHelp extends ChooseListHelper {

        RHelp(IDataType dType, IVField fie) {
            super(dType, fie);
        }

        @Override
        void asetValue(String sy) {
            setVal(sy);
        }

        @Override
        void hide() {
            wHelp.hide();
        }

    }

    ListBoxWithHelp(TableFactoriesContainer tFactories, IDataType dType,
            IVField fie) {
        super(tFactories);
        cHelper = new RHelp(dType, fie);
        hP.add(lB);
        wHelp = new WidgetWithPopUpTemplate(tFactories, hP, "image", cHelper
                .getI());
    }

    @Override
    public Widget getGWidget() {
        return hP;
    }

}
