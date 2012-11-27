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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
import com.gwtmodel.table.common.PersistTypeEnum;
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
    private final PresentationNumbEditFactory nFactory;
    private final PresentationCheckEditFactory checkFactory;
    private final PresentationImageButtonFactory imaFactory;

    interface IStartEditRow {

        void setEditRow(MutableInteger row);
    }

    void setModel(IGwtTableModel model) {
        setGModel(model);
        dFactory.setGModel(model);
        iFactory.setGModel(model);
        nFactory.setGModel(model);
        checkFactory.setGModel(model);
        imaFactory.setGModel(model);
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
            PresentationTable pTable,IColumnImage iIma) {
        super(new ErrorLineInfo(), table, lostFocus, new EditableCol(),
                iStartEdit);
        this.pTable = pTable;
        this.dFactory = new PresentationDateEditFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit);
        iFactory = new PresentationImageChooseFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit);
        nFactory = new PresentationNumbEditFactory(errorInfo, table, lostFocus,
                eCol, iStartEdit);
        checkFactory = new PresentationCheckEditFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit);
        imaFactory = new PresentationImageButtonFactory(errorInfo, table,
                lostFocus, eCol, iStartEdit,iIma);
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

    void setEditable(ChangeEditableRowsParam eParam) {
        eCol.addEditData(eParam);
    }

    public ChangeEditableRowsParam geteParam() {
        return eCol.geteParam();
    }

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(VListHeaderDesc he) {
        return nFactory.constructNumberCol(he);
    }

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(VListHeaderDesc he) {
        return dFactory.constructDateEditCol(he);
    }

    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v, boolean handleSelection) {
        return checkFactory.contructBooleanCol(v, handleSelection);
    }

    @SuppressWarnings("rawtypes")
    Column constructEditTextCol(VListHeaderDesc he) {
        IVField v = he.getFie();
        IEnumType e = v.getType().getE();
        IColumnImageSelect cSelect = he.getiColSelect();
        boolean isColImage = he.isImageCol();
        Column co;
        if (isColImage) {
            co = imaFactory.constructImageButton(he);
        } else if (cSelect != null) {
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
