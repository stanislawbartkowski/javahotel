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
package com.javahotel.client.widgets.disclosure;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DisclosureOptions extends Composite {

    public DisclosureOptions(final IResLocator rI,
            List<ContrButton> dButton, final IControlClick con) {

        DisclosurePanel ds = new DisclosurePanel(new DisclosureImages(),
                "Opcje", false);
        VerticalPanel vp = new VerticalPanel();
        IContrPanel co = ContrButtonFactory.getContr(rI, dButton);
        IContrButtonView i = ContrButtonViewFactory.getViewV(rI, co, con);
        vp.add(i.getMWidget().getWidget());
        ds.add(vp);
        initWidget(ds);
    }
}
