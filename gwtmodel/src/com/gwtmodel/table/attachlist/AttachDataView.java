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
package com.gwtmodel.table.attachlist;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.controler.DataListParam;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.IDataControler;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.util.AbstractDataModel;

/**
 * 
 * @author perseus
 */
class AttachDataView extends AbstractSlotContainer implements IAttachDataView {

    private final IDataControler dControler;
    private final ControlButtonFactory cButtonFactory;
    private static final String DOWNLOAD = "DOWNLOAD-ACTION";

    AttachDataView(IDataType dType, ISlotListener setGwt,
            IHeaderListContainer iHeader,
            IComposeControllerTypeFactory compFactory) {
        this.dType = dType;
        cButtonFactory = GwtGiniInjector.getI().getControlButtonFactory();
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        IDataModelFactory dFactory = new AbstractDataModel() {

            @Override
            public IVModelData construct(IDataType dType) {
                return new AttachData();
            }
        };

        IFormTitleFactory iTitle = new IFormTitleFactory() {

            @Override
            public String getFormTitle(ICallContext iContext) {
                return MM.getL().Attachment();
            }
        };

        DataListParam dParam = new DataListParam(dType, null, iHeader,
                dFactory, iTitle, new AttachDataGetViewControler(dFactory,
                        compFactory));

        ListOfControlDesc cList = cButtonFactory.constructCrudList();
        ControlButtonDesc b = new ControlButtonDesc(MM.getL().Download(), MM
                .getL().DownloadAttachment(), new ClickButtonType(DOWNLOAD));
        cList.getcList().add(b);

        DisplayListControlerParam cParam = tFactory.constructParam(cList,
                new CellId(0), dParam, null);
        dControler = tFactory.constructDataControler(cParam);
        dControler.getSlContainer().registerSubscriber(dType, 0, setGwt);
        ISlotable iSlo = new DownloadFile(dType, new ClickButtonType(DOWNLOAD));
        this.setSlContainer(dControler);
        iSlo.setSlContainer(dControler);
    }

    @Override
    public void startPublish(CellId cellId) {
        dControler.startPublish(null);
    }
}
