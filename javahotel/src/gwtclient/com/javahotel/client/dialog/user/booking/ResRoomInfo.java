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
package com.javahotel.client.dialog.user.booking;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class ResRoomInfo extends Composite {

    private final VerticalPanel v = new VerticalPanel();
    private final ResObjectP r;
    private final IResLocator rI;

    private Label getL(final String de, final String va) {
        Label l = new Label(de + " : " + va);
        return l;
    }

    ResRoomInfo(final IResLocator rI, final ResObjectP r) {
        this.r = r;
        this.rI = rI;
        initWidget(v);
        String name = r.getName();
        String desc = r.getDescription();
        String d = r.getDescription();
        DictionaryP sta = r.getRStandard();
        v.add(getL("Numer", name));
        v.add(getL("Opis", desc));
        v.add(getL("Standard", sta.getName() + " " + sta.getDescription()));
    }
}
