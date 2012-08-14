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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.FieldDataType.IEnumType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hotel
 *
 */
class PresentationCellFactory {

    private final IGetCellValue getCell;
    private final ILostFocusEdit lostFocus;

    PresentationCellFactory(IGetCellValue getCell, ILostFocusEdit lostFocus) {
        this.getCell = getCell;
        this.lostFocus = lostFocus;
    }

    interface IGetField {

        Object getValObj(Integer key);

        void setValObj(Integer key, Object o);
    }
    private final EditableCol eCol = new EditableCol();
    private final IGetCustomValues cValues = GwtGiniInjector.getI()
            .getTableFactoriesContainer().getGetCustomValues();
    private final DateTimeFormat fo = DateTimeFormat.getFormat(cValues
            .getCustomValue(IGetCustomValues.DATEFORMAT));
    private IGwtTableModel model;
    private final NumberCell iCell = new NumberCell(
            NumberFormat.getFormat(getNumberFormat(0)));
    private final NumberCell nCell1 = new NumberCell(
            NumberFormat.getFormat(getNumberFormat(1)));
    private final NumberCell nCell2 = new NumberCell(
            NumberFormat.getFormat(getNumberFormat(2)));
    private final NumberCell nCell3 = new NumberCell(
            NumberFormat.getFormat(getNumberFormat(3)));
    private final NumberCell nCell4 = new NumberCell(
            NumberFormat.getFormat(getNumberFormat(4)));
    // private final CheckboxCell editcheckCell = new EditableCheckBoxCell();
    private final Cell<Boolean> checkCell = new NoEditCheckBoxCell();
    private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils
            .fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" disabled=\"disabled\" checked/>");
    private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils
            .fromSafeConstant("<input type=\"checkbox\" disabled=\"disabled\" tabindex=\"-1\"/>");

    // Decorator patter implemented to add "blur" event to the contructor
    // Cannot inherit CheckboxCell directly
    private class EditCheckBoxCell extends AbstractEditableCell<Boolean, Boolean> implements IGetField {

        private final IVField v;
        private final CheckboxCell checkBox;

        EditCheckBoxCell(IVField v, boolean handleSelection) {
            super("change", "keydown", "blur");
            this.checkBox = new CheckboxCell(false, handleSelection);
            this.v = v;
        }

        @Override
        public boolean dependsOnSelection() {
            return checkBox.dependsOnSelection();
        }

        @Override
        public boolean handlesSelection() {
            return checkBox.handlesSelection();
        }

        @Override
        public Object getValObj(Integer key) {
            Boolean b = checkBox.getViewData(key);
            return b;
        }

        @Override
        public void setValObj(Integer key, Object o) {
            Boolean b = (Boolean) o;
            checkBox.setViewData(key, b);
        }

        @Override
        public boolean isEditing(
                com.google.gwt.cell.client.Cell.Context context,
                Element parent, Boolean value) {
            return checkBox.isEditing(context, parent, value);
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                Boolean value, NativeEvent event,
                ValueUpdater<Boolean> valueUpdater) {
            String type = event.getType();
            checkBox.onBrowserEvent(context, parent, value, event, valueUpdater);
            if (type.equals("blur")) {
                if (lostFocus != null) {
                    Object key = context.getKey();
                    Integer i = (Integer) key;
                    lostFocus.action(i, v);
                }
            }
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                Boolean value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            if (editenabled) {
                checkBox.render(context, value, sb);
                return;
            }

            Boolean viewData = getViewData(key);
            if (viewData != null && viewData.equals(value)) {
                clearViewData(key);
                viewData = null;
            }
            if (value) {
                sb.append(INPUT_CHECKED);
            } else {
                sb.append(INPUT_UNCHECKED);
            }

        }
    }

    // TODO: remove
    private class EditableCheckBoxCell extends CheckboxCell implements
            IGetField {

        private final IVField v;

        EditableCheckBoxCell(IVField v, boolean handleSelection) {
            // second parameter controls selection.
            // but after setting to true Selecion does not work
            // needs further investigation
            // TODO: think over later
            super(false, handleSelection);
            this.v = v;
        }

        @Override
        public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
            // Get the view data.
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            if (editenabled) {
                super.render(context, value, sb);
                return;
            }

            Boolean viewData = getViewData(key);
            if (viewData != null && viewData.equals(value)) {
                clearViewData(key);
                viewData = null;
            }
            if (value) {
                sb.append(INPUT_CHECKED);
            } else {
                sb.append(INPUT_UNCHECKED);
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                Boolean value, NativeEvent event,
                ValueUpdater<Boolean> valueUpdater) {
            String type = event.getType();
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
        }

        @Override
        public Object getValObj(Integer key) {
            Boolean b = this.getViewData(key);
            return b;
        }

        @Override
        public void setValObj(Integer key, Object o) {
            Boolean b = (Boolean) o;
            this.setViewData(key, b);
        }
    }

    private class NoEditCheckBoxCell extends AbstractCell<Boolean> {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                Boolean value, SafeHtmlBuilder sb) {
            if (value) {
                sb.append(INPUT_CHECKED);
            } else {
                sb.append(INPUT_UNCHECKED);
            }
        }
    }

    private String getNumberFormat(int afterdot) {
        String defaFormat;
        String parName;
        switch (afterdot) {
            case 0:
                defaFormat = "####0";
                parName = IGetCustomValues.NUMBERFORMAT0;
                break;
            case 1:
                defaFormat = "##########0.0";
                parName = IGetCustomValues.NUMBERFORMAT1;
                break;
            case 2:
                defaFormat = "##########0.00";
                parName = IGetCustomValues.NUMBERFORMAT2;
                break;
            case 3:
                defaFormat = "##########0.000";
                parName = IGetCustomValues.NUMBERFORMAT3;
                break;
            default:
                defaFormat = "##########0.0000";
                parName = IGetCustomValues.NUMBERFORMAT4;
                break;
        }
        String forma = cValues.getCustomValue(parName);
        if (forma == null) {
            forma = defaFormat;
        }
        return forma;
    }

    /**
     * @param model the model to set
     */
    void setModel(IGwtTableModel model) {
        this.model = model;
    }

    interface TemplateDisplay extends SafeHtmlTemplates {

        @Template("{0}")
        SafeHtml input(String value);
    }

    interface TemplateButtonAction extends SafeHtmlTemplates {

        @Template("<strong>{0}</strong>")
        SafeHtml input(String value);
    }

    private class EditSelectionCell extends SelectionCell implements IGetField {

        private final IVField v;

        /**
         * @param options
         */
        public EditSelectionCell(List<String> options, IVField v) {
            super(options);
            this.v = v;
        }

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            if (editenabled) {
                super.render(context, value, sb);
                return;
            }
            String s = this.getViewData(key);
            if (s == null) {
                s = value;
            }
            TemplateDisplay template = GWT.create(TemplateDisplay.class);
            sb.append(template.input(s));
        }

        @Override
        public Object getValObj(Integer key) {
            String s = this.getViewData(key);
            if (s == null) {
                return null;
            }
            return FUtils.getValue(v, s);
        }

        @Override
        public void setValObj(Integer key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            String s = FUtils.getValueOS(o, v);
            this.setViewData(key, s);
        }
    }

    private class EditStringCell extends TextInputCell implements IGetField {

        private final IVField v;

        EditStringCell(IVField v) {
            this.v = v;
        }

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            if (editenabled) {
                super.render(context, value, sb);
                return;
            }
            // Get the view data.
            // copy and paste from TextInputCell
            // the only difference is different HTML for displaying value
            TemplateDisplay template = GWT.create(TemplateDisplay.class);
            ViewData viewData = getViewData(key);
            if (viewData != null && viewData.getCurrentValue().equals(value)) {
                clearViewData(key);
                viewData = null;
            }
            String s = (viewData != null) ? viewData.getCurrentValue() : value;
            if (s != null) {
                sb.append(template.input(s));
            } else {
                sb.appendHtmlConstant("");
            }
        }

        @Override
        public Object getValObj(Integer key) {
            ViewData viewData = getViewData(key);
            if (viewData == null) {
                return null;
            }
            return viewData.getCurrentValue();
        }

        @Override
        public void setValObj(Integer key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            String s = (String) o;
            ViewData v = new ViewData(s);
            this.setViewData(key, v);
        }
    }

    private class EditDateCell extends DatePickerCell implements IGetField {

        private final IVField v;
        private final DateTimeFormat format;

        EditDateCell(IVField v, DateTimeFormat format) {
            super(format);
            this.v = v;
            this.format = format;
        }

        /**
         * Override this method to avoid popping up date picker if not enable
         * edit mode
         */
        @Override
        public void onBrowserEvent(Context context, Element parent, Date value,
                NativeEvent event, ValueUpdater<Date> valueUpdater) {
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            // block date picker if not edit mode
            if ("click".equals(event.getType())) {
                if (editenabled) {
                    super.onBrowserEvent(context, parent, value, event,
                            valueUpdater);
                }
            } else {
                super.onBrowserEvent(context, parent, value, event,
                        valueUpdater);
            }
        }

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            Integer i = (Integer) key;
            boolean editenabled = eCol.isEditable(i, v);
            if (editenabled) {
                super.render(context, value, sb);
                return;
            }
            // Get the view data.
            // copy and paste from TextInputCell
            // the only difference is different HTML for displaying value
            Date d = getViewData(key);
            if (d == null) {
                d = value;
            }
            if (d != null) {
                sb.appendEscaped(format.format(d));
            }
        }

        @Override
        public Object getValObj(Integer key) {
            return getViewData(key);
        }

        @Override
        public void setValObj(Integer key, Object o) {
            this.setViewData(key, (Date) o);
        }
    }

    private class TColumn extends TextColumn<Integer> {

        private final IVField iF;

        TColumn(IVField iF) {
            this.iF = iF;
        }

        @Override
        public String getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            return FUtils.getValueS(v, iF);
        }
    }

    private class TColumnEdit extends Column<Integer, String> {

        private final IVField iF;

        TColumnEdit(IVField iF, Cell<String> ce) {
            super(ce);
            this.iF = iF;
        }

        @Override
        public String getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            String s = FUtils.getValueS(v, iF);
            if (s == null) {
                return "";
            }
            return s;
        }
    }

    private class DateColumn extends Column<Integer, Date> {

        private final IVField v;

        DateColumn(IVField v, Cell<Date> ce) {
            super(ce);
            this.v = v;
        }

        @Override
        public Date getValue(Integer object) {
            IVModelData vData = model.getRows().get(object);
            return FUtils.getValueDate(vData, v);
        }
    }

    private class LongColumn extends Column<Integer, Number> {

        private final IVField v;

        LongColumn(IVField v) {
            super(iCell);
            this.v = v;
        }

        @Override
        public Long getValue(Integer object) {
            IVModelData vData = model.getRows().get(object);
            return FUtils.getValueLong(vData, v);

        }
    }

    private abstract class NumberColumn extends Column<Integer, Number> {

        private final IVField v;

        NumberColumn(NumberCell n, IVField v) {
            super(n);
            this.v = v;
        }

        @Override
        public Number getValue(Integer object) {
            IVModelData vData = model.getRows().get(object);
            BigDecimal b = FUtils.getValueBigDecimal(vData, v);
            return b;
        }
    }

    private class NumberColumn1 extends NumberColumn {

        NumberColumn1(IVField v) {
            super(nCell1, v);
        }
    }

    private class NumberColumn2 extends NumberColumn {

        NumberColumn2(IVField v) {
            super(nCell2, v);
        }
    }

    private class NumberColumn3 extends NumberColumn {

        NumberColumn3(IVField v) {
            super(nCell3, v);
        }
    }

    private class NumberColumn4 extends NumberColumn {

        NumberColumn4(IVField v) {
            super(nCell4, v);
        }
    }

    private class ActionButtonCell extends ActionCell<Integer> {

        @SuppressWarnings("unused")
        private final String buttonString;
        private final IVField iF;

        ActionButtonCell(String buttonString, IVField iF, FieldDataType fType,
                ActionCell.Delegate<Integer> aCell) {
            super("", aCell);
            this.buttonString = buttonString;
            this.iF = iF;
        }

        @Override
        public void render(Cell.Context context, Integer value,
                SafeHtmlBuilder sb) {
            IVModelData v = model.getRows().get(value);
            if (getCell != null) {
                SafeHtml sa = getCell.getValue(v, iF);
                if (sa != null) {
                    sb.append(sa);
                    return;
                }
            }
            // sb.appendHtmlConstant("<strong>");
            String s = FUtils.getValueS(v, iF);
            if (s == null) {
                return;
            }
            // sb.appendEscaped(s);
            // sb.appendHtmlConstant("</strong>");

            TemplateButtonAction template = GWT
                    .create(TemplateButtonAction.class);
            sb.append(template.input(s));
        }
    }

    private class ButtonColumn extends Column<Integer, Integer> {

        public ButtonColumn(ActionButtonCell cell) {
            super(cell);
        }

        @Override
        public Integer getValue(Integer object) {
            return object;
        }
    }

    private class CheckBoxColumn extends Column<Integer, Boolean> {

        private final IVField iF;

        CheckBoxColumn(Cell<Boolean> cell, IVField iF) {
            super(cell);
            this.iF = iF;
        }

        @Override
        public Boolean getValue(Integer object) {
            IVModelData vData = model.getRows().get(object);
            Boolean b = FUtils.getValueBoolean(vData, iF);
            return b;
        }
    }

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(IVField v, boolean editable) {
        Column co = null;
        switch (v.getType().getType()) {
            case LONG:
                co = new LongColumn(v);
                break;
            case BIGDECIMAL:
                switch (v.getType().getAfterdot()) {
                    case 0:
                        co = new LongColumn(v);
                        break;
                    case 1:
                        co = new NumberColumn1(v);
                        break;
                    case 2:
                        co = new NumberColumn2(v);
                        break;
                    case 3:
                        co = new NumberColumn3(v);
                        break;
                    default:
                        co = new NumberColumn4(v);
                        break;
                }
                break;
            default:
                assert false : LogT.getT().notExpected();
        }
        return co;

    }

    @SuppressWarnings("rawtypes")
    Column constructEditCol(IVField v, boolean editable) {

        Column co;
        if (editable) {
            IEnumType e = v.getType().getE();
            if (e != null) {
                co = new TColumnEdit(v, new EditSelectionCell(e.getValues(), v));
            } else {
                co = new TColumnEdit(v, new EditStringCell(v));
            }
        } else {
            co = new TColumn(v);
        }
        return co;
    }

    Column contructBooleanCol(IVField v, boolean editable,
            boolean handleSelection) {
        if (editable) {
            return new CheckBoxColumn(new EditCheckBoxCell(v, handleSelection),
                    v);
        }
        return new CheckBoxColumn(checkCell, v);

    }

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(IVField v, boolean editable) {
        if (editable) {
            return new DateColumn(v, new EditDateCell(v, fo));
        }
        return new DateColumn(v, new DateCell(fo));
    }

    @SuppressWarnings("rawtypes")
    Column constructActionButtonCell(String buttonString, IVField iF,
            FieldDataType fType, ActionCell.Delegate<Integer> aCell) {
        return new ButtonColumn(new ActionButtonCell(buttonString, iF, fType,
                aCell));
    }

    void setEditable(ChangeEditableRowsParam eParam) {
        eCol.addEditData(eParam);
    }

    Cell constructCell(IVField v) {
        Cell ce;
        switch (v.getType().getType()) {
            case DATE:
                ce = new DateCell(fo);
                break;
            case LONG:
            case INT:
                ce = iCell;
                break;
            case BIGDECIMAL:
                switch (v.getType().getAfterdot()) {
                    case 0:
                        ce = iCell;
                        break;
                    case 1:
                        ce = nCell1;
                        break;
                    case 2:
                        ce = nCell2;
                        break;
                    case 3:
                        ce = nCell3;
                        break;
                    default:
                        ce = nCell4;
                        break;
                }
                break;
            case BOOLEAN:
                return checkCell;
            default:
                ce = new TextCell();
                break;
        }
        return ce;
    }
}
