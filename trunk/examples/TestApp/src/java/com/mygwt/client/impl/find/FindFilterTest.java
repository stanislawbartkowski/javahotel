/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
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
import com.gwtmodel.table.listdataview.SetSortColumnSignal;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
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

    private final String SETSORT = "BUTTON_SET FILTER";

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

    private class SetSort implements ISlotListener {

        private final ISlotable iSlo;

        SetSort(ISlotable iSlo) {
            this.iSlo = iSlo;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVField v = ItemVData.fNAME;
            CustomStringSlot sl = SetSortColumnSignal
                    .constructSlotSetSortColumnSignal(dType);
            SetSortColumnSignal sig = new SetSortColumnSignal(v, true);
            iSlo.getSlContainer().publish(sl, sig);
        }

    }

    @Override
    public Widget getW() {
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        CellId panelId = new CellId(0);
        ControlButtonFactory bFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();
        List<ControlButtonDesc> cList = bFactory.constructCrudListButtons();
        ControlButtonDesc bChange = new ControlButtonDesc("Set sort", SETSORT);
        cList.add(bChange);
        ListOfControlDesc cButton = new ListOfControlDesc(cList);
        DisplayListControlerParam dList = tFactory.constructParam(cButton,
                panelId, getParam(), null, false);

        //
        // DisplayListControlerParam dList = tFactory.constructParam(panelId,
        // getParam(), null, null,tree);
        ISlotable i = tFactory.constructDataControler(dList);
        SlU.registerWidgetListener0(dType, i, new GetWidget());
        i.getSlContainer().registerSubscriber(dType,
                new ClickButtonType(SETSORT), new SetSort(i));

        i.startPublish(null);
        VModelData footerV = new VModelData();
        footerV.setF(ItemVData.fNAME, "Footer");
        SlU.drawFooter(dType, i, footerV);
        return vp;

    }

}
