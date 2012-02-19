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
package com.javahotel.nmvc.factories.persist.ui;

import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.nmvc.factories.persist.dict.IPersistResult.PersistResultContext;

/**
 * @author hotel
 * 
 */
public class InvalidBookingAction {

    private InvalidBookingAction() {

    }
    
    private static class GetGWidget implements ISlotListener {
        
        private final IGWidget w;
        
        GetGWidget(IGWidget w) {
            this.w = w;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IGWidget li = slContext.getGwtWidget();
            InvalidBookingDialog i = new InvalidBookingDialog(li.getGWidget());
            i.show(w);            
        }
        
    }

    public static void action(PersistResultContext re, IGWidget w) {

        IDataType dType = Empty.getDataType();
        
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        ListDataViewFactory liFactory = tFactories.getlDataFactory();
        IListDataView iList = liFactory.construct(dType);

        IField[] fList = new IField[] { ResDayObjectStateP.F.bookName,
                ResDayObjectStateP.F.d, ResDayObjectStateP.F.resObject };
        List<VListHeaderDesc> hList = FFactory.constructH(null, fList);
        VListHeaderContainer vHeader = new VListHeaderContainer(hList,"Lista");
        iList.getSlContainer().publish(dType, vHeader);
        IDataListType dList = DataUtil.construct(re.getRet().getResState());        
        iList.getSlContainer().publish(dType,
                DataActionEnum.DrawListAction, dList);
        SlU.registerWidgetListener0(dType, iList, new GetGWidget(w));
        SlU.startPublish0(iList);
    }

}
