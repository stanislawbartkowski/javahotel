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
package com.javahotel.view.gwt.record.view;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.apanel.GwtPanel;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.IRecordViewFactory;
import com.javahotel.client.mvc.record.view.helper.IInitDialog;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class RecordViewFactory implements IRecordViewFactory {

    private static IPanel panelInstance() {
        IPanel custV = new GwtPanel(new VerticalPanel());
        return custV;
    }

    private static IInitDialog getIDialog(final IResLocator rI, IPanel ip,
            ISetGwtWidget iSet, final DictData da, final IRecordDef model,
            final IContrPanel contr, final IControlClick co,
            final IAuxRecordPanel auxV) {
        if (da.getRt() != null) {
            switch (da.getRt()) {
            case AllPersons:
            case AllHotels:
                return new RecordView(rI, ip, iSet, da, model, contr, co, auxV);
            default:
                break;
            }
        }
        if (da.isSe()) {
            switch (da.getSE()) {
            case LoginUser:
            case LoginAdmin:
            case SpecialPeriod:
                return new RecordView(rI, ip, iSet, da, model, contr, co, auxV);
            default:
                break;
            }
        }
        if (da.isDa()) {
            switch (da.getD()) {
            case BookingList:

            case RoomFacility:
            case RoomStandard:
            case RoomObjects:
            case VatDict:
            case ServiceDict:
            case OffSeasonDict:
            case PriceListDict:
                return new RecordView(rI, ip, iSet, da, model, contr, co, auxV);
            case CustomerList:
                return new CustomerView(rI, ip, iSet, da, model, contr, co, auxV);
            default:
                break;
            }
        }
        return new RecordView(rI, ip, iSet, da, model, contr, co, auxV);

    }

    public IRecordView getRecordView(final IResLocator rI, ISetGwtWidget iSet,
            final DictData da, final IRecordDef model,
            final IAuxRecordPanel auxV, final IPanel vp) {
        IInitDialog i = getIDialog(rI, vp, iSet, da, model, null, null, auxV);
        return i;
    }

    public IRecordView getRecordView(final IResLocator rI, ISetGwtWidget iSet,
            final DictData da, final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV) {
        IInitDialog i = getIDialog(rI, panelInstance(), iSet, da, model, contr, co, auxV);
        i.addEmptyList();
        return i;
    }

    public IRecordView getRecordViewDialog(final IResLocator rI,
            ISetGwtWidget iSet, final DictData da, final IRecordDef model,
            final IContrPanel contr, final IControlClick co,
            final IAuxRecordPanel auxV) {
        IInitDialog i = getIDialog(rI, panelInstance(), iSet, da, model, contr, co, auxV);
        return new RecordVDialog(rI, i);
    }

    public IRecordView getRecordViewDialog(final IResLocator rI,
            ISetGwtWidget iSet, final DictData da, final IRecordDef model,
            final IContrPanel contr, final IControlClick co,
            final IAuxRecordPanel auxV, Widget auxW) {
        if (auxW == null) {
            return getRecordViewDialog(rI, iSet, da, model, contr, co, auxV);
        }
        IInitDialog i = getIDialog(rI, panelInstance(), iSet, da, model, contr, co, auxV);
        return new RecordVDialog(rI, i, auxW);
    }

    public IRecordView getTableViewRecord(final IResLocator rI,
            ISetGwtWidget iSet, final IRecordDef model) {
        return new TableLineRecordView(rI, model);
    }
}
