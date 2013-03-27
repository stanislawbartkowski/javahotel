/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.form;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.FormTabPanelDef;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.validate.ErrorLineContainer;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.util.CreateFormView;
import java.util.ArrayList;
import java.util.List;

class GwtFormView implements IGwtFormView {

    private final FormLineContainer fContainer;
    private Widget gg = null;
    final ErrorLineContainer eStore = new ErrorLineContainer();
    private HTMLPanel hp;
    private TabPanel tPanel = null;
    private IContrButtonView cList;

    private void setListener() {

        ITouchListener ii = new ITouchListener() {
            @Override
            public void onTouch() {
                eStore.clearE();
            }
        };
        for (FormField e : fContainer.getfList()) {
            e.getELine().setOnTouch(ii);
        }
    }

    private List<FormField> findFormCommon() {
        List<FormField> uList = new ArrayList<FormField>();
        for (FormField f : fContainer.getfList()) {
            if (f.getTabId() == null) {
                uList.add(f);
            }
        }
        return uList;
    }

    private void modifyButtonHtml() {
        if (cList == null) {
            return;
        }
        if (tPanel == null) {
            return;
        }
        int i = -1;
        for (FormTabPanelDef de : fContainer.getTabList()) {
            i++;
            if (de.getHtmlDef() == null) {
                continue;
            }
            Widget w = tPanel.getWidget(i);
            cList.fillHtml((HTMLPanel) w);
        }
    }

    private void createTabPanel() {
        tPanel = new TabPanel();
        Widget fUp = null;

        List<FormField> uList = findFormCommon();
        if (!uList.isEmpty()) {
            fUp = CreateFormView.construct(uList);
        }

        for (FormTabPanelDef de : fContainer.getTabList()) {
            List<FormField> fList = new ArrayList<FormField>();
            for (FormField f : fContainer.getfList()) {
                if (f.getTabId() == null) {
                    continue;
                }
                if (CUtil.EqNS(f.getTabId(), de.getTabId())) {
                    fList.add(f);
                }
            }
            Widget w;
            if (de.getHtmlDef() == null) {
                w = CreateFormView.construct(fList);
            } else {
                w = CreateFormView.setHtml(de.getHtmlDef(), fList);
            }
            tPanel.add(w, de.getTabTitle());
        }
        if (fUp == null) {
            gg = tPanel;
        } else {
            VerticalPanel vp = new VerticalPanel();
            vp.add(fUp);
            vp.add(tPanel);
            gg = vp;
        }
        modifyButtonHtml();
        tPanel.selectTab(0);
    }

    GwtFormView(ICallContext iContext, final FormLineContainer fContainer,
            IDataFormConstructorAbstractFactory.CType cType,
            final ISignal iSignal) {
        this.fContainer = fContainer;
        if (cType.getfConstructor() == null) {
            if (fContainer.isTabPanel()) {
                createTabPanel();
            } else if (fContainer.getHtml() == null) {
                gg = CreateFormView.construct(fContainer.getfList());
            } else {
                gg = CreateFormView.setHtml(fContainer.getHtml(),
                        fContainer.getfList());
            }
            iSignal.signal();
        } else {
            ISetGWidget iSet = new ISetGWidget() {
                @Override
                public void setW(IGWidget w) {
                    gg = w.getGWidget();
                    iSignal.signal();
                }
            };
            cType.getfConstructor().construct(iSet, iContext, fContainer);
        }
        hp = null;

        setListener();
    }

    @Override
    public Widget getGWidget() {
        if (hp == null) {
            return gg;
        }
        return hp;
    }

    @Override
    public void showInvalidate(InvalidateFormContainer errContainer) {
        List<InvalidateMess> col = errContainer.getErrMess();

        boolean something = false;
        for (InvalidateMess m : col) {
            IVField mFie = m.getFie();
            for (FormField re : fContainer.getfList()) {
                if ((mFie == null) || re.getFie().eq(mFie)) {
                    eStore.setEMess(re.getELine(), m);
                    something = true;
                }
            }
        }
        if (!something) {
            FormField re = fContainer.getfList().get(0);
            InvalidateMess m = col.get(0);
            eStore.setEMess(re.getELine(), m);
        }
        // move to the any tabpanel containing error field
        if (something && tPanel != null) {
            int act = tPanel.getTabBar().getSelectedTab();
            boolean invalidAct = false;
            int invTab = -1;
            for (InvalidateMess m : col) {
                IVField mFie = m.getFie();
                int i = -1;
                for (FormTabPanelDef de : fContainer.getTabList()) {
                    i++;
                    for (FormField f : fContainer.getfList()) {
                        if (!mFie.eq(f.getFie())) {
                            continue;
                        }
                        if (f.getTabId() == null) {
                            continue;
                        }
                        if (CUtil.EqNS(f.getTabId(), de.getTabId())) {
                            if (invTab == -1) {
                                invTab = i;
                            }
                            if (act == i) {
                                invalidAct = true;
                            }
                        }
                    }
                }
            }
            if (!invalidAct && invTab != -1) {
                tPanel.selectTab(invTab);
            }
        }
    }

    @Override
    public void fillHtml(IGWidget gw) {
        Widget w = gw.getGWidget();
        HTMLPanel pa = (HTMLPanel) w;
        hp = pa;
        if (tPanel == null) {
            CreateFormView.setHtml(pa, fContainer.getfList());
        } else {
            String htmlId = fContainer.getTabList().get(0).getTabId();
            CreateFormView.replace(pa, htmlId, tPanel);
            List<FormField> uList = findFormCommon();
            CreateFormView.setHtml(pa, uList);
        }
    }

    @Override
    public void setHtmlId(String id, IGWidget g) {
        if (gg instanceof HTMLPanel) {
            HTMLPanel pa = (HTMLPanel) gg;
            CreateFormView.replace(pa, id, g.getGWidget());
        }
        if (tPanel != null) {
            int i = -1;
            for (FormTabPanelDef de : fContainer.getTabList()) {
                i++;
                if (de.getHtmlDef() == null) {
                    continue;
                }
                Widget w = tPanel.getWidget(i);
                HTMLPanel pa = (HTMLPanel) w;
                CreateFormView.replace(pa, id, g.getGWidget());
            }
        }
    }

    public void setButtonList(IContrButtonView cList) {
        this.cList = cList;
        modifyButtonHtml();
    }

    public void changeToTab(String tabId) {
        if (tPanel == null) {
            return;
        }
        int i = -1;
        for (FormTabPanelDef de : fContainer.getTabList()) {
            i++;
            if (CUtil.EqNS(de.getTabId(), tabId)) {
                tPanel.selectTab(i);
                return;
            }
        }

    }
}
