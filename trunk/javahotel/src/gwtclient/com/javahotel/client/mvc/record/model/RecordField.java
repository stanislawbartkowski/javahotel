/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.record.model;

import com.javahotel.client.ifield.ILineField;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */

public class RecordField {

    private final String pLabel;
    private final ILineField eLine;
    private final IField fie;
    private boolean canChange;
    private boolean alwaysReadOnly;

    public RecordField(final String p, final ILineField e,
            final IField fie, final boolean canChange) {
        this.pLabel = p;
        this.eLine = e;
        this.fie = fie;
        this.canChange = canChange;
        this.alwaysReadOnly = false;
    }

    public RecordField(final String p, final ILineField e,
            final IField fie) {
        this.pLabel = p;
        this.eLine = e;
        this.fie = fie;
        this.canChange = false;
        this.alwaysReadOnly = true;
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
    public ILineField getELine() {
        return eLine;
    }

    /**
     * @return the fie
     */
    public IField getFie() {
        return fie;
    }

    /**
     * @return the canChange
     */
    public boolean isCanChange() {
        return canChange;
    }

    /**
     * @param canChange the canChange to set
     */
    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }

    /**
     * @return the alwaysReadOnly
     */
    public boolean isAlwaysReadOnly() {
        return alwaysReadOnly;
    }


}

