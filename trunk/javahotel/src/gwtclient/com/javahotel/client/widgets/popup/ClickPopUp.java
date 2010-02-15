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
package com.javahotel.client.widgets.popup;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;
import com.javahotel.client.dialog.IWidgetSize;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ClickPopUp {

    private PopupPanel pUp;

    public void setVisible(final boolean visible) {
        pUp.setVisible(visible);
    }

    private class CloseH implements CloseHandler<PopupPanel> {

        private final ICommand i;

        CloseH(ICommand i) {
            this.i = i;
        }

        public void onClose(CloseEvent<PopupPanel> event) {
            i.execute();
        }
    }

    private void setD(final IWidgetSize w, final Widget showW, ICommand i) {
        pUp = new PopupPanel(true);
        if (i != null) {
            pUp.addCloseHandler(new CloseH(i));
        }
        PopupUtil.setPos(pUp, w);
        pUp.setWidget(showW);
        pUp.show();
    }

    public ClickPopUp(final IWidgetSize w, final Widget showW) {
        setD(w, showW, null);
    }

    public ClickPopUp(final IWidgetSize w, final Widget showW, ICommand i) {
        setD(w, showW, i);
    }
}
