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
package com.javahotel.client.mvc.edittable.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.SynchronizeList;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictdata.model.IGetBooking;
import com.javahotel.client.mvc.edittable.controller.ControlerEditTableFactory;
import com.javahotel.client.mvc.edittable.controller.IControlerEditTable;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.util.MDialog;
import com.javahotel.client.mvc.util.OkDialog;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class EditTableDialog implements IEditTableDialog {

    private final IResLocator rI;
    private final String resName;
    private final MDialog md;
    private final IControlerEditTable cPan;
    private final DictData da;
    private S sync;
    private List<ResRoomGuest> gList;
    private IPersistResult aP;
    private final DictButtonFactory bFactory;

    public void setPersist(IPersistResult pe) {
        aP = pe;
    }

    public IMvcWidget getMWidget() {
        return null;
    }

    private class PE implements IPersistResult {

        public void success(PersistResultContext re) {
            Window.alert("sacved");
            md.getDBox().hide();
            if (aP != null) {
                aP.success(re);
            }
        }
    }

    private void saveGuest(List<? extends AbstractTo> prevL) {
        PersistRecordFactory pFactory = HInjector.getI().getPersistRecordFactory();
        IPersistRecord li = pFactory.getPersistList(da);
        IGetBooking ge = new IGetBooking() {

            public String getBookName() {
                return resName;
            }
        };
        RecordModel mo = new RecordModel(null, ge);
        mo.setAList((List<? extends AbstractTo>) gList);
        mo.setBeforeaList(prevL);
        li.persist(IPersistAction.ADDACION, mo, new PE());
    }

    private class S extends SynchronizeList {

        boolean ok;
        List<? extends AbstractTo> prevL;

        S(int no) {
            super(no);
            ok = true;
        }

        @Override
        protected void doTask() {
            if (!ok) {
                return;
            }
            saveGuest(prevL);
        }
    }

    private class SignS implements ISignalValidate {

        private final IRecordView vie;

        SignS(IRecordView v) {
            this.vie = v;
        }

        public void success() {
            sync.signalDone();
        }

        public void failue(IErrorMessage errmess) {
            sync.ok = false;
            sync.signalDone();
            vie.showInvalidate(errmess);
        }
    }

    private class Accept implements IControlClick {

        public void click(ContrButton co, Widget w) {
            if (co.getActionId() == IPersistAction.RESACTION) {
                md.getDBox().hide();
                return;
            }
            int no = cPan.getView().getModel().rowNum();
            sync = new S(no);
            gList = new ArrayList<ResRoomGuest>();
            int ne = 0;
            sync.prevL = cPan.getView().getModel().getList();
            for (int i = 0; i < no; i++) {
                IRecordView v = cPan.getView().getR(i);
                ICrudRecordFactory fa = cPan.getView().getFactory(i);
                RecordModel mo = fa.getNew(null, null);
                mo.setRDef(v.getModel());
                cPan.getView().ExtractFields(i, mo);
                IRecordValidator va = fa.getValidator();
                boolean e = va.isEmpty(mo);
                boolean ok = true;
                if (!e) {
                    ne++;
                    ResRoomGuest g = (ResRoomGuest) mo.getA();
                    gList.add(g);
                    va.validateS(IPersistAction.ADDACION, mo, new SignS(v));
                } else {
                    sync.signalDone();
                }
            }
            if (ne == 0) {
                String mess = "Nie ma żadnego checkin (powinno być " + no
                        + ").";
                OkDialog okD = new OkDialog(rI, mess, null);
                okD.show(w);
            }
        }
    }

    EditTableDialog(IResLocator rI, DictData da,
            List<? extends AbstractTo> gList, String resName) {
        this.rI = rI;
        this.resName = resName;
        this.da = da;
        aP = null;
        bFactory = HInjector.getI().getDictButtonFactory();
        IContrPanel cpanel = bFactory.getRecordAkcButt();
        final IContrButtonView i = ContrButtonViewFactory.getView(rI, cpanel,
                new Accept());

        cPan = ControlerEditTableFactory.getTable(rI, da, i);
        cPan.getView().getModel().setList(gList);
        VerticalPanel vP = new VerticalPanel();
        md = new MDialog(vP, "Meldowanie") {

            @Override
            protected void addVP(VerticalPanel vp) {
                vp.add(i.getMWidget().getWidget());
                vp.add(cPan.getMWidget().getWidget());
            }
        };
        cPan.show();
        md.getDBox().show();
    }

    public DialogBox getDialog() {
        return md.getDBox();
    }

    public void show() {
        cPan.show();
        md.getDBox().show();
    }

    public void hide() {
    }
}
