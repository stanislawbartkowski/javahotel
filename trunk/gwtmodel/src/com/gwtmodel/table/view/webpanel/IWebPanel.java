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
package com.gwtmodel.table.view.webpanel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ISignal;
import com.gwtmodel.table.TLogMessages;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public interface IWebPanel {

    void setErrorL(String errmess);

    void clearReply();

    Label getReplyL();

    void setUserData(String user, String data);

    void setDCenter(Widget w);

    void setCentreHideSignal(ISignal iSig);

    void setWest(Widget w);

    void setWest1(Widget w);

    Widget getWidget();

    void setMenuPanel(Widget pa);

    void setOwnerName(String owner);

    void IncDecCounter(boolean inc);

    void setCenterSize(String size);

    void setUpInfo(String upinfo);
}
