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
package com.javahotel.nmvc.factories.persist.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.view.util.ModalDialog;
import com.javahotel.client.M;

/**
 * @author hotel
 *
 */
class InvalidBookingDialog extends ModalDialog {

    private final Widget listOfR;
    
    /**
     * @param title
     */
    public InvalidBookingDialog(Widget listOfR) {
        super(M.L().CannotMakeBooking());
        this.listOfR = listOfR;
        create();
    }

    @Override
    protected void addVP(VerticalPanel vp) {
        InvalidBooking b = new InvalidBooking(listOfR);
        vp.add(b);        
    }

}