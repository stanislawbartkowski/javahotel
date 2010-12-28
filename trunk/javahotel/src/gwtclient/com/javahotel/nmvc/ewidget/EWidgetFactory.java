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
package com.javahotel.nmvc.ewidget;

import com.google.inject.Inject;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;

public class EWidgetFactory {

    private final IResLocator pLoc;
    private final EditWidgetFactory eFactory;

    @Inject
    public EWidgetFactory(IResLocator pLoc, EditWidgetFactory eFactory) {
        this.eFactory = eFactory;
        this.pLoc = pLoc;
    }

    public IFormLineView getListValuesBox(IVField v, final RType r, final CommandParam p,
            final IField f) {
        return eFactory.constructListValuesCombo(v,
                new GetCommandList(pLoc, r, p, f));
    }

    public IFormLineView getListValuesBox(IVField v, final CommandParam p) {
        return getListValuesBox(v, RType.ListDict, p, DictionaryP.F.name);
    }
}
