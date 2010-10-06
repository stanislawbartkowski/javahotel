/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.idialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.SynchronizeList;
import com.javahotel.client.IResLocator;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ELineDialogMulChoice extends ELineDialog {

    private final VerticalPanel vP = new VerticalPanel();

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
    private SyncC sync;
    private final CommandParam pa;

    private class CC {

        final String dName;
        final CheckBox c;

        CC(final String p1, final CheckBox c1) {
            this.dName = p1;
            this.c = c1;
        }
    }
    private final List<CC> aL = new ArrayList<CC>();

    private class R implements RData.IVectorList {

        private final boolean enable;

        R(final boolean penable) {
            this.enable = penable;
        }

        public void doVList(final List<? extends AbstractTo> val) {
            for (final AbstractTo a : val) {
                DictionaryP p = (DictionaryP) a;
                CheckBox c = new CheckBox(p.getName());
                c.setEnabled(enable);
                CC cc = new CC(p.getName(), c);
                aL.add(cc);
                vP.add(c);
            }
            sync.signalDone();
        }
    }

    ELineDialogMulChoice(final IResLocator rI, final CommandParam p,
            final boolean enable, final boolean readynow) {
        super(rI);
        pa = p;
        sync = new SyncC();
        sync.setEnable(enable);
        if (readynow) {
            refresh();
        }
        initWidget(vP);
        setMouse();
    }

    ELineDialogMulChoice(final IResLocator rI, final CommandParam p,
            final boolean enable) {
        super(rI);
        pa = p;
        sync = new SyncC();
        sync.setEnable(enable);
        refresh();
        initWidget(vP);
        setMouse();
    }

    public String getVal() {
        return null;
    }

    public void setVal(final String s) {
    }

    public void refresh() {
        sync.reset();
        aL.clear();
        vP.clear();
        pLi.getR().getList(RType.ListDict, pa, new R(false));
    }

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
}
