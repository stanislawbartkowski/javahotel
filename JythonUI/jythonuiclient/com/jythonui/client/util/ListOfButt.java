/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;

public class ListOfButt {

    private ListOfButt() {

    }

    public interface IGetButtons {

        List<ControlButtonDesc> getList();

        List<ControlButtonDesc> getCustomList();
    }

    public static IGetButtons constructList(DialogFormat d, String sButton) {

        ControlButtonFactory buFactory = GwtGiniInjector.getI()
                .getControlButtonFactory();
        String[] liButton = sButton.split(",");
        final List<ControlButtonDesc> cList = new ArrayList<ControlButtonDesc>();
        final List<ControlButtonDesc> customList = new ArrayList<ControlButtonDesc>();
        for (String s : liButton) {
            StandClickEnum bu = null;
            if (s.equals(ICommonConsts.BUTT_ADD)) {
                bu = StandClickEnum.ADDITEM;
            }
            if (s.equals(ICommonConsts.BUTT_REMOVE)) {
                bu = StandClickEnum.REMOVEITEM;
            }
            if (s.equals(ICommonConsts.BUTT_MODIF)) {
                bu = StandClickEnum.MODIFITEM;
            }
            if (s.equals(ICommonConsts.BUTT_SHOW)) {
                bu = StandClickEnum.SHOWITEM;
            }
            if (s.equals(ICommonConsts.BUTT_TOOLS)) {
                bu = StandClickEnum.TABLEDEFAULTMENU;
            }
            if (s.equals(ICommonConsts.BUTT_FILTER)) {
                bu = StandClickEnum.FILTRLIST;
            }
            if (s.equals(ICommonConsts.BUTT_FIND)) {
                bu = StandClickEnum.FIND;
            }
            String actionButt = FieldItem.getCustomT(s);
            ControlButtonDesc b = null;
            if (!CUtil.EmptyS(actionButt)) {
                ButtonItem but = DialogFormat.findE(d.getActionList(),
                        actionButt);
                b = CreateForm.constructButton(but, true, false);
                customList.add(b);
            }
            if (bu != null)
                b = buFactory.constructButt(bu);
            if (b != null)
                cList.add(b);
        } // for
        
        return new IGetButtons() {

            @Override
            public List<ControlButtonDesc> getList() {
                return cList;
            }

            @Override
            public List<ControlButtonDesc> getCustomList() {
                return customList;
            }

        };

    }
}
