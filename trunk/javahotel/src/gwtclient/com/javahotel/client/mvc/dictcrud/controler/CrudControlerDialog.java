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

package com.javahotel.client.mvc.dictcrud.controler;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.util.MDialog;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CrudControlerDialog implements ICrudControlerDialog {

    private final DialogBox dBox;
    private final ICrudControler iCrud;

    CrudControlerDialog(final ICrudControler iCrud, final IContrButtonView bView) {

        this.iCrud = iCrud;

        VerticalPanel vP = new VerticalPanel();

        MDialog mb = new MDialog(vP, iCrud.getTableView().getModel()
                .getHeader()) {

            @Override
            protected void addVP(VerticalPanel vp) {
                if (bView != null) {
                    vp.add(bView.getMWidget().getWidget());
                }
                vp.add(iCrud.getMWidget().getWidget());
            }
        };
        dBox = mb.getDBox();
    }

    public ICrudControler getI() {
        return iCrud;
    }

    public void show() {
        dBox.show();
        iCrud.drawTable();
    }

    public void hide() {
        dBox.hide();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(dBox);
    }

}
