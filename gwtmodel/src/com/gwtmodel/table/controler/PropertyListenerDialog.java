/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controler.ui.ChangePageSizeDialog;
import com.gwtmodel.table.controler.ui.WrapOnOffDialog;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.listdataview.ActionTableSignal;
import com.gwtmodel.table.listdataview.DataIntegerSignal;
import com.gwtmodel.table.listdataview.IsBooleanSignalNow;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.PopUpTip;
import com.gwtmodel.table.view.util.PopupCreateMenu;

/**
 * @author hotel
 * 
 */
class PropertyListenerDialog {

    private final IDataType ddType;

    PropertyListenerDialog(IDataType ddType) {
        this.ddType = ddType;
    }

    private boolean isTreeEnabled(ISlotable publishSlo) {
        CustomStringSlot slType = ActionTableSignal
                .constructSlotGetTableTreeEnabled(ddType);
        return TUtil.isBoolProp(publishSlo, slType);
    }

    private boolean isTreeSorted(ISlotable publishSlo) {
        CustomStringSlot slType = ActionTableSignal
                .constructSlotGetTableIsSorted(ddType);
        return TUtil.isBoolProp(publishSlo, slType);
    }

    private boolean isFilterOn(ISlotable publishSlo) {
        CustomStringSlot slType = ActionTableSignal
                .constructSlotGetTableIsFilter(ddType);
        return TUtil.isBoolProp(publishSlo, slType);
    }

    private boolean isWrapOn(ISlotable publishSlo) {
        CustomStringSlot slType = IsBooleanSignalNow
                .constructSlotGetLineWrap(ddType);
        return TUtil.isBoolProp(publishSlo, slType);
    }

    private int getPageSize(ISlotable publishSlo) {
        CustomStringSlot slType = ActionTableSignal
                .constructGetPageSizeSignal(ddType);
        ISlotSignalContext rContext = publishSlo.getSlContainer()
                .getGetterCustom(slType);
        DataIntegerSignal ret = (DataIntegerSignal) rContext.getCustom();
        return ret.getValue();
    }

    private class PropertyListener implements ISlotListener {

        private final ISlotable publishSlo;
        private final IDataType publishdType;
        private final static String CHANGE_TO_TREE = "CHANGE_TO_TREE";
        private final static String CHANGE_TO_TABLE = "CHANGE_TO_TABLE";
        private final static String REMOVE_SORT = "REMOVE_SORT";
        private final static String CHANGE_PAGE_SIZE = "CHANGE_PAGE_SIZE";
        private final static String WRAP_LINE_ON = "WRAP_LINE_ON";

        PropertyListener(ISlotable publishSlo, IDataType publishdType) {
            this.publishSlo = publishSlo;
            this.publishdType = publishdType;
        }

        private class ChangeWrap implements WrapOnOffDialog.IChangeWrap {

            @Override
            public void action(boolean wrap) {
                CustomStringSlot slType = IsBooleanSignalNow
                        .constructSlotSetLineWrap(ddType);
                IsBooleanSignalNow sig = new IsBooleanSignalNow(wrap);
                publishSlo.getSlContainer().publish(slType, sig);
            }

        }

        private boolean onlyForTable(Widget w) {
            if (TUtil.isTreeView(publishSlo, ddType)) {
                OkDialog ok = new OkDialog(MM.getL().OnlyForTable(), null, null);
                ok.show(w);
                return false;
            }
            return true;
        }

        private boolean onlyForTree(Widget w) {
            if (!TUtil.isTreeView(publishSlo, ddType)) {
                OkDialog ok = new OkDialog(MM.getL().OnlyForTree(), null, null);
                ok.show(w);
                return false;
            }
            return true;
        }

        private void addMenu(List<ControlButtonDesc> mList, String id,
                String displayName) {
            ControlButtonDesc bu = new ControlButtonDesc(id, displayName);
            mList.add(bu);
        }

        private class ControlClick implements IControlClick {

            private PopupPanel up;

            private void closeP() {
                if (up != null) {
                    up.hide();
                }
            }

            @Override
            public void click(ControlButtonDesc co, Widget w) {
                p_click(co, w);
                closeP();
            }

            private class ChangePageAction implements
                    ChangePageSizeDialog.IChangeAction {

                private final int startSize;

                ChangePageAction(int startSize) {
                    this.startSize = startSize;
                }

                private void setSize(int size) {
                    SlotType sl = ActionTableSignal
                            .constructSetPageSizeSignal(ddType);
                    DataIntegerSignal si = new DataIntegerSignal(size);
                    publishSlo.getSlContainer().publish(sl, si);

                }

                @Override
                public void toDefault() {
                    setSize(startSize);
                }

                @Override
                public void toChange(int newsize) {
                    setSize(newsize);
                }

            }

            public void p_click(ControlButtonDesc co, Widget w) {
                String id = co.getActionId().getCustomButt();
                if (id.equals(WRAP_LINE_ON)) {
                    if (!onlyForTable(w)) {
                        return;
                    }
                    WrapOnOffDialog.doDialog(new WSize(w),
                            isWrapOn(publishSlo), new ChangeWrap());
                    return;

                }
                if (id.equals(CHANGE_PAGE_SIZE)) {
                    if (!onlyForTable(w)) {
                        return;
                    }
                    VListHeaderContainer vHeader = SlU.getHeaderList(ddType,
                            publishSlo);
                    ChangePageSizeDialog.doDialog(new WSize(w),
                            vHeader.getPageSize(), getPageSize(publishSlo),
                            new ChangePageAction(vHeader.getPageSize()));
                    return;
                }
                if (id.equals(REMOVE_SORT)) {
                    if (!onlyForTable(w)) {
                        return;
                    }
                    if (!isTreeSorted(publishSlo)) {
                        OkDialog ok = new OkDialog(
                                MM.getL().TableIsNotSorted(), null, null);
                        ok.show(w);
                        return;
                    }
                    SlotType sl = ActionTableSignal
                            .constructRemoveSortSignal(ddType);
                    publishSlo.getSlContainer().publish(sl);
                    return;
                }
                if (id.equals(CHANGE_TO_TREE)) {
                    if (!onlyForTable(w)) {
                        return;
                    }
                    if (!isTreeEnabled(publishSlo)) {
                        OkDialog ok = new OkDialog(MM.getL()
                                .CannotDisplayAsTree(), null, null);
                        ok.show(w);
                        return;
                    }
                    if (isFilterOn(publishSlo)) {
                        OkDialog ok = new OkDialog(MM.getL()
                                .CannotSwitchToTreeWhileFilter(), null, null);
                        ok.show(w);
                        return;
                    }
                    SlotType sl = ActionTableSignal
                            .constructToTreeSignal(ddType);
                    publishSlo.getSlContainer().publish(sl);
                    return;
                }
                if (id.equals(CHANGE_TO_TABLE)) {
                    if (!onlyForTree(w)) {
                        return;
                    }
                    SlotType sl = ActionTableSignal
                            .constructToTableSignal(ddType);
                    publishSlo.getSlContainer().publish(sl);
                }
            }
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            List<ControlButtonDesc> mList = new ArrayList<ControlButtonDesc>();
            if (isTreeEnabled(publishSlo)) {
                addMenu(mList, MM.getL().ChangeToTable(), CHANGE_TO_TABLE);
                addMenu(mList, MM.getL().ChangeToTree(), CHANGE_TO_TREE);
            }
            addMenu(mList, MM.getL().RemoveSortOrder(), REMOVE_SORT);
            addMenu(mList, MM.getL().ChangeNumberOfRows(), CHANGE_PAGE_SIZE);
            addMenu(mList, MM.getL().WrapLines(), WRAP_LINE_ON);
            ListOfControlDesc coP = new ListOfControlDesc(mList);
            IGWidget wi = slContext.getGwtWidget();
            ControlClick co = new ControlClick();
            MenuBar mp = PopupCreateMenu.createMenu(coP, co, wi.getGWidget());
            co.up = PopUpTip.drawPopupTip(wi.getGWidget().getAbsoluteLeft(), wi
                    .getGWidget().getAbsoluteTop()
                    + wi.getGWidget().getOffsetHeight(), mp);
        }
    }

    ISlotListener constructPropertyListener(ISlotable publishSlo,
            IDataType publishdType) {
        return new PropertyListener(publishSlo, publishdType);
    }

}
