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
package com.javahotel.nmvc.factories;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.view.table.ColumnDataType;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.common.command.RType;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VField;

class HeaderListContainer extends AbstractSlotContainer implements
        IHeaderListContainer {

    private final VListHeaderContainer vHeader;
    private final IDataType dType;

    public void startPublish(CellId cellId) {
        publish(dType, vHeader);
    }

    HeaderListContainer(ColListFactory cFactory, IDataType dType) {
        DictData dt = DataUtil.constructDictData(dType);
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        if (dt.isRt()) {
            if (dt.getRt() == RType.AllPersons) {
                VListHeaderDesc v = new VListHeaderDesc("Nazwa",
                        new LoginField(LoginField.F.LOGINNAME),
                        ColumnDataType.STRING);
                heList.add(v);
            }

        }
        String title = cFactory.getHeader(dt);
        if (heList.isEmpty()) {
            List<ColTitle> coList = cFactory.getColList(dt);
            for (ColTitle co : coList) {
                VListHeaderDesc he = new VListHeaderDesc(co.getCTitle(),
                        new VField(co.getF()), ColumnDataType.STRING);
                heList.add(he);
            }
        }
        vHeader = new VListHeaderContainer(heList, title);
        this.dType = dType;
    }

}
