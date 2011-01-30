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
package com.javahotel.client.dialog.user.custinfo;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.common.toobject.CustomerP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CustomerAddInfo {

    private final VerticalPanel vp = new VerticalPanel();
    @SuppressWarnings("unused")
	private final CustomerP cu;

    public CustomerAddInfo(CustomerP p) {
        this.cu = p;
        vp.add(new Label(p.getName()));
        vp.add(new Label(p.getDescription()));
        vp.add(new Label(p.getFirstName()));
        vp.add(new Label(p.getLastName()));
        vp.add(new Label(p.getName1()));
        vp.add(new Label(p.getName2()));
    }

    public Widget getWidget() {
        return vp;
    }
}
