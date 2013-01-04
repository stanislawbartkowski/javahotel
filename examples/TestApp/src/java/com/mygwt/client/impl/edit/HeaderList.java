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
package com.mygwt.client.impl.edit;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.util.ClickPopUp;

/**
 * @author hotel
 * 
 */
class HeaderList extends AbstractSlotContainer implements IHeaderListContainer {

    private final VListHeaderContainer vHeader;

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, vHeader);
    }

    private static class ImageSelect implements IColumnImageSelect {

        @Override
        public String getImage() {
            return "aaa";
        }

        @Override
        public String getImageHint() {
            return "pipka";
        }

        @Override
        public void executeImage(String val, int row, WSize w,
                final IExecuteSetString i) {
            Button b = new Button("Choose val");
            final ClickPopUp pUp = new ClickPopUp(w, b);
            ClickHandler ha = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    pUp.setVisible(false);
                    i.setString("choosed");

                }

            };
            b.addClickHandler(ha);
            pUp.setVisible(true);
        }

    }

    static List<VListHeaderDesc> getHList() {
        List<VListHeaderDesc> vList = new ArrayList<VListHeaderDesc>();
        vList.add(new VListHeaderDesc("Id", ItemVData.fID, false, null, true,
                null, null, null, "width:40px;", null, 0));
        vList.add(new VListHeaderDesc("Number", ItemVData.fNUMB, false, null,
                true, null, null, null, "width:60px;", null, 0));
        vList.add(new VListHeaderDesc("Date", ItemVData.fDATE, false, null,
                true, null, null, null, "width:100px;", null, 0));
        vList.add(new VListHeaderDesc("Name from", ItemVData.fNAMES, false,
                null, true, null, null, null, "width:100px;",
                new ImageSelect(), 0));
        vList.add(new VListHeaderDesc("Name", ItemVData.fNAME, false, null,
                true, null, null, null, null, null, 0));
        vList.add(new VListHeaderDesc("Mark", ItemVData.fMARK, false, null,
                true, null, null, null, null, null, 0));
        return vList;
    }

    public HeaderList(IDataType dType) {
        this.dType = dType;
        List<VFooterDesc> fList = new ArrayList<VFooterDesc>();
        fList.add(new VFooterDesc(ItemVData.fNUMB, null, ItemVData.fNUMB
                .getType()));

        vHeader = new VListHeaderContainer(getHList(), "List of edit items",
                IConsts.defaultPage, null, "1000px", "500px", fList);
    }

}
