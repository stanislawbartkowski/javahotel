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
    private final List<ControlButtonDesc> chooseButton;
    private final List<ControlButtonDesc> yesnoButton;
    private final List<ControlButtonDesc> loginButton;
    private final List<ControlButtonDesc> printButton;

    public ControlButtonFactory() {
        dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(new ControlButtonDesc("New", "Dodaj", new ClickButtonType(
                ClickButtonType.StandClickEnum.ADDITEM)));
        dButton.add(new ControlButtonDesc("DeleteRed", "Usuń",
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

        chooseButton = new ArrayList<ControlButtonDesc>();
        chooseButton.add(new ControlButtonDesc(null, "Wybierasz",
                new ClickButtonType(
                ClickButtonType.StandClickEnum.CHOOSELIST)));
        chooseButton.add(new ControlButtonDesc(null, "Rezygnujesz",
                new ClickButtonType(
                ClickButtonType.StandClickEnum.RESIGNLIST)));

        yesnoButton = new ArrayList<ControlButtonDesc>();
        yesnoButton.add(new ControlButtonDesc(null, "Tak", new ClickButtonType(
                ClickButtonType.StandClickEnum.ACCEPT)));
        yesnoButton.add(new ControlButtonDesc(null, "Nie", new ClickButtonType(
                ClickButtonType.StandClickEnum.RESIGN)));

        loginButton = new ArrayList<ControlButtonDesc>();
        loginButton.add(new ControlButtonDesc(null, "Login",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));

        printButton = new ArrayList<ControlButtonDesc>();
        printButton.add(new ControlButtonDesc(null, "Drukujesz",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        printButton.add(new ControlButtonDesc(null, "Rezygnujesz",
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

    public ListOfControlDesc constructChooseList() {
        return new ListOfControlDesc(chooseButton);
    }

    public ListOfControlDesc constructYesNoButton() {
        return new ListOfControlDesc(yesnoButton);
    }

    public ListOfControlDesc constructLoginButton() {
        return new ListOfControlDesc(loginButton);
    }

    public ListOfControlDesc constructPrintButton() {
        return new ListOfControlDesc(printButton);
    }
}
