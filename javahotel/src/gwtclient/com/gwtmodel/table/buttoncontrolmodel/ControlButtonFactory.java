/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ClickButtonType.StandClickEnum;

public class ControlButtonFactory {

    private final List<ControlButtonDesc> dButton;
    private final List<ControlButtonDesc> akcButton;
    private final List<ControlButtonDesc> removeButton;
    private final List<ControlButtonDesc> okButton;
    private final List<ControlButtonDesc> chooseButton;
    private final List<ControlButtonDesc> yesnoButton;
    private final List<ControlButtonDesc> loginButton;
    private final List<ControlButtonDesc> printButton;
    private final List<ControlButtonDesc> filtrButton;
    private final List<ControlButtonDesc> findButton;
    private final IGetCustomValues c;

    private ControlButtonDesc constructButt(StandClickEnum bType) {
        String imageName = ControlButtonImages.getImageName(bType);
        switch (bType) {
            case FIND:
                return new ControlButtonDesc(imageName,
                        "Szukaj", new ClickButtonType(bType));

            case FINDFROMBEGINNING:
                return new ControlButtonDesc(imageName,
                        "Szukaj od początku", new ClickButtonType(bType));

            case FINDNOW:
                return new ControlButtonDesc(imageName,
                        "Szukaj", new ClickButtonType(bType));
            case FINDNEXT:
                return new ControlButtonDesc(imageName,
                        "Szukaj dalej", new ClickButtonType(bType));

            case SETFILTER:
                return new ControlButtonDesc(imageName,
                        "Ustaw filtr", new ClickButtonType(bType));

            case REMOVEFILTER:
                return new ControlButtonDesc(imageName,
                        "Usuń filtr", new ClickButtonType(bType));

            case FILTRLIST:
                return new ControlButtonDesc(imageName,
                        "Filtr", new ClickButtonType(bType));
            case ADDITEM:
                return new ControlButtonDesc(imageName,
                        c.getCustomValue(IGetCustomValues.ADDBUTTON),
                        new ClickButtonType(bType));
            case REMOVEITEM:
                return new ControlButtonDesc(imageName,
                        c.getCustomValue(IGetCustomValues.REMOVEBUTTON),
                        new ClickButtonType(bType));
            case MODIFITEM:
                return new ControlButtonDesc(imageName, c.getCustomValue(IGetCustomValues.MODIFITEM),
                        new ClickButtonType(bType));
            case SHOWITEM:
                return new ControlButtonDesc(
                        imageName,
                        c.getCustomValue(IGetCustomValues.SHOWITEM),
                        new ClickButtonType(bType));
            case ACCEPT:
                return new ControlButtonDesc(imageName, "Akceptujesz",
                        new ClickButtonType(bType));
            case RESIGN:
            case RESIGNLIST:
                return new ControlButtonDesc(imageName, "Rezygnujesz",
                        new ClickButtonType(bType));
            case CHOOSELIST:
                return new ControlButtonDesc(imageName, "Wybierasz",
                        new ClickButtonType(bType));

            default:
                break;
        }
        return null;
    }

    public ControlButtonFactory() {
        c = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
        dButton = new ArrayList<ControlButtonDesc>();
        dButton.add(constructButt(StandClickEnum.ADDITEM));
        dButton.add(constructButt(StandClickEnum.REMOVEITEM));
        dButton.add(constructButt(StandClickEnum.MODIFITEM));
        dButton.add(constructButt(StandClickEnum.FILTRLIST));
        dButton.add(constructButt(StandClickEnum.FIND));

        filtrButton = new ArrayList<ControlButtonDesc>();
        filtrButton.add(constructButt(StandClickEnum.SETFILTER));
        filtrButton.add(constructButt(StandClickEnum.REMOVEFILTER));
        filtrButton.add(constructButt(StandClickEnum.RESIGN));

        findButton = new ArrayList<ControlButtonDesc>();
        findButton.add(constructButt(StandClickEnum.FINDNOW));
        findButton.add(constructButt(StandClickEnum.FINDFROMBEGINNING));
        findButton.add(constructButt(StandClickEnum.FINDNEXT));
        findButton.add(constructButt(StandClickEnum.RESIGN));

        akcButton = new ArrayList<ControlButtonDesc>();
        akcButton.add(constructButt(StandClickEnum.ACCEPT));
        akcButton.add(constructButt(ClickButtonType.StandClickEnum.RESIGN));

        okButton = new ArrayList<ControlButtonDesc>();
        okButton.add(new ControlButtonDesc(null, "OK", new ClickButtonType(
                ClickButtonType.StandClickEnum.ACCEPT)));

        removeButton = new ArrayList<ControlButtonDesc>();
        removeButton.add(new ControlButtonDesc(null, "Usuwasz",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        removeButton.add(constructButt(ClickButtonType.StandClickEnum.RESIGN));

        chooseButton = new ArrayList<ControlButtonDesc>();
        chooseButton.add(constructButt(ClickButtonType.StandClickEnum.CHOOSELIST));
        chooseButton.add(constructButt(StandClickEnum.RESIGNLIST));

        yesnoButton = new ArrayList<ControlButtonDesc>();
        yesnoButton.add(new ControlButtonDesc(null, "Tak", new ClickButtonType(
                ClickButtonType.StandClickEnum.ACCEPT)));
        yesnoButton.add(new ControlButtonDesc(null, "Nie", new ClickButtonType(
                ClickButtonType.StandClickEnum.RESIGN)));

        loginButton = new ArrayList<ControlButtonDesc>();
        loginButton.add(new ControlButtonDesc(null, c.getCustomValue(IGetCustomValues.LOGINBUTTON),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));

        printButton = new ArrayList<ControlButtonDesc>();
        printButton.add(new ControlButtonDesc(null, "Drukujesz",
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        printButton.add(constructButt(StandClickEnum.RESIGN));
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

    public ListOfControlDesc constructOkButton() {
        return new ListOfControlDesc(okButton);
    }

    public ListOfControlDesc constructFilterButton() {
        return new ListOfControlDesc(filtrButton);
    }

    public ListOfControlDesc constructFindButton() {
        return new ListOfControlDesc(findButton);
    }

    public ListOfControlDesc constructList(List<StandClickEnum> cList) {
        List<ControlButtonDesc> lButton = new ArrayList<ControlButtonDesc>();
        for (StandClickEnum de : cList) {
            ControlButtonDesc bu = constructButt(de);
            if (bu != null) {
                lButton.add(bu);
            }
        }
        return new ListOfControlDesc(lButton);
    }
}
