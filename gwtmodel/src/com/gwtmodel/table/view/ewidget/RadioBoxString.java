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
package com.gwtmodel.table.view.ewidget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.LogT;
import com.javahotel.client.MM;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class RadioBoxString extends AbstractField {

    private final VerticalPanel vP = new VerticalPanel();
    private SyncC sync;
    private final List<CC> aL = new ArrayList<CC>();
    private final IGetDataList iGet;

    private void isetReadOnly(final boolean readOnly) {
        for (CC c : aL) {
            c.c.setEnabled(!readOnly);
        }
    }

    @SuppressWarnings("deprecation")
    private void isetVal(final List<String> col) {
        for (CC c : aL) {
            boolean is = false;
            for (final String s : col) {
                if (s.equals(c.dName)) {
                    is = true;
                }
            }
            c.c.setChecked(is);
        }
    }

    private class SyncC extends SynchronizeList {

        private boolean waitforenable = false;
        private boolean waitforsetval = false;
        private boolean enable;
        private List<String> val;

        void setEnable(boolean enable) {
            waitforenable = true;
            this.enable = enable;
            dowaiting();
        }

        void setVal(List<String> col) {
            waitforsetval = true;
            this.val = col;
            dowaiting();
        }

        private void dowaiting() {
            if (!signalledAlready()) {
                return;
            }
            if (waitforenable) {
                isetReadOnly(!enable);
                waitforenable = false;
            }
            if (waitforsetval) {
                isetVal(val);
                waitforsetval = false;
            }
        }

        SyncC() {
            super(1);
        }

        @Override
        protected void doTask() {
            dowaiting();
        }
    }

    private class CC {

        final String dName;
        final CheckBox c;

        CC(final String p1, final CheckBox c1) {
            this.dName = p1;
            this.c = c1;
        }
    }

    private void setRadio(IDataListType dataList, boolean enable) {
        IVField sym = tFactories.getGetCustomValues().getSymForCombo();
        assert sym != null : LogT.getT().cannotBeNull();
        for (IVModelData vv : dataList.getList()) {
            String s = FUtils.getValueS(vv, sym);
            CheckBox c = new CheckBox(s);
            c.setEnabled(enable);
            CC cc = new CC(s, c);
            aL.add(cc);
            vP.add(c);
        }
        sync.signalDone();
    }

    private class R implements IGetDataListCallBack {

        private final boolean enable;

        R(final boolean penable) {
            this.enable = penable;
        }

        @Override
        public void set(IDataListType dataList) {
            setRadio(dataList, enable);
        }
    }

    RadioBoxString(ITableCustomFactories tFactories, IVField v,
            IGetDataList iGet, final boolean enable) {
        super(tFactories, v);
        this.iGet = iGet;
        sync = new SyncC();
        sync.setEnable(enable);
        refresh();
        initWidget(vP);
    }

    @Override
    public Object getValObj() {
        return null;
    }

    @Override
    public void setValObj(Object o) {
    }

    public final void refresh() {
        sync.reset();
        aL.clear();
        vP.clear();
        iGet.call(new R(false));
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        sync.setEnable(!readOnly);
    }

    public boolean validateField() {
        return true;
    }

    public void setValues(final List<String> col) {
        sync.setVal(col);
    }

    @SuppressWarnings("deprecation")
    public List<String> getValues() {
        List<String> co = new ArrayList<String>();
        for (CC c : aL) {
            if (c.c.isChecked()) {
                co.add(c.dName);
            }
        }
        return co;
    }

    @Override
    public Widget getGWidget() {
        return this;
    }
}
