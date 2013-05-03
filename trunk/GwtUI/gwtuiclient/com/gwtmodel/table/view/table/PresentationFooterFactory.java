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

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

/**
 * @author hotel
 * 
 */
class PresentationFooterFactory {

    @SuppressWarnings("unused")
    private final PresentationCellFactory cFactory;
    private IVModelData footerV;
    private IGetStandardMessage iMess = GwtGiniInjector.getI()
            .getStandardMessage();

    PresentationFooterFactory(PresentationCellFactory cFactory) {
        this.cFactory = cFactory;
    }

    // center, left, right
    interface InputTemplate extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<p style=\"text-align:{0};\">{1}</p>")
        SafeHtml input(String align, String value);
    }

    private InputTemplate headerInput = GWT.create(InputTemplate.class);

    private SafeHtml getHtml(VListHeaderDesc.ColAlign align,
            FieldDataType dType, String value) {
        String ali = null;
        switch (AlignCol.getCo(align, dType)) {
        case LEFT:
            ali = "left";
            break;
        case CENTER:
            ali = "center";
            break;
        case RIGHT:
            ali = "right";
            break;
        }
        return headerInput.input(ali, value);
    }

    private class FooterH extends Header<SafeHtml> {

        private final VFooterDesc he;

        FooterH(VFooterDesc he) {
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
            return getHtml(he.getAlign(), he.getfType(), val);
        }

    }

    Header<?> constructHeader(VListHeaderDesc he) {
        String headerText = iMess.getMessage(he.getHeaderString());
        return new SafeHtmlHeader(getHtml(he.getAlign(), he.getFie().getType(),
                headerText));
    }

    Header<?> constructFooter(VFooterDesc he) {
        return new FooterH(he);
    }

    /**
     * @param footerV
     *            the footerV to set
     */
    void setFooterV(IVModelData footerV) {
        this.footerV = footerV;
    }

}
