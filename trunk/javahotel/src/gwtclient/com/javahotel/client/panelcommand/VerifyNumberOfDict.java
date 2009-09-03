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

import com.google.gwt.user.client.ui.HTML;
import com.javahotel.client.CallBackHotel;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class VerifyNumberOfDict implements IPanelCommandBeforeCheck {

    private final IResLocator sI;
    private IPanelCommand iPanel;
    private final DictType[] dList;
    private ISetGwtWidget iSet;
    private boolean errorTe;

    private class IRequestSet implements ReadRequestHtml.ISetRequestText {

        public void setText(String s) {
            HTML ha = new HTML(s);
            iSet.setGwtWidget(new DefaultMvcWidget(ha));
        }
    }

    class BackHo extends CallBackHotel<List<ReturnPersist>> {

        BackHo() {
            super(sI);
        }

        @Override
        public void onMySuccess(List<ReturnPersist> li) {
            boolean success = true;
            for (ReturnPersist pe : li) {
                int no = pe.getNumberOf();
                if (no == 0) {
                    success = false;
                }
            }
            if (success) {
                errorTe = false;
                iPanel.beforeDrawAction(iSet);
            } else {
                errorTe = true;
                String res = CommonUtil.getResAdr("cannotdisplaypanel.jsp");
                ReadRequestHtml.doGet(res, new IRequestSet());
//                ReadRequestHtml.doGet("http://localhost:8084/WebHotel/welcomeGWT.html", new IRequestSet());
            }
        }
    }

    VerifyNumberOfDict(IResLocator sI, DictType[] dList) {
        this.sI = sI;
        this.dList = dList;
    }

    public void setIPanelCommand(IPanelCommand i) {
        iPanel = i;
    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        this.iSet = iSet;
        List<CommandParam> co = new ArrayList<CommandParam>();
        for (int i = 0; i < dList.length; i++) {
            CommandParam pa = sI.getR().getHotelCommandParam();
            pa.setoP(HotelOpType.NumberOfDictRecords);
            pa.setDict(dList[i]);
            co.add(pa);
        }

        GWTGetService.getService().hotelOpRet(co, new BackHo());
    }

    public void drawAction() {
        if (!errorTe) {
            iPanel.drawAction();
        }
    }
}
