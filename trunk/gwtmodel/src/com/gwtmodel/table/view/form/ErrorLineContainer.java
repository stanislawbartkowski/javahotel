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
package com.gwtmodel.table.view.form;

import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.rdef.FormField;
import java.util.ArrayList;
import java.util.List;

class ErrorLineContainer {

    /** Contains all fields with error messages. */
    private List<FormField> el = new ArrayList<FormField>();

    /**
     * Clear error messages list
     */
    void initErr() {
        el.clear();
    }

    /**
     * Apply error message nad CSS style to fiekd
     * 
     * @param re
     *            Field where error should be attached to
     * @param m
     *            Error
     */
    void setEMess(FormField re, InvalidateMess m) {
        re.getELine().setGStyleName(IConsts.errorStyle, true);
        String e = m.getErrmess();
        re.getELine().setInvalidMess(e);
        // remember the field with error message
        el.add(re);
    }

    /**
     * Remove all error messages and error style
     */
    void clearE() {
        for (FormField re : el) {
            re.getELine().setGStyleName(IConsts.errorStyle, false);
            re.getELine().setInvalidMess(null);
        }
        initErr();
    }
}
