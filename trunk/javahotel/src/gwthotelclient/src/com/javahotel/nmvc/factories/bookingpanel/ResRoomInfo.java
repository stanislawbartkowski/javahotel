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
package com.javahotel.nmvc.factories.bookingpanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.ButtonClickHandler;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;

/**
 * Display enhanced information on one room.
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ResRoomInfo extends Composite {

    private final VerticalPanel v = new VerticalPanel();

    private Label getL(final String de, final String va) {
        Label l = new Label(de + " : " + va);
        return l;
    }

    private void addInfo(final ResObjectP r) {
        String name = r.getName();
        String desc = r.getDescription();
        DictionaryP sta = r.getRStandard();
        v.add(getL("Numer", name));
        v.add(getL("Opis", desc));
        v.add(getL("Standard", sta.getName() + " " + sta.getDescription()));
        Button b = new Button("Zobacz");
        v.add(b);
        b.addClickHandler(new ButtonClickHandler<ResObjectP>(r,
                DictType.RoomObjects));
    }

    private class ReadRoom implements BackAbstract.IRunAction<ResObjectP> {

        @Override
        public void action(ResObjectP t) {
            addInfo(t);
        }
    }

    ResRoomInfo(final String roomName) {
        initWidget(v);
        new BackAbstract<ResObjectP>().readAbstract(DictType.RoomObjects,
                roomName, new ReadRoom());

    }
}
