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
package com.javahotel.client.mvc.record.view;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class RecordViewFactory {

    private RecordViewFactory() {
    }

    public static final IRecordView getRecordView(final IResLocator rI,
            final DictData da, final IRecordDef model,
            final IAuxRecordPanel auxV, final IPanel vp) {
        IRecordViewFactory i = rI.getView().getViewFactory(rI);
        return i.getRecordView(rI, da, model, auxV, vp);
    }

    public static final IRecordView getRecordView(final IResLocator rI,
            final DictData da, final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV) {
        IRecordViewFactory i = rI.getView().getViewFactory(rI);
        return i.getRecordView(rI, da, model, contr, co, auxV);
    }

    public static final IRecordView getRecordViewDialog(final IResLocator rI,
            final DictData da, final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV) {

        IRecordViewFactory i = rI.getView().getViewFactory(rI);
        return i.getRecordViewDialog(rI, da, model, contr, co, auxV);

    }

    public static final IRecordView getRecordViewDialog(final IResLocator rI,
            final DictData da, final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV, Widget auxW) {
        IRecordViewFactory i = rI.getView().getViewFactory(rI);
        return i.getRecordViewDialog(rI, da, model, contr, co, auxV, auxW);

    }

    public static final IRecordView getTableViewRecord(final IResLocator rI,
            final IRecordDef model) {
        IRecordViewFactory i = rI.getView().getViewFactory(rI);
        return i.getTableViewRecord(rI, model);
    }
}
