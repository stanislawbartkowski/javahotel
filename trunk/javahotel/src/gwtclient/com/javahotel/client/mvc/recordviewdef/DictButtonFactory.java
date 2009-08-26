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
package com.javahotel.client.mvc.recordviewdef;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DictButtonFactory {

    private final static ArrayList<ContrButton> dButton;
    private final static ArrayList<ContrButton> akcButton;
    private final static ArrayList<ContrButton> removeButton;
    private final static ArrayList<ContrButton> loginButton;
    private final static ArrayList<ContrButton> okButton;
    private final static ArrayList<ContrButton> yesnoButton;
    private final static ArrayList<ContrButton> chooseButton;


    static {
        dButton = new ArrayList<ContrButton>();
        dButton.add(new ContrButton("New", "Dodaj", IPersistAction.ADDACION));
        dButton.add(new ContrButton("DeleteRed", "Usuń",
                IPersistAction.DELACTION));
        dButton.add(new ContrButton("DataViewerMin", "Popraw",
                IPersistAction.MODIFACTION));

        akcButton = new ArrayList<ContrButton>();
        akcButton.add(new ContrButton(null, "Akceptujesz", IPersistAction.AKCACTION));
        akcButton.add(new ContrButton(null, "Rezygnujesz", IPersistAction.RESACTION));

        removeButton = new ArrayList<ContrButton>();
        removeButton.add(new ContrButton(null, "Usuwasz", IPersistAction.AKCACTION));
        removeButton.add(new ContrButton(null, "Rezygnujesz", IPersistAction.RESACTION));

        loginButton = new ArrayList<ContrButton>();
        loginButton.add(new ContrButton(null, "Login", IPersistAction.AKCACTION));

        okButton = new ArrayList<ContrButton>();
        okButton.add(new ContrButton(null, "OK", IPersistAction.AKCACTION));

        yesnoButton = new ArrayList<ContrButton>();
        yesnoButton.add(new ContrButton(null, "Tak", IPersistAction.AKCACTION));
        yesnoButton.add(new ContrButton(null, "Nie", IPersistAction.RESACTION));

        chooseButton = new ArrayList<ContrButton>();
        chooseButton.add(new ContrButton(null, "Wybierasz", IPersistAction.AKCACTION));
        chooseButton.add(new ContrButton(null, "Rezygnujesz", IPersistAction.RESACTION));
    }

    public static IContrPanel getDictButt(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, dButton);
    }

    public static IContrPanel getDictChooseButt(final IResLocator rI) {
        ArrayList<ContrButton> cButton = new ArrayList<ContrButton>();
        cButton.addAll(chooseButton);
        cButton.addAll(dButton);
        return ContrButtonFactory.getContr(rI, cButton);
    }

    public static IContrPanel getRecordAkcButt(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, akcButton);
    }

    public static IContrPanel getOkButton(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, okButton);
    }

    public static IContrPanel getRecordDelButt(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, removeButton);
    }

    public static IContrPanel getLoginButt(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, loginButton);

    }

    public static IContrPanel getYesNoButt(final IResLocator rI) {
        return ContrButtonFactory.getContr(rI, yesnoButton);

    }
}
