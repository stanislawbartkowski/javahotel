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
package com.gwtmodel.table.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.rdef.IFormLineView;

public class ErrorLineContainer {

    private List<IFormLineView> el = new ArrayList<IFormLineView>();

    private void initErr() {
        el.clear();
    }

    public void setEMess(IFormLineView i, InvalidateMess m) {
        i.setGStyleName(IConsts.EMPTYCLASSSTYLE, true);
        String e = m.getErrmess();
        i.setInvalidMess(e);
        el.add(i);
    }

    public void clearE() {
        for (IFormLineView i : el) {
            i.setGStyleName(IConsts.EMPTYCLASSSTYLE, false);
            i.setInvalidMess(null);
        }
        initErr();
    }
}
