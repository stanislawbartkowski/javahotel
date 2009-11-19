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
package com.javahotel.client.mvc.dictcrud.controler.bookroom;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.ICreateViewContext;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;

public class BookRoom extends AbstractAuxRecordPanel {

    private final IResLocator rI;
    private HorizontalPanel hp;
    private PriceRecord pri;

    @Inject
    public BookRoom(IResLocator rI) {
        this.rI = rI;
    }

    private class SetP implements ISetGwtWidget {

        public void setGwtWidget(IMvcWidget i) {
            hp.add(i.getWidget());
        }

    }

    @Override
    public boolean getCustomView(ISetGwtWidget iSet, ICreateViewContext con,
            IContrButtonView i) {

        hp = new HorizontalPanel();
        IBookRoomConnector bRom = (IBookRoomConnector) iContx.getI();
        con.getIPanel().add(hp);
        VerticalPanel pad = new VerticalPanel();
        con.createDefaultDialog(pad);
        RecordField serv = null;
        for (RecordField f : con.getModel().getFields()) {
            if (f.getFie() == BookElemP.F.service) {
                serv = f;
            }
        }
        hp.add(pad);
        if (i != null) {
            VerticalPanel pa = new VerticalPanel();
            pa.add(i.getMWidget().getWidget());
            pad.add(pa);
        }
        pri = new PriceRecord(rI, new SetP(), bRom, serv.getELine());
        iSet.setGwtWidget(new DefaultMvcWidget(hp));

        return true;
    }

    public IMvcWidget getMWidget() {
        // return new DefaultMvcWidget(new Label("aaaaaa"));
        return null;
    }

}
