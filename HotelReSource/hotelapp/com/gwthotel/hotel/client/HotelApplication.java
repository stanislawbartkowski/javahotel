/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.client;

import com.google.gwt.core.client.EntryPoint;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.Utils;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.server.IConsts;
import com.jythonui.server.ISharedConsts;
import com.jythonui.shared.CustomSecurity;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HotelApplication implements EntryPoint {
    public void onModuleLoad() {
        String hotelName = Utils.getURLParam(IHotelConsts.HOTELURLQUERY);
        CustomSecurity sec = new CustomSecurity();
        sec.setAttr(IConsts.OBJECTNAME, hotelName);
        sec.setAttr(IConsts.INSTANCEID, ISharedConsts.INSTANCEDEFAULT);
        UIGiniInjector.getI().getJythonClientStart().start(sec);
    }

}
