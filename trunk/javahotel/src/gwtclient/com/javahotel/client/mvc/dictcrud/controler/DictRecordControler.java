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
package com.javahotel.client.mvc.dictcrud.controler;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.AbstractToFactory;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.dialog.MvcWindowSize;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudAccept;
import com.javahotel.client.mvc.crud.controler.ICrudChooseTable;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.mvc.crud.controler.ICrudReadModel;
import com.javahotel.client.mvc.crud.controler.ICrudRecordControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.ICrudView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.dictcrud.read.CrudReadModelFactory;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.mvc.record.extractor.IRecordExtractor;
import com.javahotel.client.mvc.record.extractor.RecordExtractorFactory;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordDefFactory;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.RecordViewFactory;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DictRecordControler implements ICrudRecordFactory {

    private final IResLocator rI;
    private final IRecordExtractor ex;
    private final DictData da;
    private final RecordAuxParam aParam;
    private final DictValidatorFactory dFactory;
    private final DictButtonFactory bFactory;
    private final GetRecordDefFactory gFactory;

    private CrudRView rView;
    private boolean setW;

    private void showDialog() {
        if (rView == null) {
            return;
        }
        if (!setW) {
            return;
        }
        rView.iView.show();
    }

    private void invalidate() {
        setW = false;
        rView = null;
    }

    DictRecordControler(final IResLocator rI, final DictData da,
            final RecordAuxParam aParam) {
        this.rI = rI;
        this.da = da;
        this.aParam = aParam;
        dFactory = HInjector.getI().getDictValidFactory();
        bFactory = HInjector.getI().getDictButtonFactory();
        gFactory = HInjector.getI().getGetRecordDefFactory();
        ex = RecordExtractorFactory.getExtractor(rI, da);
    }

    public ICrudReadModel getCrudRead() {
        CrudReadModelFactory fa = HInjector.getI().getCrudReadModelFactory();
        return fa.getRead(da);
    }

    public IRecordView getRView(ICrudView v) {
        CrudRView r = (CrudRView) v;
        return r.iView;
    }

    public IRecordExtractor getExtractor() {
        return ex;
    }

    public void show(ICrudView v) {
        rView = (CrudRView) v;
        showDialog();

        if (aParam.getAuxV() != null) {
            aParam.getAuxV().show();
        }
    }

    public ICrudPersistSignal getPersistSignal() {
        return aParam.getPSignal();
    }

    public ITableSignalClicked getClicked() {
        return aParam.getSClicked();
    }

    public IRecordValidator getValidatorAux() {
        if (aParam == null) {
            return null;
        }
        if (aParam.getAuxV() == null) {
            return null;
        }
        return aParam.getAuxV().getValidator();
    }

    public ICrudChooseTable getChoose() {
        return aParam.getIChoose();
    }

    public List<RecordField> getDef() {
        List<RecordField> dict = gFactory.getDef(da);
        if (aParam.getModifD() != null) {
            aParam.getModifD().modifRecordDef(dict);
        }
        return dict;
    }

    private class CrudDictRecordControler implements ICrudRecordControler {

        public void showDialog(final int action, final ICrudView view,
                final Widget w) {
            CrudRView r = (CrudRView) view;
            if (w != null) {
                r.iView.setPos(w);
            }
            show(r);
            if (aParam.getInfoP() != null) {
                aParam.getInfoP().draw(w, aParam.getAuxW(), r.mo);
            }
        }

        public void showInvalidate(final int action, final ICrudView view,
                final IErrorMessage vali) {
            CrudRView r = (CrudRView) view;
            r.iView.showInvalidate(vali);
        }

        public void hideDialog(final ICrudView view) {
            CrudRView r = (CrudRView) view;
            r.iView.hide();
        }

        public void showInvalidateAux(int action, ICrudView view,
                IErrorMessage vali) {
            aParam.getAuxV().showInvalidate(vali);
        }
    }

    private class CrudRView implements ICrudView {

        private final IRecordView iView;
        private final RecordModel mo;

        CrudRView(final IRecordView v, RecordModel mo) {
            iView = v;
            this.mo = mo;
        }
    }

    public RecordModel getNew(AbstractTo beforea, AbstractTo pa) {
        AbstractTo aa = pa;
        if (aa == null) {
            aa = AbstractToFactory.getA(da);
        }
        RecordModel mo = new RecordModel(aParam.getAuxO(), aParam.getAuxO1());
        mo.setA(aa);
        mo.setBeforea(beforea);
        return mo;
    }

    public IRecordValidator getValidator() {
        return dFactory.getValidator(da);
    }

    private class ButtonClick implements IControlClick {

        private final ICrudAccept acc;
        private IRecordView v;
        private final RecordModel mo;
        private final int action;

        ButtonClick(ICrudAccept acc, RecordModel mo, int action) {
            this.acc = acc;
            this.mo = mo;
            this.action = action;
        }

        public void click(ContrButton co, Widget w) {
            if (co.getActionId() == IPersistAction.RESACTION) {
                v.hide();
                invalidate();
                return;
            }
            RecordModel a = getNew(mo.getA(), null);
            if (action != IPersistAction.ADDACION) {
                a.getA().copyFrom(mo.getA());
            }
            ex.toA(a, v);
            acc.accept(a);
        }

        /**
         * @param v
         *            the v to set
         */
        public void setV(IRecordView v) {
            this.v = v;
        }
    }

    private class setWidget implements ISetGwtWidget {

        public void setGwtWidget(IMvcWidget i) {
            setW = true;
            showDialog();
        }

    }

    public ICrudView getView(final RecordModel a, final ICrudAccept acc,
            final int actionId, final IPanel vp) {

        invalidate();
        if (aParam.getBSignal() != null) {
            aParam.getBSignal().signal(a);
        }
        List<RecordField> dict = getDef();
        String title = gFactory.getTitle(da);
        String aName = gFactory.getActionName(actionId);
        MvcWindowSize mSize = gFactory.getSize(da);
        IRecordDef model = RecordDefFactory.getRecordDef(rI, aName + " / "
                + title, dict, mSize);

        IRecordView v;
        if (vp == null) {
            IContrPanel aButton;
            switch (actionId) {
            case IPersistAction.DELACTION:
                aButton = bFactory.getRecordDelButt();
                break;
            default:
                aButton = bFactory.getRecordAkcButt();
                break;
            }
            ButtonClick bu = new ButtonClick(acc, a, actionId);
            v = RecordViewFactory.getRecordViewDialog(rI, new setWidget(), da,
                    model, aButton, bu, aParam.getAuxV(), aParam.getAuxW());
            bu.setV(v);
        } else {
            v = RecordViewFactory.getRecordView(rI, new setWidget(), da, model,
                    aParam.getAuxV(), vp);
        }
        if (a != null) {
            ex.toView(v, a);
            v.changeMode(actionId);
            if (aParam.getAuxV() != null) {
                aParam.getAuxV().changeMode(actionId);
            }
            return new CrudRView(v, a);
        } else {
            RecordModel mo = new RecordModel(aParam.getAuxO(), aParam
                    .getAuxO1());
            return new CrudRView(v, mo);
        }
    }

    public IPersistRecord getPersist() {
        return PersistRecordFactory.getPersistDict(rI, da);
    }

    public ICrudRecordControler getControler() {
        return new CrudDictRecordControler();
    }
}
