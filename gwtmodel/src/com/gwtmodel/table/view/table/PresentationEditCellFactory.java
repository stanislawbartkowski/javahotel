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

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType.IEnumType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.ImageNameFactory;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.util.ClickPopUp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hotel
 *
 */
class PresentationEditCellFactory extends PresentationCellHelper {

    private final EditableCol eCol = new EditableCol();
    private final ILostFocusEdit lostFocus;
    private final CellTable<MutableInteger> table;

    interface IGetField {

        Object getValObj(MutableInteger key);

        void setValObj(MutableInteger key, Object o);
    }

    // cannot be private
    interface TemplateDisplay extends SafeHtmlTemplates {

        @Template("{0}")
        SafeHtml input(String value);
    }
    // cannot be private

    interface InputTemplate extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\" style=\"{1}\" class=\"{2}\"></input>")
        SafeHtml input(String value, String style, String classC);
    }
    private TemplateDisplay templateDisplay = GWT.create(TemplateDisplay.class);
    private InputTemplate templateInput = GWT.create(InputTemplate.class);

    private String getS(String value) {
        return value == null ? "" : value;
    }

    private void addInputSb(SafeHtmlBuilder sb, String value, VListHeaderDesc he) {
        sb.append(
                templateInput.input(
                getS(value),
                getS(he.getInputStyle()),
                getS(he.getInputClass())));
    }

    PresentationEditCellFactory(ILostFocusEdit lostFocus,
            CellTable<MutableInteger> table) {
        this.lostFocus = lostFocus;
        this.table = table;
    }

    // Decorator patter implemented to add "blur" event to the constructor
    // Cannot inherit CheckboxCell directly
    private class EditCheckBoxCell extends AbstractEditableCell<Boolean, Boolean> implements IGetField {

        private final IVField v;
        private final CheckboxCell checkBox;

        EditCheckBoxCell(IVField v, boolean handleSelection) {
            super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN,
                    BrowserEvents.BLUR);
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
        public Object getValObj(MutableInteger key) {
            Boolean b = checkBox.getViewData(key);
            return b;
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
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
            lostFocus(type, context, v);
        }

        @Override
        public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
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
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
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
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
        }

        @Override
        public Object getValObj(MutableInteger key) {
            Boolean b = this.getViewData(key);
            return b;
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            Boolean b = (Boolean) o;
            this.setViewData(key, b);
        }
    }

    /**
     * Creates list to be used as an argument to constructor
     *
     * @param c1 First element
     * @param c2 Second element
     * @return List containing two elements
     */
    private List<HasCell<Date, ?>> createList(HasCell<Date, ?> c1,
            HasCell<Date, ?> c2) {
        List<HasCell<Date, ?>> rList = new ArrayList<HasCell<Date, ?>>();
        rList.add(c1);
        rList.add(c2);
        return rList;
    }

    private class CellPickerDate extends CompositeCell<Date> implements
            IGetField {

        private final EditDateCell dCell;

        public CellPickerDate(HasEditDateCell dateCell,
                HasDateButtonCellImage cellImage) {
            super(createList(dateCell, cellImage));
            this.dCell = (EditDateCell) dateCell.getCell();
        }

        @Override
        public Object getValObj(MutableInteger key) {
            return dCell.getValObj(key);
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            dCell.setValObj(key, o);
        }
    }

    private class HasDateButtonCellImage implements HasCell {

        private final IVField v;
        private final DateButtonImageCell bCell;
        private final HasEditDateCell hasDate;

        HasDateButtonCellImage(IVField v, HasEditDateCell hasDate) {
            this.v = v;
            bCell = new DateButtonImageCell(new Delegate(), v);
            this.hasDate = hasDate;
        }

        class Delegate implements ActionCell.Delegate<Date> {

            WSize lastRendered = null;
            Context lastContext = null;

            private class DatePickerChanger implements ValueChangeHandler<Date> {

                ClickPopUp pUp;

                @Override
                public void onValueChange(ValueChangeEvent<Date> event) {

                    Date date = event.getValue();
                    EditDateCell eCell = (EditDateCell) hasDate.getCell();
                    if (lastContext != null) {
                        eCell.setValObj((MutableInteger) lastContext.getKey(),
                                date);
                        table.redrawRow(lastContext.getIndex());
                    }
                    pUp.setVisible(false);
                }
            }

            @Override
            public void execute(Date object) {
                if (lastRendered == null) {
                    return;
                }
                DatePicker dPicker = new DatePicker();
                if (object != null) {
                    dPicker.setValue(object);
                }
                DatePickerChanger pChanger = new DatePickerChanger();

                dPicker.addValueChangeHandler(pChanger);
                pChanger.pUp = new ClickPopUp(lastRendered, dPicker);
                pChanger.pUp.setVisible(true);
            }
        }

        @Override
        public Cell getCell() {
            return bCell;
        }

        @Override
        public FieldUpdater getFieldUpdater() {
            return null;
        }

        @Override
        public Object getValue(Object object) {
            return object;
        }
    }

    private class DateButtonImageCell extends ActionCell<Date> {

        private final IVField v;
        private final HasDateButtonCellImage.Delegate delegate;

        DateButtonImageCell(HasDateButtonCellImage.Delegate delegate, IVField v) {
            super("", delegate);
            this.v = v;
            this.delegate = delegate;
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, Date value,
                NativeEvent event, ValueUpdater<Date> valueUpdater) {
            delegate.lastRendered = new WSize(parent);
            delegate.lastContext = context;
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
        }

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            if (editenabled) {
                String s = Utils.getImageHTML(ImageNameFactory.getImageName(ImageNameFactory.ImageType.CALENDAR));
                SafeHtml html = SafeHtmlUtils.fromTrustedString(s);
                sb.append(html);
            }
        }
    }

    private class HasEditDateCell implements HasCell {

        private final EditDateCell iCell;

        HasEditDateCell(VListHeaderDesc he, DateTimeFormat format) {
            iCell = new EditDateCell(he, format);
        }

        @Override
        public Cell getCell() {
            return iCell;
        }

        @Override
        public FieldUpdater getFieldUpdater() {
            return null;
        }

        @Override
        public Object getValue(Object object) {
            return object;
        }
    }

    private class EditDateCell extends AbstractInputCell<Date, Date> implements
            IGetField {

        private final IVField v;
        private final VListHeaderDesc he;
        private final DateTimeFormat format;

        EditDateCell(VListHeaderDesc he, DateTimeFormat format) {
            super(BrowserEvents.CHANGE);
            this.v = he.getFie();
            this.he = he;
            this.format = format;
        }

        @Override
        public Object getValObj(MutableInteger key) {
            return getViewData(key);
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            this.setViewData(key, (Date) o);
        }

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            Date d = getViewData(key);
            if (d == null) {
                d = value;
            }
            if (editenabled) {
                String val = null;
                if (d != null) {
                    val = format.format(d);
                }
                addInputSb(sb, val, he);
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent, Date value,
                NativeEvent event, ValueUpdater<Date> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            if (eventType.equals(BrowserEvents.CHANGE)) {
                InputElement e = super.getInputElement(parent).cast();
                String s = e.getValue();
                Object key = context.getKey();
                Date d;
                try {
                    if (CUtil.EmptyS(s)) {
                        d = null;
                    } else {
                        d = format.parse(s);
                    }
                    setViewData(key, d);
                } catch (IllegalArgumentException ee) {
                }
            }
            lostFocus(eventType, context, v);
        }
    }

    private class xEditDateCell extends DatePickerCell implements IGetField {

        private final IVField v;
        private final DateTimeFormat format;

        xEditDateCell(IVField v, DateTimeFormat format) {
            super(format);
            this.v = v;
            this.format = format;
        }

        private class myUpdater implements ValueUpdater<Date> {

            private final ValueUpdater<Date> updater;
            private final Object key;

            myUpdater(ValueUpdater<Date> updater, Object key) {
                this.updater = updater;
                this.key = key;
            }

            @Override
            public void update(Date value) {
                if (updater != null) {
                    updater.update(value);
                }
                modifUpdate(key, v);
            }
        }

        /**
         * Override this method to avoid popping up date picker if not enable
         * edit mode
         */
        @Override
        public void onBrowserEvent(Context context, Element parent, Date value,
                NativeEvent event, ValueUpdater<Date> valueUpdater) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            // block date picker if not edit mode
            myUpdater myU = new myUpdater(valueUpdater, context.getKey());
            if (BrowserEvents.CLICK.equals(event.getType())) {
                if (editenabled) {
                    super.onBrowserEvent(context, parent, value, event, myU);
                }
            } else {
                super.onBrowserEvent(context, parent, value, event, myU);
            }
        }

        @Override
        public void render(Context context, Date value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
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
        public Object getValObj(MutableInteger key) {
            return getViewData(key);
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            this.setViewData(key, (Date) o);
        }
    }

    private class TColumnEdit extends Column<MutableInteger, String> {

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

    private void modifUpdate(Object key, IVField v) {
        if (lostFocus != null) {
            MutableInteger i = (MutableInteger) key;
            lostFocus.action(i.intValue(), v);
        }
    }

    private void lostFocus(String eventType, Context context, IVField v) {
        if (eventType.equals(BrowserEvents.BLUR)) {
            modifUpdate(context.getKey(), v);
        }
    }

    private class EditStringCell extends TextInputCell implements IGetField {

        private final IVField v;
        private final VListHeaderDesc he;

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
                super.render(context, value, sb);
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
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            lostFocus(eventType, context, v);
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

    private class EditSelectionCell extends SelectionCell implements IGetField {

        private final IVField v;
        private final VListHeaderDesc he;

        /**
         * @param options
         */
        public EditSelectionCell(VListHeaderDesc he, List<String> options) {
            super(options);
            this.v = he.getFie();
            this.he = he;
        }

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            if (editenabled) {
                super.render(context, value, sb);
                return;
            }
            String s = this.getViewData(key);
            if (s == null) {
                s = value;
            }
            sb.append(templateDisplay.input(s));
        }

        @Override
        public Object getValObj(MutableInteger key) {
            String s = this.getViewData(key);
            if (s == null) {
                return null;
            }
            return FUtils.getValue(v, s);
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            String s = FUtils.getValueOS(o, v);
            this.setViewData(key, s);
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                String value, NativeEvent event,
                ValueUpdater<String> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            lostFocus(eventType, context, v);
        }
    }

    private class EditNumberCell extends AbstractInputCell<Number, Number>
            implements IGetField {

        private final IVField v;
        private final VListHeaderDesc he;
        private final SafeHtmlRenderer<String> renderer;
        private final NumberFormat format;
        private final NumberCell noEditCell;

        EditNumberCell(VListHeaderDesc he, NumberFormat format, NumberCell noEditCell) {
            super(BrowserEvents.CHANGE);
            this.v = he.getFie();
            this.he = he;
            renderer = SimpleSafeHtmlRenderer.getInstance();
            this.format = format;
            this.noEditCell = noEditCell;
        }

        @Override
        public Object getValObj(MutableInteger key) {
            Number n = this.getViewData(key);
            if (n == null) {
                return null;
            }
            switch (v.getType().getType()) {
                case INT:
                    Integer i = n.intValue();
                    return i;
                case LONG:
                    Long l = n.longValue();
                    return l;
                default:
                    return new BigDecimal(n.doubleValue());
            }

        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            switch (v.getType().getType()) {
                case INT:
                    Integer i = (Integer) o;
                    this.setViewData(key, i);
                    break;
                case LONG:
                    Long l = (Long) o;
                    this.setViewData(key, l);
                    break;
                default:
                    Number n = (Number) o;
                    this.setViewData(key, n);
                    break;
            }
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                Number value, NativeEvent event,
                ValueUpdater<Number> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            if (eventType.equals(BrowserEvents.CHANGE)) {
                InputElement e = super.getInputElement(parent).cast();
                String s = e.getValue();
                Object key = context.getKey();
                try {
                    Number num = format.parse(s);
                    setViewData(key, num);
                } catch (NumberFormatException ee) {
                    Number num = this.getViewData(key);
                    String prevs = "";
                    if (num != null) {
                        prevs = format.format(num);
                    }
                    e.setValue(prevs);
                }
            }
            lostFocus(eventType, context, v);
        }

        @Override
        public void render(Context context, Number value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            if (!editenabled) {
                // sb.append(renderer.render(format.format(value)));
                noEditCell.render(context, value, sb);
                return;
            }
            Number num = this.getViewData(key);
            if (num == null) {
                num = value;
            }
            String val = null;
            if (num != null) {
                val = format.format(num);
            }
            addInputSb(sb, val, he);
        }
    }

    void setEditable(ChangeEditableRowsParam eParam) {
        eCol.addEditData(eParam);
    }

    public ChangeEditableRowsParam geteParam() {
        return eCol.geteParam();
    }

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(VListHeaderDesc he) {
        Column co = null;
        IVField v = he.getFie();
        switch (v.getType().getType()) {
            case LONG:
                EditNumberCell ce = new EditNumberCell(he,
                        NumberFormat.getFormat(getNumberFormat(0)), iCell);
                co = new GLongColumn(ce, v);
                break;
            case INT:
                EditNumberCell cce = new EditNumberCell(he,
                        NumberFormat.getFormat(getNumberFormat(0)), iCell);
                co = new GIntegerColumn(cce, v);
                break;
            case BIGDECIMAL:
                switch (v.getType().getAfterdot()) {
                    case 0:
                        EditNumberCell ec = new EditNumberCell(he,
                                NumberFormat.getFormat(getNumberFormat(0)), iCell);
                        co = new GLongColumn(ec, v);
                        break;
                    case 1:
                        EditNumberCell ce1 = new EditNumberCell(he,
                                NumberFormat.getFormat(getNumberFormat(1)), nCell1);
                        co = new NumberColumn(ce1, v);
                        break;
                    case 2:
                        EditNumberCell ce2 = new EditNumberCell(he,
                                NumberFormat.getFormat(getNumberFormat(2)), nCell2);
                        co = new NumberColumn(ce2, v);
                        break;
                    case 3:
                        EditNumberCell ce3 = new EditNumberCell(he,
                                NumberFormat.getFormat(getNumberFormat(3)), nCell3);
                        co = new NumberColumn(ce3, v);
                        break;
                    default:
                        EditNumberCell ce4 = new EditNumberCell(he,
                                NumberFormat.getFormat(getNumberFormat(4)), nCell4);
                        co = new NumberColumn(ce4, v);
                        break;
                }
                break;
            default:
                assert false : LogT.getT().notExpected();
        }
        return co;

    }

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(VListHeaderDesc he) {
        HasEditDateCell c1 = new HasEditDateCell(he, fo);
        HasDateButtonCellImage c2 = new HasDateButtonCellImage(he.getFie(), c1);
        CellPickerDate ceCell = new CellPickerDate(c1, c2);
        return new DateColumn(he.getFie(), ceCell);
    }

    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v, boolean handleSelection) {
        return new CheckBoxColumn(new EditCheckBoxCell(v, handleSelection), v);
    }

    @SuppressWarnings("rawtypes")
    Column constructEditTextCol(VListHeaderDesc he) {
        IVField v = he.getFie();
        IEnumType e = v.getType().getE();
        Column co;
        if (e != null) {
            co = new TColumnEdit(v, new EditSelectionCell(he, e.getValues()));
        } else {
            co = new TColumnEdit(v, new EditStringCell(he));
        }
        return co;
    }

    private class ButtonImageCell extends ActionCell<MutableInteger> {

        private final String imageName;

        ButtonImageCell(String imageName,
                ActionCell.Delegate<MutableInteger> delegate) {
            super("", delegate);
            this.imageName = imageName;
        }

        @Override
        public void render(Context context, MutableInteger value,
                SafeHtmlBuilder sb) {
            String s = Utils.getImageHTML(imageName, 12, 12);
            // add div to have them vertically
            SafeHtml html = SafeHtmlUtils.fromTrustedString("<div>" + s
                    + "</div>");
            sb.append(html);
        }
    }

    private class HasCellImage implements HasCell {

        private final String imageName;
        private final PersistTypeEnum persist;

        HasCellImage(String imageName, PersistTypeEnum persist) {
            this.imageName = imageName;
            this.persist = persist;
        }

        private class Delegate implements ActionCell.Delegate<MutableInteger> {

            @Override
            public void execute(MutableInteger object) {
                if (model.getRowEditAction() != null) {
                    model.getRowEditAction().action(object.intValue(), persist);
                }
            }
        }

        @Override
        public Cell getCell() {
            return new ButtonImageCell(imageName, new Delegate());
        }

        @Override
        public FieldUpdater getFieldUpdater() {
            return null;
        }

        @Override
        public Object getValue(Object object) {
            return object;
        }
    }

    @SuppressWarnings("rawtypes")
    Column constructControlColumn() {
        // return new ImageColumn();

        List<HasCell> ce = new ArrayList<HasCell>();
        ce.add(new HasCellImage(ImageNameFactory.getImageName(ImageNameFactory.ImageType.ADDROW), PersistTypeEnum.ADD));
// TODO: blocked now rethink usage        
//        ce.add(new HasCellImage(ImageNameFactory.getImageName(ImageNameFactory.ImageType.CHANGEROW), PersistTypeEnum.MODIF));
        ce.add(new HasCellImage(ImageNameFactory.getImageName(ImageNameFactory.ImageType.DELETEROW), PersistTypeEnum.REMOVE));
        CompositeCell<MutableInteger> cCell = new CompositeCell(ce);
        Column<MutableInteger, MutableInteger> imageColumn = new Column<MutableInteger, MutableInteger>(
                cCell) {
            @Override
            public MutableInteger getValue(MutableInteger object) {
                return object;
            }
        };
        return imageColumn;

    }
}
