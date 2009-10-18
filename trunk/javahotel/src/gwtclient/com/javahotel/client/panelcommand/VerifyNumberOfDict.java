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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.CallBackHotel;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.rhtml.ISetResText;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;

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
    private final int numb[];
    private final String pageName;

    public IMvcWidget getWestWidget() {
        return null;
    }
    
    private class SetRes implements ISetResText {

        public void setResText(String s) {
            VerticalPanel hp = new VerticalPanel();
            for (int i = 0; i < dList.length; i++) {
                String sl = sI.getLabels().DictList().get(dList[i].toString());
                String fo = sI.getMessages().noDict(sl, numb[i]);
                hp.add(new Label(fo));
            }
            HTML ha = new HTML(s);
            hp.add(ha);
            iSet.setGwtWidget(new DefaultMvcWidget(hp));
            
        }
        
    }

   class BackHo extends CallBackHotel<List<ReturnPersist>> {

        BackHo() {
            super(sI);
        }

        @Override
        public void onMySuccess(List<ReturnPersist> li) {
            boolean success = true;
            int nn = 0;
            for (ReturnPersist pe : li) {
                int no = pe.getNumberOf();
                numb[nn++] = no;
                if (no == 0) {
                    success = false;
                }
            }
            if (success) {
                errorTe = false;
                iPanel.beforeDrawAction(iSet);
            } else {
                errorTe = true;
                sI.readRes().readRes(new SetRes(), pageName);
            }
        }
    }

    VerifyNumberOfDict(IResLocator sI, DictType[] dList, String pageName) {
        this.sI = sI;
        this.dList = dList;
        numb = new int[dList.length];
        this.pageName = pageName;
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
