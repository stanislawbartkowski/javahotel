/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.edittable.view;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class EditTableViewFactory {

    private EditTableViewFactory() {
    }

    public static IEditTableView getView(IResLocator rI, DictData da,
            final IContrButtonView cView, ITableModel model,
            IGetWidgetTableView customW) {
        return new EditTableView(rI, da, cView, model, customW);

    }
}
