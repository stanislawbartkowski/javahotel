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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.LogT;

/**
 * @author hotel
 * 
 */
class PresentationCellFactory extends PresentationCellHelper {

    private final IGetCellValue getCell;

    PresentationCellFactory(IGetCellValue getCell) {
        this.getCell = getCell;
    }

    void setModel(IGwtTableModel model) {
        setGModel(model);
    }

    IGwtTableModel getModel() {
        return model;
    }

    private final Cell<Boolean> checkCell = new NoEditCheckBoxCell();

    private class NoEditCheckBoxCell extends AbstractCell<Boolean> {

        @Override
        public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
            if (value) {
                sb.append(INPUT_CHECKED);
            } else {
                sb.append(INPUT_UNCHECKED);
            }
        }
    }

    // cannot be private
    interface TemplateButtonAction extends SafeHtmlTemplates {

        @Template("<strong>{0}</strong>")
        SafeHtml input(String value);
    }

    // cannot be private

    interface TemplateEmptyButtonAction extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<strong>&nbsp;&nbsp;&nbsp;&nbsp;</strong>")
        SafeHtml input();
    }

    private class TColumn extends TextColumn<MutableInteger> {

        private final IVField iF;

        TColumn(IVField iF) {
            this.iF = iF;
        }

        @Override
        public String getValue(MutableInteger object) {
            IVModelData v = model.get(object.intValue());
            return FUtils.getValueS(v, iF);
        }
    }

    private class ActionButtonCell extends ActionCell<MutableInteger> {

        @SuppressWarnings("unused")
        private final String buttonString;
        private final IVField iF;

        ActionButtonCell(String buttonString, IVField iF, FieldDataType fType,
                ActionCell.Delegate<MutableInteger> aCell) {
            super("", aCell);
            this.buttonString = buttonString;
            this.iF = iF;
        }

        @Override
        public void render(Cell.Context context, MutableInteger value,
                SafeHtmlBuilder sb) {
            IVModelData v = model.get(value.intValue());
            if (getCell != null) {
                SafeHtml sa = getCell.getValue(v, iF);
                if (sa != null) {
                    sb.append(sa);
                    return;
                }
            }
            // sb.appendHtmlConstant("<strong>");
            String s = FUtils.getValueS(v, iF);
            if (CUtil.EmptyS(s)) {
                TemplateEmptyButtonAction template = GWT
                        .create(TemplateEmptyButtonAction.class);
                sb.append(template.input());
                return;
            }
            // sb.appendEscaped(s);
            // sb.appendHtmlConstant("</strong>");

            TemplateButtonAction template = GWT
                    .create(TemplateButtonAction.class);
            sb.append(template.input(s));
        }
    }

    private class ButtonColumn extends Column<MutableInteger, MutableInteger> {

        public ButtonColumn(ActionButtonCell cell) {
            super(cell);
        }

        @Override
        public MutableInteger getValue(MutableInteger object) {
            return object;
        }
    }

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(IVField v) {
        Column co = null;
        switch (v.getType().getType()) {
        case LONG:
            co = new LongColumn(v);
            break;
        case INT:
            co = new IntegerColumn(v);
            break;
        case BIGDECIMAL:
            switch (v.getType().getAfterdot()) {
            case 0:
                co = new LongColumn(v);
                break;
            case 1:
                co = new NumberColumn(nCell1, v);
                break;
            case 2:
                co = new NumberColumn(nCell2, v);
                break;
            case 3:
                co = new NumberColumn(nCell3, v);
                break;
            default:
                co = new NumberColumn(nCell4, v);
                break;
            }
            break;
        default:
            assert false : LogT.getT().notExpected();
        }
        return co;

    }

    @SuppressWarnings("rawtypes")
    Column constructTextCol(IVField v) {
        return new TColumn(v);
    }

    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v) {
        return new CheckBoxColumn(checkCell, v);
    }

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(IVField v) {
        return new DateColumn(v, new DateCell(fo));
    }

    @SuppressWarnings("rawtypes")
    Column constructActionButtonCell(String buttonString, IVField iF,
            FieldDataType fType, ActionCell.Delegate<MutableInteger> aCell) {
        return new ButtonColumn(new ActionButtonCell(buttonString, iF, fType,
                aCell));
    }

    @SuppressWarnings("rawtypes")
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
