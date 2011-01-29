/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.rdef;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

public class FormField {

    private final String pLabel;
    private final IFormLineView eLine;
    private final boolean readOnlyIfModif;
    private final IVField fRange;
    private String htmlId;
    private boolean disabled;

    public FormField(final String p, final IFormLineView e, final IVField fie,
            boolean readOnlyIfModif) {
        this.pLabel = p;
        if (e == null) {
            assert fie != null : LogT.getT().cannotBeNull();
            EditWidgetFactory eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
            this.eLine = eFactory.constructEditWidget(fie);
        } else {
            this.eLine = e;
        }
        this.readOnlyIfModif = readOnlyIfModif;
        this.fRange = null;
    }

    public FormField(final String p, final IFormLineView e, final IVField fie) {
        this(p, e, fie, false);
    }

    public FormField(final String p, final IFormLineView e) {
        this(p, e, null, false);
    }

    public FormField(final String p, final IVField fie) {
        this(p, null, fie, false);
    }

    public FormField(final String p, final IFormLineView e, final IVField fie, IVField fRange) {
        this.pLabel = p;
        this.eLine = e;
        this.readOnlyIfModif = false;
        this.fRange = fRange;
    }

    public boolean isReadOnlyIfModif() {
        return readOnlyIfModif;
    }

    /**
     * @return the pLabel
     */
    public String getPLabel() {
        return pLabel;
    }

    /**
     * @return the eLine
     */
    public IFormLineView getELine() {
        return eLine;
    }

    /**
     * @return the fie
     */
    public IVField getFie() {
        return eLine.getV();
    }

    public boolean isRange() {
        return fRange != null;
    }

    public IVField getFRange() {
        return fRange;

    }

    /**
     * @return the htmlId
     */
    public String getHtmlId() {
        return htmlId;
    }

    /**
     * @param htmlId the htmlId to set
     */
    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
