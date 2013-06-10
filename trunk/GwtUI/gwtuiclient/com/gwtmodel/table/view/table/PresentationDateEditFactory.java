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
package com.gwtmodel.table.view.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.ImageNameFactory;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.PresentationEditCellFactory.IStartEditRow;
import com.gwtmodel.table.view.util.ClickPopUp;

/**
 * @author hotel
 *
 */
class PresentationDateEditFactory extends PresentationEditCellHelper {

    PresentationDateEditFactory(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol, IStartEditRow iStartEdit) {
        super(errorInfo, table, lostFocus, eCol, iStartEdit);
    }

    private class EditDateCell extends AbstractInputCell<Date, Date> implements
            IGetField {

        private final IVField v;
        private final VListHeaderDesc he;
        private final DateTimeFormat format;

        EditDateCell(VListHeaderDesc he, DateTimeFormat format) {
            super(BrowserEvents.CHANGE, BrowserEvents.KEYPRESS);
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
            String val = null;
            if (d != null) {
                val = format.format(d);
            }
            if (editenabled) {
                addInputSb(sb, i, val, he);
            } else {
                if (val != null) {
                    sb.append(templateDisplay.input(val));
                } else {
                    sb.appendHtmlConstant("");
                }

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
            if (eventType.equals(BrowserEvents.KEYPRESS)) {
                removeErrorStyle();
            }
            afterChange(eventType, context, v, new WSize(parent));
        }
    }

    @SuppressWarnings("rawtypes")
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

    @SuppressWarnings("rawtypes")
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
                        modifUpdate(false, lastContext.getKey(), v, null);
                        removeErrorStyle();
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
                String s = Utils.getImageHTML(ImageNameFactory
                        .getImageName(ImageNameFactory.ImageType.CALENDAR));
                SafeHtml html = SafeHtmlUtils.fromTrustedString(s);
                sb.append(html);
            }
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

        @SuppressWarnings("unchecked")
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

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(VListHeaderDesc he) {
        HasEditDateCell c1 = new HasEditDateCell(he, fo);
        HasDateButtonCellImage c2 = new HasDateButtonCellImage(he.getFie(), c1);
        CellPickerDate ceCell = new CellPickerDate(c1, c2);
        return new DateColumn(he.getFie(), ceCell);
    }
}
