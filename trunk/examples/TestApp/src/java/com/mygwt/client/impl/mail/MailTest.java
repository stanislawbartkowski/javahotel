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

package com.mygwt.client.impl.mail;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.factories.mailtest.IMailTest;
import com.gwtmodel.table.factories.mailtest.MailTestFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.mygwt.client.testEntryPoint;

public class MailTest implements testEntryPoint.IGetWidget {

    private final VerticalPanel vp = new VerticalPanel();
    private final MailTestFactory maFactory;

    public MailTest() {
        maFactory = GwtGiniInjector.getI().getMailTestFactory();
    }

    protected class SetTable implements ISlotListener {

        public void signal(ISlotSignalContext slContext) {
            IGWidget w = slContext.getGwtWidget();
            Widget ww = w.getGWidget();
            vp.clear();
            vp.add(ww);
        }
    }

    private void runI(ISlotable sl, IDataType mType) {
        CellId dialogId = new CellId(1);
        sl.getSlContainer().registerSubscriber(mType, dialogId, new SetTable());
        sl.startPublish(dialogId);
    }

    private void runCommand() {
        IDataType mType = Empty.getDataType();
        IMailTest mTest = maFactory.construct(mType);
        runI(mTest, mType);
        return;
    }

    @Override
    public Widget getW() {
        runCommand();
        return vp;
    }
}