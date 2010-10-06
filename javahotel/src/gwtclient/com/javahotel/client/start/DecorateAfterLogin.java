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
package com.javahotel.client.start;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;

class DecorateAfterLogin implements ICommand {

    private final IResLocator rI;

    DecorateAfterLogin(IResLocator rI) {
        this.rI = rI;
    }

    public void execute() {
        IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
        String user = rI.getR().getUserName();
        String hotel = rI.getR().getHotel();
        iW.setUserData(user, hotel);
    }
}
