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
package com.gwtmodel.table.view.table;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.PresentationEditCellFactory.IStartEditRow;

/**
 * @author hotel
 *
 */
abstract class PresentationEditCellHelper extends PresentationCellHelper {

    protected final ErrorLineInfo errorInfo;
    protected final CellTable<MutableInteger> table;
    protected final ILostFocusEdit lostFocus;
    protected final EditableCol eCol;
    private final IStartEditRow iStartEdit;

    protected static class ErrorLineInfo {

        boolean active = false;
        MutableInteger key;
        InvalidateFormContainer errContainer;
        IToRowNo i;
    }

    // default visibility
    interface IGetField {

        Object getValObj(MutableInteger key);

        void setValObj(MutableInteger key, Object o);
    }

    // cannot be private
    interface InputTemplate extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\" style=\"{1}\" class=\"{2}\"></input>")
        SafeHtml input(String value, String style, String classC);
    }

    // cannot be private
    interface TemplateDisplay extends SafeHtmlTemplates {

        @Template("{0}")
        SafeHtml input(String value);
    }
    private InputTemplate templateInput = GWT.create(InputTemplate.class);
    protected TemplateDisplay templateDisplay = GWT
            .create(TemplateDisplay.class);

    PresentationEditCellHelper(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol, IStartEditRow iStartEdit) {
        this.errorInfo = errorInfo;
        this.table = table;
        this.lostFocus = lostFocus;
        this.eCol = eCol;
        this.iStartEdit = iStartEdit;
    }

    interface IToRowNo {

        int row(MutableInteger key);
    }

    private String getS(String value) {
        return value == null ? "" : value;
    }

    protected void addInputSb(SafeHtmlBuilder sb, MutableInteger i,
            String value, VListHeaderDesc he) {
        String sClass = null;
        if (errorInfo.active && i.intValue() == errorInfo.key.intValue()) {
            InvalidateMess me = errorInfo.errContainer.findV(he.getFie());
            if (me != null) {
                sClass = IConsts.errorStyle + " " + getS(he.getInputClass());
            }
        }
        if (sClass == null) {
            sClass = getS(he.getInputClass());
        }
        sb.append(templateInput.input(getS(value), getS(he.getInputStyle()),
                sClass));
    }

    protected void removeErrorStyle() {
        if (errorInfo.active) {
            errorInfo.active = false;
            int rowno = errorInfo.i.row(errorInfo.key);
            if (rowno == -1) {
                return;
            }
            NodeList<TableCellElement> cList = table.getRowElement(rowno)
                    .getCells();
            for (int i = 0; i < cList.getLength(); i++) {
                TableCellElement el = cList.getItem(i);
                Element elex = el.getFirstChildElement();
                assert elex != null : LogT.getT().cannotBeNull();
                Element ele = elex.getFirstChildElement();
                // assert ele != null : LogT.getT().cannotBeNull();
                // it is possible for a cell do not have inner element
                if (ele == null) {
                    continue;
                }
                // assuming that it is HTML describing inner cell
                String cl = ele.getClassName();
                int x = cl.indexOf(IConsts.errorStyle);
                if (x != -1) {
                    // remove error class attribute
                    cl = cl.replace(IConsts.errorStyle, "");
                    ele.setClassName(cl);
                }
            }
        }
    }

    protected void modifUpdate(Object key, IVField v) {
        if (lostFocus != null) {
            MutableInteger i = (MutableInteger) key;
            lostFocus.action(i.intValue(), v);
        }
    }

    protected void afterChange(String eventType, Context context, IVField v) {
        if (eventType.equals(BrowserEvents.CHANGE)) {
            modifUpdate(context.getKey(), v);
        }
    }

    protected void setEditLine(Context context, WSize w) {
        Object key = context.getKey();
        MutableInteger i = (MutableInteger) key;
        if (iStartEdit != null) {
            iStartEdit.setEditRow(i, w);
        }
    }

    protected class TColumnEdit extends Column<MutableInteger, String> {

        private final IVField iF;

        TColumnEdit(IVField iF, Cell<String> ce) {
            super(ce);
            this.iF = iF;
        }

        @Override
        public String getValue(MutableInteger object) {
            IVModelData v = model.getRows().get(object.intValue());
            String s = FUtils.getValueS(v, iF);
            if (s == null) {
                return "";
            }
            return s;
        }
    }

    protected class EditStringCell extends TextInputCell implements IGetField {

        protected final IVField v;
        protected final VListHeaderDesc he;

        EditStringCell(VListHeaderDesc he) {
            this.v = he.getFie();
            this.he = he;
        }

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            ViewData viewData = getViewData(key);
            if (viewData != null && viewData.getCurrentValue().equals(value)) {
                clearViewData(key);
                viewData = null;
            }
            String s = (viewData != null) ? viewData.getCurrentValue() : value;
            if (editenabled) {
                addInputSb(sb, i, s, he);
                // super.render(context, value, sb);
                return;
            }
            // Get the view data.
            // copy and paste from TextInputCell
            // the only difference is different HTML for displaying value
            if (s != null) {
                sb.append(templateDisplay.input(s));
            } else {
                sb.appendHtmlConstant("");
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                String value, NativeEvent event,
                ValueUpdater<String> valueUpdater) {
            if (isEditing(context, parent, value)) {
                setEditLine(context, new WSize(parent));
            }
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            afterChange(eventType, context, v);
            // only because KEYUP is consumed in TextInputCell
            if (eventType.equals(BrowserEvents.KEYUP)) {
                removeErrorStyle();
            }
        }

        @Override
        public Object getValObj(MutableInteger key) {
            ViewData viewData = getViewData(key);
            if (viewData == null) {
                return null;
            }
            return viewData.getCurrentValue();
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            String s = (String) o;
            ViewData v = new ViewData(s);
            this.setViewData(key, v);
        }
    }
}
