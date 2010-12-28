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
package com.javahotel.client.mvc.table.view;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.table.model.ITableModel;

/**
 *
 * @author hotel
 */
public class TableViewFactory {

    private TableViewFactory() {
    }

    public static ITableView getView(final IResLocator rI, final DictData da,
            final IContrButtonView cView, final ITableModel model,
            final ITableSignalClicked sc, final ITableCallBackSetField iCall,
            final IGetWidgetTableView iW) {
        IGetTableViewFactory i = rI.getView().getTableViewFactory(rI);
        return i.getView(da, cView, model, sc, iCall,iW);
    }

    public static ITableView getGridView(final IResLocator rI, final DictData da,
            final IContrButtonView cView, final ITableModel model,
            final ITableSignalClicked sc, final ITableCallBackSetField iCall,
            final IGetWidgetTableView iW) {
        IGetTableViewFactory i = rI.getView().getTableViewFactory(rI);
        return i.getGridView(da, cView, model, sc, iCall, iW);
    }
}
