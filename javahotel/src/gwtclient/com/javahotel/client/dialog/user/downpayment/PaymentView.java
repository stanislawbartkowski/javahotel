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

package com.javahotel.client.dialog.user.downpayment;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcView;
import com.javahotel.client.dialog.IMvcWidget;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PaymentView implements IMvcView {

    @SuppressWarnings("unused")
	private final IResLocator rI;
    private final PaymentModel mo;
    private final VerticalPanel vP = new VerticalPanel();

    PaymentView(IResLocator rI,PaymentModel mo) {
        this.rI = rI;
        this.mo = mo;
        HorizontalPanel hP = new HorizontalPanel();
        hP.setSpacing(5);
        hP.add(new Label("Termin płatności od: "));
        hP.add(mo.getDFrom().getMWidget().getWidget());
        hP.add(new Label(" do: "));
        hP.add(mo.getDTo().getMWidget().getWidget());
        vP.setSpacing(5);
        vP.add(hP);
        vP.add(mo.getITable().getMWidget().getWidget());
    }


    public void show() {
        mo.getITable().drawTable();
    }

    public void hide() {
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(vP);
    }

}
