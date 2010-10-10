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
package com.javahotel.client.mvc.recordviewdef;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictButtonFactory {

    private final ArrayList<ContrButton> dButton;
    private final ArrayList<ContrButton> akcButton;
    private final ArrayList<ContrButton> removeButton;
    private final ArrayList<ContrButton> loginButton;
    private final ArrayList<ContrButton> okButton;
    private final ArrayList<ContrButton> yesnoButton;
    private final ArrayList<ContrButton> chooseButton;
    private final IResLocator rI;

    @Inject
    public DictButtonFactory(final IResLocator rI) {
        this.rI = rI;

        dButton = new ArrayList<ContrButton>();
        dButton.add(new ContrButton("AddItem", "Dodaj", IPersistAction.ADDACION));
        dButton.add(new ContrButton("RemoveItem", "Usuń",
                IPersistAction.DELACTION));
        dButton.add(new ContrButton("ModifItem", "Popraw",
                IPersistAction.MODIFACTION));

        akcButton = new ArrayList<ContrButton>();
        akcButton.add(new ContrButton(null, "Akceptujesz",
                IPersistAction.AKCACTION));
        akcButton.add(new ContrButton(null, "Rezygnujesz",
                IPersistAction.RESACTION));

        removeButton = new ArrayList<ContrButton>();
        removeButton.add(new ContrButton(null, "Usuwasz",
                IPersistAction.AKCACTION));
        removeButton.add(new ContrButton(null, "Rezygnujesz",
                IPersistAction.RESACTION));

        loginButton = new ArrayList<ContrButton>();
        loginButton
                .add(new ContrButton(null, "Login", IPersistAction.AKCACTION));

        okButton = new ArrayList<ContrButton>();
        okButton.add(new ContrButton(null, "OK", IPersistAction.AKCACTION));

        yesnoButton = new ArrayList<ContrButton>();
        yesnoButton.add(new ContrButton(null, "Tak", IPersistAction.AKCACTION));
        yesnoButton.add(new ContrButton(null, "Nie", IPersistAction.RESACTION));

        chooseButton = new ArrayList<ContrButton>();
        chooseButton.add(new ContrButton(null, "Wybierasz",
                IPersistAction.AKCACTION));
        chooseButton.add(new ContrButton(null, "Rezygnujesz",
                IPersistAction.RESACTION));
    }

    public IContrPanel getDictButt() {
        return ContrButtonFactory.getContr(rI, dButton);
    }

    public IContrPanel getDictChooseButt() {
        ArrayList<ContrButton> cButton = new ArrayList<ContrButton>();
        cButton.addAll(chooseButton);
        cButton.addAll(dButton);
        return ContrButtonFactory.getContr(rI, cButton);
    }

    public IContrPanel getRecordAkcButt() {
        return ContrButtonFactory.getContr(rI, akcButton);
    }

    public IContrPanel getOkButton() {
        return ContrButtonFactory.getContr(rI, okButton);
    }

    public IContrPanel getRecordDelButt() {
        return ContrButtonFactory.getContr(rI, removeButton);
    }

    public IContrPanel getLoginButt() {
        return ContrButtonFactory.getContr(rI, loginButton);

    }

    public IContrPanel getYesNoButt() {
        return ContrButtonFactory.getContr(rI, yesnoButton);

    }
}
