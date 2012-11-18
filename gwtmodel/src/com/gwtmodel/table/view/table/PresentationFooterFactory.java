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
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.cellview.client.TextHeader;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.PresentationEditCellHelper.InputTemplate;

/**
 * @author hotel
 * 
 */
class PresentationFooterFactory {

    private final PresentationCellFactory cFactory;
    private IVModelData footerV;

    PresentationFooterFactory(PresentationCellFactory cFactory) {
        this.cFactory = cFactory;
    }

    // center, left, right
    interface InputTemplate extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<p style=\"text-align:{0};\">{1}</p>")
        SafeHtml input(String align, String value);
    }

    private InputTemplate headerInput = GWT.create(InputTemplate.class);

    private SafeHtml getHtml(VListHeaderDesc he, String value) {
        String align = null;
        switch (AlignCol.getCo(he)) {
        case LEFT:
            align = "left";
            break;
        case CENTER:
            align = "center";
            break;
        case RIGHT:
            align = "right";
            break;
        }
        return headerInput.input(align, value);
    }

    private class FooterH extends Header<SafeHtml> {

        private final VListHeaderDesc he;

        FooterH(VListHeaderDesc he) {
            super(new SafeHtmlCell());
            this.he = he;
        }

        @Override
        public SafeHtml getValue() {
            if (footerV == null) {
                return null;
            }
            Object o = footerV.getF(he.getFie());
            String val = FUtils.getValueOS(o, he.getFie());
            return getHtml(he, val);
        }

    }

    private class StringFooter<T> extends Header<T> {

        private final VListHeaderDesc he;

        public StringFooter(Cell<T> cell, VListHeaderDesc he) {
            super(cell);
            this.he = he;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getValue() {
            if (footerV == null) {
                return null;
            }
            return (T) footerV.getF(he.getFie());
        }

    }

    // SafeHtmlHeader header = new SafeHtmlHeader(new SafeHtml() {

    // @Override
    // public String asString() {
    // return "<p style=\"text-align:center;\">My Column Header</p>";
    // }
    // });

    // myCellTable.addColumn( myCol, header);

    Header<?> XconstructHeader(VListHeaderDesc he) {
        return new TextHeader(he.getHeaderString());
    }

    Header<?> constructHeader(VListHeaderDesc he) {
        return new SafeHtmlHeader(getHtml(he, he.getHeaderString()));
    }

    Header<?> constructFooter(VListHeaderDesc he) {
        return new FooterH(he);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Header<?> OldconstructFooter(VListHeaderDesc he) {
        Header<?> hea = new StringFooter(cFactory.constructCell(he.getFie()),
                he);
        hea.setHeaderStyleNames("footer_right_align");
        SafeHtmlHeader header = new SafeHtmlHeader(new SafeHtml() {

            @Override
            public String asString() {
                return "<p style=\"text-align:center;\">My Column Header</p>";
            }
        });

        return header;
    }

    /**
     * @param footerV
     *            the footerV to set
     */
    void setFooterV(IVModelData footerV) {
        this.footerV = footerV;
    }

}
