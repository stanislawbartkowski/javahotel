/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panelcommand;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.edittable.controller.ControlerEditTableFactory;
import com.javahotel.client.mvc.edittable.controller.IControlerEditTable;
import com.javahotel.common.toobject.ResObjectP;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class Test1Panel implements IPanelCommand {

    private IControlerEditTable cPan;
    private final IResLocator rI;

    Test1Panel(IResLocator rI) {
        this.rI = rI;

    }

    public void beforeDrawAction() {
        cPan = ControlerEditTableFactory.getTable(rI,
                new DictData(SpecE.ResGuestList), null);
        ArrayList<ResRoomGuest> gList = new ArrayList<ResRoomGuest>();
        for (int i = 0; i < 3; i++) {
            ResObjectP re = new ResObjectP();
            re.setName("No" + i);
            re.setDescription("Desc" + i);
            gList.add(new ResRoomGuest(re));
        }
        cPan.getView().getModel().setList(gList);
        cPan.show();
    }

    public IMvcWidget getMWidget() {
        return cPan.getMWidget();
    }

    public void drawAction() {
        cPan.show();
    }
}
