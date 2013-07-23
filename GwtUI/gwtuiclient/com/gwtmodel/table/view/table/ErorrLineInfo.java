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

import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.view.table.PresentationEditCellHelper.IToRowNo;

class ErrorLineInfo {

    private boolean active = false;
    private MutableInteger key;
    private InvalidateFormContainer errContainer;
    private IToRowNo i;

    boolean isActive() {
        return active;
    }

    MutableInteger getKey() {
        return key;
    }

    InvalidateFormContainer getErrContainer() {
        return errContainer;
    }

    IToRowNo getI() {
        return i;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    void setKey(MutableInteger key) {
        this.key = key;
    }

    void setErrContainer(InvalidateFormContainer errContainer) {
        this.errContainer = errContainer;
    }

    void setI(IToRowNo i) {
        this.i = i;
    }

    boolean isErrorLine(MutableInteger i) {
        return (active && i.intValue() == key.intValue());
    }

}