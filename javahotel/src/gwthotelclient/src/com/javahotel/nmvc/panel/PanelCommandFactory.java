/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.panel;

import com.gwtmodel.table.ISetGWidget;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.client.start.panel.IPanelCommand;
import com.javahotel.client.start.panel.IPanelCommandFactory;
import com.javahotel.client.start.panel.IUserPanelMenuFactory;
import com.javahotel.client.start.panel.UserPanel;

/**
 * @author hotel
 * 
 */
public class PanelCommandFactory implements IPanelCommandFactory {

    @Override
    public IPanelCommand construct(EPanelCommand e) {
        if (e == EPanelCommand.ROOMSADMIN) {
            IPanelCommand i = new IPanelCommand() {

                @Override
                public void beforeDrawAction(ISetGWidget iSet) {
                    IUserPanelMenuFactory pa = new RoomsPanelFactory();
                    UserPanel.draw(pa);
                }

            };
            return i;
        }
        IPanelCommandBeforeCheck pI = PanelCommandBeforeCheckFactory
                .getPanelCheck(e);
        IPanelCommand i = new NewMvcPanel(e);
        if (pI != null) {
            pI.setIPanelCommand(i);
            return pI;
        }
        return i;
    }

}
