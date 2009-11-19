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
package com.javahotel.client.mvc.crud.controler;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerFactory;
import com.javahotel.client.mvc.crudtable.controler.CrudTableControlerParam;
import com.javahotel.client.mvc.crudtable.controler.ICrudTableControler;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.client.mvc.tablepanel.ITablePanel;
import com.javahotel.client.mvc.tablepanel.WidgetTableFactory;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author hotel
 */
class CrudControler implements ICrudControler {

    private final IResLocator rI;
    private final IContrPanel panel;
    private final ITablePanel taP;
    private final ICrudRecordFactory fa;
    private final IContrButtonView cview;
    private final ICrudAuxControler cAux;
    private boolean enabled = true;
    private final ICrudTableControler iTable;

    private ICrudView getView(final RecordModel a, final ICrudAccept acc,
            final int actionId, final IPanel vp) {
        ICrudView tview = fa.getView(a, acc, actionId, vp);
        return tview;
    }

    public ICrudRecordFactory getF() {
        return fa;
    }

    public ITableView getTableView() {
        return iTable.getTableView();
    }

    public void changeMode(int actionMode) {
        enabled = true;
        if (actionMode == IPersistAction.DELACTION) {
            enabled = false;
        }
        setEnable(enabled);
        if (cview != null) {
            cview.setEnable(IPersistAction.ADDACION, enabled);
        }
    }

    public void drawTable() {
        iTable.drawTable();
    }

    public IMvcWidget getMWidget() {
        return taP.getMWidget();
    }

    private class CAccept implements ICrudAccept {

        private final int action;
        private final ICrudRecordControler con;
        private ICrudView rview;

        private class ValidateBack implements ISignalValidate {

            private final RecordModel a;
            private final boolean aux;

            ValidateBack(RecordModel a, boolean aux) {
                this.a = a;
                a.setRDef(fa.getRView(rview).getModel());
                this.aux = aux;
            }

            public void success() {
                if (!aux) {
                    IRecordValidator val = fa.getValidatorAux();
                    if (val != null) {
                        val.validateS(action, a, new ValidateBack(a, true));
                        return;
                    }
                }
                IPersistRecord pers = fa.getPersist();
                pers.persist(action, a, new CPersist());
            }

            public void failue(IErrorMessage errmess) {
                if (aux) {
                    con.showInvalidateAux(action, rview, errmess);
                } else {
                    con.showInvalidate(action, rview, errmess);
                }
            }
        }

        /**
         * @param rview
         *            the rview to set
         */
        public void setRview(ICrudView rview) {
            this.rview = rview;
        }

        private class CPersist implements IPersistResult {

            public void success(PersistResultContext re) {
                if (fa.getPersistSignal() != null) {
                    PersistCrudContext pC = new PersistCrudContext();
//                    pC.setWDialog(fa.getRView(rview).getMWidget().getWidget());
                    re.setAuxContext(pC);
                    fa.getPersistSignal().signal(re);
                    if (pC.isStayDialog()) {
                        // keep dialog
                        return;
                    }
                }
                con.hideDialog(rview);
                if (iTable.getTableView().getModel().isTableDefined()) {
                    iTable.getTableView().invalidateClicked();
                    iTable.drawTable();
                }
            }
        }

        CAccept(int action, ICrudRecordControler con) {
            this.action = action;
            this.con = con;
        }

        public void accept(RecordModel a) {
            IRecordValidator val = fa.getValidator();
            val.validateS(action, a, new ValidateBack(a, false));
        }
    }

    public void RecordDialog(int actionId, Widget w, AbstractTo aa) {
        RecordModel a = fa.getNew(aa, aa);
        ICrudRecordControler con = fa.getControler();
        CAccept ca = new CAccept(actionId, con);
        ICrudView view = getView(a, ca, actionId, null);
        ca.setRview(view);
        con.showDialog(actionId, view, w);
    }

    private class CClick implements IControlClick {

        public void click(ContrButton co, Widget w) {
            AbstractTo aa = iTable.getTableView().getClicked();
            if (fa.getChoose() != null) {
                ICrudChooseTable.IControlerContext con = new ICrudChooseTable.IControlerContext() {

                    public ICrudControler getControler() {
                        return CrudControler.this;
                    }
                };
                switch (co.getActionId()) {
                case IPersistAction.AKCACTION:
                    if (aa == null) {
                        return;
                    }
                    fa.getChoose().signal(con, true);
                    return;
                case IPersistAction.RESACTION:
                    fa.getChoose().signal(con, false);
                    return;
                }
            }
            if (co.getActionId() == IPersistAction.ADDACION) {
                aa = null;
            } else {
                if (aa == null) {
                    return;
                }
            }
            RecordDialog(co.getActionId(), w, aa);
        }
    }

    private void setEnable(boolean enable) {
        if (cview != null) {
            cview.setEnable(IPersistAction.MODIFACTION, enable);
            cview.setEnable(IPersistAction.DELACTION, enable);
        }
    }

    private void changeButtons() {
        if (!enabled) {
            return;
        }
        int size = iTable.getTableView().getModel().rowNum();
        boolean enable = true;
        if (size == 0) {
            enable = false;
        }
        setEnable(enable);
    }

    CrudControler(final IResLocator rI, final DictData da,
            final ITableModel model, final IContrPanel panel,
            final ICrudRecordFactory fa, final ICrudAuxControler cAux,
            final IControlClick iClick) {
        this.rI = rI;
        this.cAux = cAux;
        this.panel = panel;
        this.fa = fa;
        ITableModelSignalRead iSig = new ITableModelSignalRead() {

            public void successRead() {
                changeButtons();
            }
        };
        CrudTableControlerParam pa = new CrudTableControlerParam();
        pa.setIRead(fa.getCrudRead());
        pa.setSRead(iSig);
        pa.setSc(fa.getClicked());
        if (panel != null) {
            IControlClick c;
            if (iClick != null) {
                c = iClick;
            } else {
                c = new CClick();
            }
            cview = ContrButtonViewFactory.getView(rI, panel, c);
        } else {
            cview = null;
        }
        pa.setCView(cview);

        iTable = CrudTableControlerFactory.getCrudTable(rI, da, model, pa);
        taP = WidgetTableFactory.getGwtTable();
        Widget up = iTable.getContrWidget();
        Widget add = null;
        if (cAux != null) {
            add = cAux.getMWidget().getWidget();
        }
        taP.addPanels(up, iTable.getMWidget().getWidget(), add);
    }

}
