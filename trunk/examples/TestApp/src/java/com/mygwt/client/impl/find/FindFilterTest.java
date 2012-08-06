/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.mygwt.client.impl.find;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.AbstractDataModel;
import com.mygwt.client.testEntryPoint.IGetWidget;
import com.mygwt.common.data.TOItemRecord;

/**
 * @author hotel
 * 
 */
public class FindFilterTest implements IGetWidget {

    /** IDataType used all the time. */
    private final IDataType dType = Empty.getDataType();

    private final VerticalPanel vp = new VerticalPanel();
    
    private final boolean tree;
    
    public FindFilterTest(boolean tree) {
        this.tree = tree;
    }

    /** Listener for setting widget. */
    private class GetWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            // clear : can be launched twice and more
            vp.clear();
            vp.add(w);
        }

    }

    private class DataModel extends AbstractDataModel {

        @Override
        public IVModelData construct(IDataType dType) {
            return new ItemVData(new TOItemRecord());
        }
    }

    
    private DataListParam getParam() {

        // create factories
        IDataPersistListAction iPersist = new ItemListPersistAction(dType);
        IHeaderListContainer heList = new HeaderList(dType);
        IDataModelFactory dataFactory = new DataModel();
        IFormTitleFactory formFactory = new IFormTitleFactory() {

            @Override
            public String getFormTitle(ICallContext iContext) {
                return "Item data";
            }

        };

        IGetViewControllerFactory fControler = new GetViewController(
                dataFactory);

        return new DataListParam(dType, iPersist, heList, dataFactory,
                formFactory, fControler, null);
    }

    @Override
    public Widget getW() {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        CellId panelId = new CellId(0);
        DisplayListControlerParam dList = tFactory.constructParam(panelId,
                getParam(), null, null,tree);
        ISlotable i = tFactory.constructDataControler(dList);
        SlU.registerWidgetListener0(dType, i, new GetWidget());
        i.startPublish(null);
        return vp;

    }

}
