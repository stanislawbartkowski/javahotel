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
package com.gwtmodel.table.view.util;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;

abstract public class ModalDialog {

    private final DialogBox dBox;
    private final VerticalPanel vP;
    private final String title;

    private class CloseClick implements ICloseAction {

        public void onClose() {
            hide();
        }
    }

    protected abstract void addVP(VerticalPanel vp);

    public ModalDialog(VerticalPanel vP, String title) {
        this.vP = vP;
        this.title = title;
        dBox = new DialogBox(false, true);
    }

    protected void create() {
        PopupUtil.addClose(vP, new CloseClick(), null, null);
        addVP(vP);
        dBox.setWidget(vP);
        dBox.setText(title);
    }

    /**
     * @return the dBox
     */
    public DialogBox getDBox() {
        return dBox;
    }

    public void show() {
        dBox.show();
    }

    public void hide() {
        dBox.hide();
    }
}
