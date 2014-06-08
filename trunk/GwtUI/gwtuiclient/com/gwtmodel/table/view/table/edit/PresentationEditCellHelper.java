/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.table.edit;

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
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.ILostFocusEdit;
import com.gwtmodel.table.view.table.util.EditableCol;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IGetField;
import com.gwtmodel.table.view.table.util.IStartEditRow;
import com.gwtmodel.table.view.table.util.PresentationCellHelper;

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

    interface InsertStyleAndClass {

        void set(String inputStyle, String inputClass);
    }

    protected void addInputSbI(MutableInteger i, VListHeaderDesc he,
            InsertStyleAndClass ins) {
        String sClass = null;
        if (errorInfo.isErrorLine(i)) {
            InvalidateMess me = errorInfo.getErrContainer().findV(he.getFie());
            if (me != null) {
                sClass = IConsts.errorStyle + " " + getS(he.getInputClass());
            }
        }
        if (sClass == null) {
            sClass = getS(he.getInputClass());
        }
        // sb.append(templateInput.input(getS(value), getS(he.getInputStyle()),
        // sClass));
        ins.set(getS(getS(he.getInputStyle())), getS(sClass));
    }

    protected void addInputSb(final SafeHtmlBuilder sb, MutableInteger i,
            final String value, VListHeaderDesc he) {
        addInputSbI(i, he, new InsertStyleAndClass() {

            @Override
            public void set(String inputStyle, String inputClass) {
                sb.append(templateInput.input(getS(value), inputStyle,
                        inputClass));
            }
        });
    }

    protected void removeErrorStyle() {
        if (errorInfo.isActive()) {
            errorInfo.setActive(false);
            int rowno = errorInfo.getI().row(errorInfo.getKey());
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

    protected void modifUpdate(boolean before, Object key, IVField v, WSize w) {
        if (lostFocus != null) {
            MutableInteger i = (MutableInteger) key;
            lostFocus.action(before, i.intValue(), v, w);
        }
    }

    protected void afterChange(String eventType, Context context, IVField v,
            WSize w) {
        if (eventType.equals(BrowserEvents.CHANGE)) {
            modifUpdate(false, context.getKey(), v, w);
        }
        if (eventType.equals(BrowserEvents.FOCUS)) {
            modifUpdate(true, context.getKey(), v, w);
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
            IVModelData v = model.get(object.intValue());
            String s = FUtils.getValueS(v, iF);
            if (s == null) {
                return "";
            }
            return s;
        }
    }

    protected interface ICustomEditStringRender {
        void render(SafeHtmlBuilder sb, MutableInteger i, String value);
    };

    protected class EditStringCell extends TextInputCell implements IGetField {

        protected final IVField v;
        protected final VListHeaderDesc he;

        EditStringCell(VListHeaderDesc he) {
            this.v = he.getFie();
            this.he = he;
        }

        protected void customRender(Context context, String value,
                SafeHtmlBuilder sb, ICustomEditStringRender iCustom) {
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
                // addInputSb(sb, i, s, he);
                // super.render(context, value, sb);
                iCustom.render(sb, i, s);
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
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            customRender(context, value, sb, new ICustomEditStringRender() {

                @Override
                public void render(SafeHtmlBuilder sb, MutableInteger i,
                        String value) {
                    addInputSb(sb, i, value, he);
                }
            });
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
            afterChange(eventType, context, v, new WSize(parent));
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
            ViewData vv = new ViewData(s);
            this.setViewData(key, vv);
        }
    }

    protected class EditNumberCell extends EditStringCell {

        EditNumberCell(VListHeaderDesc he) {
            super(he);
        }

        @Override
        public Object getValObj(MutableInteger key) {
            ViewData viewData = getViewData(key);
            if (viewData == null) {
                return null;
            }
            return FUtils.getValue(v, viewData.getCurrentValue());
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            String s = FUtils.getValueOS(o, v);
            super.setValObj(key, s);
        }

    }

}
