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
package com.gwtmodel.table.buttoncontrolmodel;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.slotmodel.ClickButtonType;

public class ControlButtonFactory {

    private static List<ControlButtonDesc> dButton;

    static {
        dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(new ControlButtonDesc("New", "Dodaj", new ClickButtonType(
                ClickButtonType.StandClickEnum.ADDITEM)));
        dButton
                .add(new ControlButtonDesc("DeleteRed", "Usuń",
                        new ClickButtonType(
                                ClickButtonType.StandClickEnum.REMOVEITEM)));
        dButton
                .add(new ControlButtonDesc("DataViewerMin", "Popraw",
                        new ClickButtonType(
                                ClickButtonType.StandClickEnum.MODIFITEM)));
    }

    public static ListOfControlDesc constructCrudList() {
        return new ListOfControlDesc(dButton);
    }

}
