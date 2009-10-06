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
package com.javahotel.client.mvc.util;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IClickNextYesNo;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class YesNoDialog extends AbstractDialog {

    public YesNoDialog(final IResLocator rI, final String ask,
            final IClickNextYesNo yes) {

        final VerticalPanel vp = new VerticalPanel();
        MDialog mDial = new MDialog(vp, "Pytanie") {

            @Override
            protected void addVP(VerticalPanel vp) {
                vp.add(new Label(ask));
            }
        };
        IContrPanel yP = DictButtonFactory.getYesNoButt(rI);
        IControlClick cli = new IControlClick() {

            public void click(ContrButton co, Widget w) {
                dBox.hide();
                yes.click(co.getActionId() == IPersistAction.AKCACTION);
            }
        };
        IContrButtonView i = ContrButtonViewFactory.getView(rI, yP, cli);
        vp.add(i.getMWidget().getWidget());
        dBox = mDial.getDBox();
    }

}
