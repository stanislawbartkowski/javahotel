/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.panel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IWebHotelPanel {

    Label getReplyL();

    Label getErrorL();

    void setUserHotel(String user, String hotel);

    void setDCenter(Widget w);

    void setWest(Widget w);

    void setWest1(Widget w);

    Widget getWidget();

    void setMenuPanel(Panel pa);
}
