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

    private final List<ControlButtonDesc> dButton;
    private final List<ControlButtonDesc> akcButton;
    private final List<ControlButtonDesc> removeButton;

    public ControlButtonFactory() {
        dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(new ControlButtonDesc("New", "Dodaj", new ClickButtonType(
                ClickButtonType.StandClickEnum.ADDITEM)));
        dButton
                .add(new ControlButtonDesc("DeleteRed", "Usuń",
                        new ClickButtonType(
                                ClickButtonType.StandClickEnum.REMOVEITEM)));
        dButton.add(new ControlButtonDesc("DataViewerMin", "Popraw",
                new ClickButtonType(ClickButtonType.StandClickEnum.MODIFITEM)));

        akcButton = new ArrayList<ControlButtonDesc>();
        akcButton.add(new ControlButtonDesc(null, "Akceptujesz",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        akcButton.add(new ControlButtonDesc(null, "Rezygnujesz",
                new ClickButtonType(ClickButtonType.StandClickEnum.RESIGN)));

        removeButton = new ArrayList<ControlButtonDesc>();
        removeButton.add(new ControlButtonDesc(null, "Usuwasz",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        removeButton.add(new ControlButtonDesc(null, "Rezygnujesz",
                new ClickButtonType(ClickButtonType.StandClickEnum.RESIGN)));

    }

    public ListOfControlDesc constructCrudList() {
        return new ListOfControlDesc(dButton);
    }

    public ListOfControlDesc constructAcceptResign() {
        return new ListOfControlDesc(akcButton);
    }

    public ListOfControlDesc constructRemoveDesign() {
        return new ListOfControlDesc(removeButton);
    }

}
