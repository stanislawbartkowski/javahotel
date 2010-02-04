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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.persist.IMemoryListModel;
import com.gwtmodel.table.persist.IVModelDataEquable;
import com.gwtmodel.table.persist.MemoryListPersist;
import com.gwtmodel.table.persist.StringV;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.ValidateUtil;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.types.LId;

public class CustomerAddInfo extends AbstractSlotContainer implements ISlotable {

    // private final IDataType dType;
    private final IMemoryListModel lPhonelist;
    private final IMemoryListModel lPhonedata;
    
    private final IDataControler dControler;
    private final VerticalPanel vPanel = new VerticalPanel();
    private final DataViewModelFactory daFactory;

    private class ValidateS extends AbstractSlotContainer implements
            IDataValidateAction {

        private final IDataType stringType;

        private class ValidateA implements ISlotSignaller {

            public void signal(ISlotSignalContext slContext) {
                IVModelData pData = getGetterIVModelData(
                        GetActionEnum.GetViewComposeModelEdited, stringType);
                List<IVField> listMFie = new ArrayList<IVField>();
                listMFie.add(new StringF());
                List<InvalidateMess> errMess = ValidateUtil.checkEmpty(pData,
                        listMFie);
                if (errMess != null) {
                    publish(DataActionEnum.InvalidSignal, stringType,
                            new InvalidateFormContainer(errMess));
                    return;
                }
                publish(DataActionEnum.ValidSignal, stringType);
            }
        }

        ValidateS(IDataType stringType) {
            this.stringType = stringType;
            registerSubscriber(DataActionEnum.ValidateAction, stringType,
                    new ValidateA());

        }

        public void startPublish(int cellId) {
        }

    }

    private class SetGetter implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return null;
        }
    }

    public class StringE implements IVModelDataEquable {

        private final LId id;
        private String s;

        StringE() {
            id = new LId();
        }

        StringE(LId id) {
            this.id = id;
        }

        public boolean eq(IVModelDataEquable o) {
            StringE e = (StringE) o;
            return id.equals(e.id);
        }

        public String getS(IVField fie) {
            return s;
        }

        public boolean isEmpty(IVField fie) {
            return CUtil.EmptyS(s);
        }

        public void setS(IVField fie, String s) {
            this.s = s;
        }

    }

    private class DrawModel implements ISlotSignaller {

        private final IDataType sType;

        DrawModel(IDataType sType) {
            this.sType = sType;
        }

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;
            CustomerP cust = (CustomerP) vData.getA();

            List eList = new ArrayList<StringE>();
            IDataListType dList = DataListTypeFactory.construct(eList);
            lPhonelist.setDataList(dList);
            lPhonedata.setDataList(dList);
            dControler.startPublish(0);
            List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
            VListHeaderDesc he = new VListHeaderDesc("Telefon", new StringF());
            heList.add(he);
            VListHeaderContainer vHeader;
            vHeader = new VListHeaderContainer(heList, "Telefony");
            dControler.getSlContainer().publish(sType, vHeader);

        }
    }

    private class SetGwt implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            vPanel.add(w);
        }
    }

    private class DataFactory implements IDataModelFactory {

        public IVModelData construct(IDataType dType) {
            return new StringE();
        }

        public void copyFromPersistToModel(IDataType dType, IVModelData from,
                IVModelData to) {
            StringE efrom = (StringE) from;
            StringE eto = (StringE) to;
            eto.s = efrom.s;
        }

        private IFormLineView getI(FormLineContainer fContainer) {
            FormField i = fContainer.getfList().get(0);
            return i.getELine();
        }

        public void fromDataToView(IVModelData aFrom,
                FormLineContainer fContainer) {
            StringE efrom = (StringE) aFrom;
            IFormLineView e = getI(fContainer);
            e.setVal(efrom.s);
        }

        public void fromModelToPersist(IDataType dType, IVModelData from,
                IVModelData to) {
            copyFromPersistToModel(dType, from, to);
        }

        public void fromViewToData(FormLineContainer fContainer, IVModelData aTo) {
            StringE eto = (StringE) aTo;
            IFormLineView e = getI(fContainer);
            eto.s = e.getVal();
        }

    }

    private class StringF implements IVField {

        public boolean eq(IVField o) {
            return true;
        }

    }

    private class StringFactory implements IFormDefFactory {

        public FormLineContainer construct(IDataType dType) {
            EditWidgetFactory eFactory = GwtGiniInjector.getI()
                    .getEditWidgetFactory();
            List<FormField> di = new ArrayList<FormField>();
            IFormLineView textLine = eFactory.constructTextField();
            di.add(new FormField("Text", textLine, new StringF()));
            return new FormLineContainer(di);
        }

        public String getFormTitle(IDataType dType) {
            return "Telefony";
        }

    }

    private class GetControler implements IGetViewControllerFactory {

        public IComposeController construct(IDataType dType) {
            ComposeControllerFactory coFactory = GwtGiniInjector.getI()
                    .getComposeControllerFactory();
            FormLineContainer fContainer = new StringFactory().construct(dType);
            IDataModelFactory dFactory = new DataFactory();
            IComposeController iCon = coFactory.construct(dType, dFactory);
            IDataViewModel daModel = daFactory.construct(dType, fContainer,
                    dFactory);
            ComposeControllerType cType = new ComposeControllerType(daModel,
                    dType, 0, 0);
            iCon.registerController(cType);
            iCon.registerController(new ComposeControllerType(lPhonedata, dType));
            iCon.registerController(new ComposeControllerType(new ValidateS(
                    dType), dType));
            return iCon;
        }

    }

    public CustomerAddInfo(IDataType dType) {
        // this.dType = dType;

        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        IDataType sType = new StringV();
        lPhonelist = new MemoryListPersist(sType);
        lPhonedata = new MemoryListPersist(sType);

        dControler = tFactory.constructDataControler(sType, 0, 1,
                new DataListParam(lPhonelist, null, new DataFactory(),
                        new StringFactory(), new GetControler()));

        registerCaller(GetActionEnum.GetViewModelEdited, dType, new SetGetter());
        registerCaller(GetActionEnum.GetModelToPersist, dType, new SetGetter());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel(sType));
        dControler.getSlContainer().registerSubscriber(0, new SetGwt());
        // dControler.startPublish(0);
    }

    public void startPublish(int cellId) {
        publish(cellId, new GWidget(vPanel));
    }

}
