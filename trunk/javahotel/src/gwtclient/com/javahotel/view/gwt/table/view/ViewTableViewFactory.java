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
package com.javahotel.view.gwt.table.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.injector.WebPanelHolder;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.IGetTableViewFactory;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.table.view.ITableView;

/**
 *
 * @author hotel
 */
public class ViewTableViewFactory implements IGetTableViewFactory {
    
    private final IResLocator rI;
    
    @Inject
    public ViewTableViewFactory(IResLocator rI) {
        this.rI = rI;
    }


    private class GetLabelWidget implements IGetWidgetTableView {

        public Widget getWidget(ITableView mo, int row, int col, String val) {
            Label l = new Label(val);
            return l;
        }
    }
    
    private IGetWidgetTableView getI(final IGetWidgetTableView iW) {
    	if (iW == null) { return new GetLabelWidget(); }
    	return iW;
    }

    public ITableView getView(final DictData da,
            final IContrButtonView cView, final ITableModel model,
            final ITableSignalClicked sc, final ITableCallBackSetField iCall,
            final IGetWidgetTableView iW) {
    	if (WebPanelHolder.isGoogletable()) {
          return new GTableView(rI, cView, model, sc, iCall, getI(iW));
    	}
    	return getGridView(da,cView,model,sc,iCall,iW);
    }
        

    public ITableView getGridView(final DictData da,
            final IContrButtonView cView, final ITableModel model,
            final ITableSignalClicked sc, final ITableCallBackSetField iCall,
            final IGetWidgetTableView iW) {
        return new TableView(rI, cView, model, sc, iCall, getI(iW));
    }
}
