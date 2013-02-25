/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import java.util.Map;

import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
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
    private boolean wasset = false;
    private final IGetCustomValues cValues;

    public ControlButtonDesc constructButt(StandClickEnum bType,
            ClickButtonType buttonType) {
        String imageName = ControlButtonImages.getImageName(cValues, bType);
        Map<String, String> mAction = MM.getL().ActionName();
        switch (bType) {
        case TABLEDEFAULTMENU:
            return new ControlButtonDesc(imageName, MM.getL().MenuForTable(),
                    buttonType);

        case CLEARFILTER:
        case CLEARFIND:
            return new ControlButtonDesc(imageName, MM.getL().ClearParam(),
                    buttonType);
        case FIND:
            return new ControlButtonDesc(imageName, MM.getL().SearchButton(),
                    buttonType);

        case FINDFROMBEGINNING:
            return new ControlButtonDesc(imageName, MM.getL()
                    .SearchFromBeginning(), buttonType);

        case FINDNOW:
            return new ControlButtonDesc(imageName, MM.getL().SearchButton(),
                    buttonType);
        case FINDNEXT:
            return new ControlButtonDesc(imageName, MM.getL().SearchNext(),
                    buttonType);

        case SETFILTER:
            return new ControlButtonDesc(imageName, MM.getL().SetFilter(),
                    buttonType);

        case REMOVEFILTER:
            return new ControlButtonDesc(imageName, MM.getL().RemoveFilter(),
                    buttonType);

        case FILTRLIST:
            return new ControlButtonDesc(imageName, MM.getL().Filter(),
                    buttonType);
        case ADDITEM:
            return new ControlButtonDesc(imageName,
                    mAction.get(StandClickEnum.ADDITEM.name()), buttonType);
        case REMOVEITEM:
            return new ControlButtonDesc(imageName,
                    mAction.get(StandClickEnum.REMOVEITEM.name()), buttonType);
        case MODIFITEM:
            return new ControlButtonDesc(imageName,
                    mAction.get(StandClickEnum.MODIFITEM.name()), buttonType);
        case SHOWITEM:
            return new ControlButtonDesc(imageName,
                    mAction.get(StandClickEnum.SHOWITEM.name()), buttonType);
        case ACCEPT:
            return new ControlButtonDesc(imageName, MM.getL().Accept(),
                    buttonType);
        case RESIGN:
        case RESIGNLIST:
            return new ControlButtonDesc(imageName, MM.getL().Resign(),
                    buttonType);
        case CHOOSELIST:
            return new ControlButtonDesc(imageName, MM.getL().Choose(),
                    buttonType);

        default:
            break;
        }
        return null;
    }

    public ControlButtonDesc constructButt(StandClickEnum bType) {
        return constructButt(bType, new ClickButtonType(bType));
    }

    private void setM() {
        if (wasset) {
            return;
        }
        dButton.add(constructButt(StandClickEnum.TABLEDEFAULTMENU));
        dButton.add(constructButt(StandClickEnum.ADDITEM));
        dButton.add(constructButt(StandClickEnum.REMOVEITEM));
        dButton.add(constructButt(StandClickEnum.MODIFITEM));
        dButton.add(constructButt(StandClickEnum.FILTRLIST));
        dButton.add(constructButt(StandClickEnum.FIND));

        filtrButton.add(constructButt(StandClickEnum.SETFILTER));
        filtrButton.add(constructButt(StandClickEnum.REMOVEFILTER));
        filtrButton.add(constructButt(StandClickEnum.CLEARFILTER));
        filtrButton.add(constructButt(StandClickEnum.RESIGN));

        findButton.add(constructButt(StandClickEnum.FINDNOW));
        findButton.add(constructButt(StandClickEnum.FINDFROMBEGINNING));
        findButton.add(constructButt(StandClickEnum.FINDNEXT));
        findButton.add(constructButt(StandClickEnum.CLEARFIND));
        findButton.add(constructButt(StandClickEnum.RESIGN));

        akcButton.add(constructButt(StandClickEnum.ACCEPT));
        akcButton.add(constructButt(ClickButtonType.StandClickEnum.RESIGN));

        okButton.add(new ControlButtonDesc(null, MM.getL().Ok(),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));

        removeButton.add(new ControlButtonDesc(null, MM.getL().Remove(),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        removeButton.add(constructButt(ClickButtonType.StandClickEnum.RESIGN));

        chooseButton
                .add(constructButt(ClickButtonType.StandClickEnum.CHOOSELIST));
        chooseButton.add(constructButt(StandClickEnum.RESIGNLIST));
        chooseButton.add(constructButt(StandClickEnum.FILTRLIST));
        chooseButton.add(constructButt(StandClickEnum.FIND));

        yesnoButton.add(new ControlButtonDesc(null, MM.getL().Yes(),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        yesnoButton.add(new ControlButtonDesc(null, MM.getL().No(),
                new ClickButtonType(ClickButtonType.StandClickEnum.RESIGN)));

        loginButton.add(new ControlButtonDesc(null, MM.getL().Login(),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));

        printButton.add(new ControlButtonDesc(null, MM.getL().Print(),
                new ClickButtonType(ClickButtonType.StandClickEnum.ACCEPT)));
        printButton.add(constructButt(StandClickEnum.RESIGN));

        wasset = true;
    }

    public ControlButtonFactory() {
        cValues = GwtGiniInjector.getI().getCustomValues();

        dButton = new ArrayList<ControlButtonDesc>();

        filtrButton = new ArrayList<ControlButtonDesc>();

        findButton = new ArrayList<ControlButtonDesc>();

        akcButton = new ArrayList<ControlButtonDesc>();

        okButton = new ArrayList<ControlButtonDesc>();

        removeButton = new ArrayList<ControlButtonDesc>();

        chooseButton = new ArrayList<ControlButtonDesc>();

        yesnoButton = new ArrayList<ControlButtonDesc>();

        loginButton = new ArrayList<ControlButtonDesc>();

        printButton = new ArrayList<ControlButtonDesc>();
    }

    public List<ControlButtonDesc> constructCrudListButtons() {
        setM();
        List<ControlButtonDesc> cList = new ArrayList<ControlButtonDesc>();
        cList.addAll(dButton);
        return cList;
    }

    public ListOfControlDesc constructCrudList() {
        setM();
        return new ListOfControlDesc(constructCrudListButtons());
    }

    public ListOfControlDesc constructAcceptResign() {
        setM();
        return new ListOfControlDesc(akcButton);
    }

    public ListOfControlDesc constructRemoveDesign() {
        setM();
        return new ListOfControlDesc(removeButton);
    }

    public ListOfControlDesc constructChooseList(String htmlFormat) {
        setM();
        return new ListOfControlDesc(chooseButton, htmlFormat);
    }

    public ListOfControlDesc constructYesNoButton() {
        setM();
        return new ListOfControlDesc(yesnoButton);
    }

    public ListOfControlDesc constructLoginButton() {
        setM();
        return new ListOfControlDesc(loginButton);
    }

    public ListOfControlDesc constructPrintButton() {
        setM();
        return new ListOfControlDesc(printButton);
    }

    public ListOfControlDesc constructOkButton() {
        setM();
        return new ListOfControlDesc(okButton);
    }

    public ListOfControlDesc constructFilterButton() {
        setM();
        return new ListOfControlDesc(filtrButton);
    }

    public ListOfControlDesc constructFindButton() {
        setM();
        return new ListOfControlDesc(findButton);
    }

    public List<ControlButtonDesc> constructDefCrud() {
        List<ControlButtonDesc> li = new ArrayList<ControlButtonDesc>();
        li.add(constructButt(StandClickEnum.TABLEDEFAULTMENU));
        return li;
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
