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

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcView;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.auxabstract.AdvancePaymentCustomer;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.persistrecord.IPersistResult.PersistResultContext;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.common.command.RType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DownPaymentControler implements IMvcView {

    private final IResLocator rI;
    private final PaymentModel mo;
    private final PaymentView view;

    public IMvcWidget getMWidget() {
        return view.getMWidget();
    }

    private class CallP implements ICrudPersistSignal {

        public void signal(PersistResultContext re) {
            rI.getR().invalidateCache(RType.DownPayments);
            show();
        }
    }


    private class TClicked implements ITableSignalClicked {

        public void signal(ClickedContext co) {
            AdvancePaymentCustomer a =
                (AdvancePaymentCustomer)
                mo.getITable().getTableView().getModel().getRow(co.getRow());
            new DrawResInfo(rI,a,co.getFCol(),co.getW(), new CallP());
        }
    }

    public DownPaymentControler(IResLocator rI) {
        this.rI = rI;
        mo = new PaymentModel(rI,new TClicked());
        view = new PaymentView(rI,mo);
        IChangeListener li = new IChangeListener() {

            public void onChange(ILineField arg0) {
                show();
            }
        };
        mo.getDFrom().setChangeListener(li);
        mo.getDTo().setChangeListener(li);
    }

    public void show() {
        view.show();
    }

    public void hide() {
    }

}
