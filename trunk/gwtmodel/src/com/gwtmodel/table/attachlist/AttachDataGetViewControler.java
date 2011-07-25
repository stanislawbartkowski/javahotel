/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.composecontroller.ComposeControllerFactory;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.composecontroller.IComposeControllerTypeFactory;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataFormConstructor;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory.CType;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.gwtmodel.table.view.util.CreateFormView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
class AttachDataGetViewControler implements IGetViewControllerFactory {

    private final ComposeControllerFactory fFactory;
    private final AttachViewFactory aFactory;
    private final DataViewModelFactory daFactory;
    private final IDataModelFactory dFactory;
    private final IComposeControllerTypeFactory compFactory;

    AttachDataGetViewControler(IDataModelFactory dFactory, IComposeControllerTypeFactory compFactory) {
        fFactory = GwtGiniInjector.getI().getComposeControllerFactory();
        this.aFactory = new AttachViewFactory();
        daFactory = GwtGiniInjector.getI().getDataViewModelFactory();
        this.dFactory = dFactory;
        this.compFactory = compFactory;
    }

    private class ReturnU {

        UploadFile u;
        FormLineContainer f;
    }

    private ReturnU getUpLoad(VListHeaderContainer listHeader) {
        ReturnU u = new ReturnU();
        EditWidgetFactory eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IFormLineView dComment = eFactory.constructTextField(AttachDataField.vcomment);
        IFormLineView dfilename = eFactory.constructEditFileName(AttachDataField.vfilename);
        u.u = new UploadFile(dComment.getGWidget(), dfilename.getGWidget());
        VListHeaderDesc hCom = listHeader.getHeader(AttachDataField.vcomment);
        VListHeaderDesc hFile = listHeader.getHeader(AttachDataField.vfilename);
        u.u.labelcomment.setText(hCom.getHeaderString());
        u.u.labelfile.setText(hFile.getHeaderString());
        di.add(new FormField("", dComment));
        di.add(new FormField("", dfilename));
        u.f = new FormLineContainer(di);
        return u;
    }

    @Override
    public IComposeController construct(ICallContext iContext) {
        IDataType dType = iContext.getDType();
        ISlotSignalContext slContext = iContext.iSlo().getSlContainer().getGetterContext(dType, GetActionEnum.GetHeaderList);
        VListHeaderContainer listHeader = slContext.getListHeader();
        IComposeController i = fFactory.construct(dType);
        IDataFormConstructorAbstractFactory cFactory;
        PersistTypeEnum e = iContext.getPersistTypeEnum();
        ReturnU up = new ReturnU();
        if (e == PersistTypeEnum.ADD) {
            final ReturnU u = getUpLoad(listHeader);
            up = u;
            final IDataFormConstructor cConstructor = new IDataFormConstructor() {

                @Override
                public Widget construct(ICallContext iContext, FormLineContainer model) {
                    return u.u;
                }
            };
            cFactory = new IDataFormConstructorAbstractFactory() {

                @Override
                public CType construct(ICallContext iContext) {
                    return new IDataFormConstructorAbstractFactory.CType(cConstructor);
                }
            };


        } else {
            up.f = aFactory.construct(iContext, listHeader);
            cFactory = new IDataFormConstructorAbstractFactory() {

                @Override
                public CType construct(ICallContext iContext) {
                    IDataFormConstructor c = new IDataFormConstructor() {

                        @Override
                        public Widget construct(ICallContext iContext, FormLineContainer model) {
                            Widget w = CreateFormView.construct(model.getfList());
                            return w;
                        }
                    };
                    return new IDataFormConstructorAbstractFactory.CType(c);
                }
            };
        }
        IDataViewModel daModel = daFactory.construct(iContext.getDType(),
                up.f, dFactory, cFactory);
        ComposeControllerType cType = new ComposeControllerType(daModel,
                iContext.getDType(), 0, 0);
        i.registerControler(cType);
        ComposeControllerType c1Type = new ComposeControllerType(new PersistAttach(dType, up.u));
        i.registerControler(c1Type);
        ComposeControllerType c2Type = compFactory.construct(iContext);
        i.registerControler(c2Type);
        return i;
    }
}
