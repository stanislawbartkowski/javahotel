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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType.IEnumType;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.ImageNameFactory;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

/**
 * @author hotel
 * 
 */
class PresentationEditCellFactory extends PresentationEditCellHelper {

    private final PresentationTable pTable;
    private final PresentationDateEditFactory dFactory;
    private final PresentationImageChooseFactory iFactory;

    interface IStartEditRow {

        void setEditRow(MutableInteger row);
    }

    void setModel(IGwtTableModel model) {
        setGModel(model);
        dFactory.setGModel(model);
        iFactory.setGModel(model);
    }

    /**
     * @return the errorInfo
     */
    ErrorLineInfo getErrorInfo() {
        return errorInfo;
    }

    // default visibility
    void setErrorLineInfo(MutableInteger key,
            InvalidateFormContainer errContainer, IToRowNo i) {
        errorInfo.active = true;
        errorInfo.key = key;
        errorInfo.errContainer = errContainer;
        errorInfo.i = i;
        table.redrawRow(i.row(key));
    }

    PresentationEditCellFactory(ILostFocusEdit lostFocus,
            CellTable<MutableInteger> table, IStartEditRow iStartEdit,
            PresentationTable pTable) {
        super(new ErrorLineInfo(), table, lostFocus, new EditableCol(),
                iStartEdit);
        this.pTable = pTable;
        this.dFactory = new PresentationDateEditFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit);
        iFactory = new PresentationImageChooseFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit);
    }

    // Decorator patter implemented to add "blur" event to the constructor
    // Cannot inherit CheckboxCell directly
    private class EditCheckBoxCell extends
            AbstractEditableCell<Boolean, Boolean> implements IGetField {

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
            afterChange(type, context, v);
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

    private class EditSelectionCell extends SelectionCell implements IGetField {

        private final IVField v;
        @SuppressWarnings("unused")
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
            afterChange(eventType, context, v);
        }
    }

    class NumberViewState {

        Number num = null;
    }

    private class EditNumberCell extends
            AbstractInputCell<Number, NumberViewState> implements IGetField {

        private final IVField v;
        private final VListHeaderDesc he;
        @SuppressWarnings("unused")
        private final SafeHtmlRenderer<String> renderer;
        @SuppressWarnings("unused")
        private final NumberFormat format;
        private final NumberCell noEditCell;
        private final int afterDot;

        EditNumberCell(VListHeaderDesc he, NumberCell noEditCell, int afterDot) {
            super(BrowserEvents.CHANGE, BrowserEvents.KEYPRESS);
            this.v = he.getFie();
            this.he = he;
            renderer = SimpleSafeHtmlRenderer.getInstance();
            this.format = NumberFormat.getFormat(getNumberFormat(afterDot));
            this.noEditCell = noEditCell;
            this.afterDot = afterDot;
        }

        @Override
        public Object getValObj(MutableInteger key) {
            NumberViewState n = this.getViewData(key);
            if ((n == null) || (n.num == null)) {
                return null;
            }
            switch (v.getType().getType()) {
            case INT:
                Integer i = n.num.intValue();
                return i;
            case LONG:
                Long l = n.num.longValue();
                return l;
            default:
                return new BigDecimal(n.num.doubleValue());
            }

        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            NumberViewState num = new NumberViewState();
            if (o != null) {
                switch (v.getType().getType()) {
                case INT:
                    Integer i = (Integer) o;
                    num.num = i;
                    break;
                case LONG:
                    Long l = (Long) o;
                    num.num = l;
                    break;
                default:
                    num.num = (Number) o;
                    break;
                }
            }
            this.setViewData(key, num);
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                Number value, NativeEvent event,
                ValueUpdater<Number> valueUpdater) {
            if (isEditing(context, parent, value)) {
                setEditLine(context);
            }
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            if (eventType.equals(BrowserEvents.CHANGE)) {
                InputElement e = super.getInputElement(parent).cast();
                String s = e.getValue();
                Object key = context.getKey();
                NumberViewState num = new NumberViewState();
                if (CUtil.EmptyS(s)) {
                    setViewData(key, num); // empty value
                } else {
                    num.num = Utils.toBig(s, afterDot);
                    setViewData(key, num);
                }
            }
            afterChange(eventType, context, v);
            if (eventType.equals(BrowserEvents.KEYPRESS)) {
                removeErrorStyle();
            }
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
            NumberViewState num = this.getViewData(key);
            if (num == null) {
                num = new NumberViewState();
                num.num = value;
            }
            String val = null;
            if (num.num != null) {
                val = Utils.DecimalToS(new BigDecimal(num.num.doubleValue()), v
                        .getType().getAfterdot());
            }
            addInputSb(sb, i, val, he);
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
            EditNumberCell ce = new EditNumberCell(he, iCell, 0);
            co = new GLongColumn(ce, v);
            break;
        case INT:
            EditNumberCell cce = new EditNumberCell(he, iCell, 0);
            co = new GIntegerColumn(cce, v);
            break;
        case BIGDECIMAL:
            switch (v.getType().getAfterdot()) {
            case 0:
                EditNumberCell ec = new EditNumberCell(he, iCell, 0);
                co = new GLongColumn(ec, v);
                break;
            case 1:
                EditNumberCell ce1 = new EditNumberCell(he, nCell1, 1);
                co = new NumberColumn(ce1, v);
                break;
            case 2:
                EditNumberCell ce2 = new EditNumberCell(he, nCell2, 2);
                co = new NumberColumn(ce2, v);
                break;
            case 3:
                EditNumberCell ce3 = new EditNumberCell(he, nCell3, 3);
                co = new NumberColumn(ce3, v);
                break;
            default:
                EditNumberCell ce4 = new EditNumberCell(he, nCell4, 4);
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
        return dFactory.constructDateEditCol(he);
    }

    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v, boolean handleSelection) {
        return new CheckBoxColumn(new EditCheckBoxCell(v, handleSelection), v);
    }

    @SuppressWarnings("rawtypes")
    Column constructEditTextCol(VListHeaderDesc he) {
        IVField v = he.getFie();
        IEnumType e = v.getType().getE();
        IColumnImageSelect cSelect = he.getiColSelect();
        Column co;
        if (cSelect != null) {
            co = iFactory.construct(he);
        } else {
            if (e != null) {
                co = new TColumnEdit(v,
                        new EditSelectionCell(he, e.getValues()));
            } else {
                co = new TColumnEdit(v, new EditStringCell(he));
            }
        }
        return co;
    }

    @SuppressWarnings("incomplete-switch")
    private SafeHtml createSafeForImage(PersistTypeEnum persist) {
        String imageName = null;
        String title = null;
        switch (persist) {
        case ADDBEFORE:
            imageName = ImageNameFactory
                    .getImageName(ImageNameFactory.ImageType.ADDBEFOREROW);
            title = MM.getL().AddRowAtTheBeginning();
            break;
        case ADD:
            imageName = ImageNameFactory
                    .getImageName(ImageNameFactory.ImageType.ADDROW);
            title = MM.getL().AddRowAfter();
            break;
        case REMOVE:
            imageName = ImageNameFactory
                    .getImageName(ImageNameFactory.ImageType.DELETEROW);
            title = MM.getL().RemoveRow();
            break;
        }
        String s = Utils.getImageHTML(imageName, IConsts.actionImageHeight,
                IConsts.actionImageWidth);
        // add div to have them vertically

        SafeHtml html = SafeHtmlUtils.fromTrustedString("<div title=\"" + title
                + "\" >" + s + "</div>");
        return html;
    }

    private class ButtonImageCell extends ActionCell<MutableInteger> {

        private final PersistTypeEnum persist;

        ButtonImageCell(PersistTypeEnum persist,
                ActionCell.Delegate<MutableInteger> delegate) {
            super("", delegate);
            this.persist = persist;
        }

        @Override
        public void render(Context context, MutableInteger value,
                SafeHtmlBuilder sb) {
            // add div to have them vertically
            SafeHtml html = createSafeForImage(persist);
            sb.append(html);
        }
    }

    @SuppressWarnings("rawtypes")
    private class HasCellImage implements HasCell {

        private final PersistTypeEnum persist;

        HasCellImage(PersistTypeEnum persist) {
            this.persist = persist;
        }

        private class Delegate implements ActionCell.Delegate<MutableInteger> {

            @Override
            public void execute(MutableInteger object) {
                if (model.getRowEditAction() != null) {
                    WSize w = pTable.getRowWidget(object.intValue());
                    model.getRowEditAction().action(w, object.intValue(),
                            persist);
                }
            }
        }

        @Override
        public Cell getCell() {
            return new ButtonImageCell(persist, new Delegate());
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Column constructControlColumn() {
        // return new ImageColumn();

        List<HasCell> ce = new ArrayList<HasCell>();
        // TODO: blocked now, re-think the usage
        // ce.add(new
        // HasCellImage(ImageNameFactory.getImageName(ImageNameFactory.ImageType.CHANGEROW),
        // PersistTypeEnum.MODIF));
        ce.add(new HasCellImage(PersistTypeEnum.REMOVE));
        ce.add(new HasCellImage(PersistTypeEnum.ADD));
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

    private class ElemContainer {

        Element elem;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    void addActionColumn() {
        Column imColumn = constructControlColumn();

        Cell headercell = new ClickableTextCell() {
            @Override
            public void render(Cell.Context context, SafeHtml value,
                    SafeHtmlBuilder sb) {
                SafeHtml html = createSafeForImage(PersistTypeEnum.ADDBEFORE);
                sb.append(html);
            }
        };

        final ElemContainer elemX = new ElemContainer();
        Header head = new Header(headercell) {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void onBrowserEvent(Cell.Context context, Element elem,
                    NativeEvent event) {
                elemX.elem = elem;
                super.onBrowserEvent(context, elem, event);
            }
        };
        head.setUpdater(new ValueUpdater<String>() {
            @Override
            public void update(String value) {
                if (model.getRowEditAction() != null) {
                    WSize w = new WSize(elemX.elem);
                    model.getRowEditAction().action(w, 0,
                            PersistTypeEnum.ADDBEFORE);
                }
            }
        });

        table.insertColumn(0, imColumn, head);
    }
}
