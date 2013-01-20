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
package com.jythonui.client.dialog;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.IJythonUIClient;
import com.jythonui.client.M;
import com.jythonui.shared.DialogFormat;

/**
 * @author hotel
 * 
 */
public class RunAction implements IJythonUIClient {

    private class GetCenterWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            IWebPanel i = GwtGiniInjector.getI().getWebPanel();
            i.setDCenter(w);
        }

    }

    private class StartBack extends CommonCallBack<DialogFormat> {

        private final IDataType dType;

        StartBack(IDataType dType) {
            this.dType = dType;
        }

        @Override
        public void onMySuccess(DialogFormat arg) {
            DialogContainer d = new DialogContainer(dType, arg,null);
            SlU.registerWidgetListener0(dType, d, new GetCenterWidget());
            CellId cId = new CellId(0);
            d.startPublish(cId);
        }
    }

    @Override
    public void start(String startdialogName) {
        IDataType dType = DataType.construct(startdialogName);
        M.JR().getDialogFormat(startdialogName, new StartBack(dType));
    }

}
