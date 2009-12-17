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
package com.javahotel.client.dialog.user.booking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.ABillsCustomer;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.NumAbstractTo;
import com.javahotel.client.mvc.auxabstract.NumAddPaymentP;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.AbstractAuxRecordPanel;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.ICrudControlerDialog;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.dictdata.model.IGetAddPaymentList;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.mvc.persistrecord.IPersistResult.PersistResultContext;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.roominfo.RoomInfoData;
import com.javahotel.client.widgets.popup.PopupUtil;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddPayment {

    private final IResLocator rI;
    private BookingP p;
    private final RoomInfoData rInfo;
    private IPersistResult pe;
    private SS sync;
    private final ICrudControlerDialog iD;
    private DialogBox dBox;

    private class PE implements ICrudPersistSignal {

        public void signal(PersistResultContext re) {
            if (pe != null) {
                pe.success(re);
            }
        }
    }

    private class SS extends SynchronizeList {

        private final List<BillsCustomer> aList;

        SS(int no) {
            super(no);
            aList = new ArrayList<BillsCustomer>();
        }

        @Override
        protected void doTask() {
            List<ABillsCustomer> aCol = new ArrayList<ABillsCustomer>();
            for (BillsCustomer bi : aList) {
                ABillsCustomer a = new ABillsCustomer(bi);
                aCol.add(a);
            }
            iD.getI().getTableView().getModel().setList(aCol);
            iD.show();
        }
    }

    private class BackC implements RData.IOneList<CustomerP> {

        private final BillP be;

        BackC(BillP be) {
            this.be = be;
        }

        public void doOne(CustomerP cu) {
            BillsCustomer bi = new BillsCustomer(be, cu);
            bi.setBillType(be.getBillType());
            bi.setId(be.getId());
            bi.setAP(be.getAddpayments());
            sync.aList.add(bi);
            sync.signalDone();
        }
    }

    private class ServiceAux extends AbstractAuxRecordPanel {

        private ICrudControler iC;

        private ServiceAux() {
            RecordAuxParam aux = new RecordAuxParam();
            iC = HInjector.getI().getDictCrudControlerFactory().getCrud(new DictData(
                    SpecE.AddPaymentList), aux, null);
        }

        IGetAddPaymentList getP() {

            return new IGetAddPaymentList() {

                public List<NumAddPaymentP> getList() {
                    List<NumAddPaymentP> aList = (List<NumAddPaymentP>) iC
                            .getTableView().getModel().getList();
                    return aList;
                }

                public String getResName() {
                    return p.getName();
                }
            };
        }

        @Override
        public void setFields(RecordModel mo) {
            ABillsCustomer a;
            a = (ABillsCustomer) mo.getA();
            BillsCustomer bi = (BillsCustomer) a.getO();
            List<AddPaymentP> col = bi.getAP();
            List<NumAbstractTo> an = new ArrayList<NumAbstractTo>();
            if (col != null) {
                for (AddPaymentP pa : col) {
                    NumAddPaymentP au = new NumAddPaymentP(pa);
                    an.add(au);
                }
            }
            iC.getTableView().getModel().setList(an);
            iC.drawTable();
        }

        public IMvcWidget getMWidget() {
            return iC.getMWidget();
        }
    }

    AddPayment(IResLocator rI) {
        this.rI = rI;
        this.rInfo = new RoomInfoData(rI);
        RecordAuxParam aux = new RecordAuxParam();
        ServiceAux auxS = new ServiceAux();
        ListenerCustomer lC = new ListenerCustomer(rI);
        aux.setAuxV(auxS);
        aux.setAuxO1(auxS.getP());
        lC.modifAux(aux);

        aux.setPSignal(new PE());
        iD = HInjector.getI().getDictCrudControlerFactory().getCrudD(
                new DictData(SpecE.BillsList), aux, null, null);
    }

    void setPe(IPersistResult pe) {
        this.pe = pe;
    }

    void drawBill(BookingP book) {
        this.p = book;
        List<BillP> a = p.getBill();
        sync = new SS(a.size());

        for (BillP bu : a) {
            CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
                    bu.getCustomer());
            rI.getR().getOne(RType.ListDict, pa, new BackC(bu));
        }
    }

    void showDialog(Widget w) {
        dBox = (DialogBox) iD.getMWidget().getWidget();
        PopupUtil.setPos(dBox, w);

    }
}
