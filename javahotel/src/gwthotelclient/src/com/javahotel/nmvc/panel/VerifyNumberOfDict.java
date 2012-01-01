/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.panel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.readres.ISetResText;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.start.panel.IPanelCommand;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class VerifyNumberOfDict implements IPanelCommandBeforeCheck {

    private final IResLocator sI;
    private IPanelCommand iPanel;
    private final DictType[] dList;
    private ISetGWidget iSet;
    private final int numb[];
    private final String pageName;
    private final RType r;

    private class SetRes implements ISetResText {

        @Override
        public void setResText(String s) {
            VerticalPanel hp = new VerticalPanel();
            for (int i = 0; i < numb.length; i++) {
                String fo;
                if (r == null) {
                  String d = dList[i].toString();
                  String sl = sI.getLabels().DictList().get(d);
                  fo = sI.getMessages().noDict(sl, numb[i]);
                }
                else {
                  fo = sI.getMessages().NoHotel(numb[i]);  
                }
                hp.add(new Label(fo));
            }
            HTML ha = new HTML(s);
            hp.add(ha);
            iSet.setW(new GWidget(hp));
        }
    }
    
    private class BackT extends CommonCallBack<List<AbstractTo>> {

        @Override
        public void onMySuccess(List<AbstractTo> arg) {
            if (arg.size() > 0) {
                iPanel.beforeDrawAction(iSet);                
            }
            else {
                numb[0] = 0;
                sI.readRes().readRes(new SetRes(), pageName);
            }
            
        }
        
    }

    private class BackHo extends CommonCallBack<List<ReturnPersist>> {

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
                iPanel.beforeDrawAction(iSet);
            } else {
                sI.readRes().readRes(new SetRes(), pageName);
            }
        }
    }

    VerifyNumberOfDict(DictType[] dList, String pageName) {
        this.sI = HInjector.getI().getI();
        this.dList = dList;
        numb = new int[dList.length];
        this.pageName = pageName;
        this.r = null;
    }
    
    VerifyNumberOfDict(RType r, String pageName) {
        this.sI = HInjector.getI().getI();
        this.dList = null;
        numb = new int[1];
        this.pageName = pageName;
        this.r = r;
    }

    @Override
    public void setIPanelCommand(IPanelCommand i) {
        iPanel = i;
    }

    @Override
    public void beforeDrawAction(ISetGWidget iSet) {
        this.iSet = iSet;
        if (r == null) {
          List<CommandParam> co = new ArrayList<CommandParam>();
          for (int i = 0; i < dList.length; i++) {
              CommandParam pa = sI.getR().getHotelCommandParam();
              pa.setoP(HotelOpType.NumberOfDictRecords);
              pa.setDict(dList[i]);
              co.add(pa);
          }

          GWTGetService.getService().hotelOp(co, new BackHo());
        }
        else {
            CommandParam pa = sI.getR().getHotelCommandParam();
            GWTGetService.getService().getList(r, pa, new BackT());            
        }
    }

}
