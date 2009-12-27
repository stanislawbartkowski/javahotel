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
package com.javahotel.nmvc.common;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.javahotel.client.ifield.ILineField;

public class FormLineDef implements IFormLineView  {
    
    private final ILineField iField;
    
    public FormLineDef(ILineField f) {
        iField = f;
    }

    public void addChangeListener(IFormChangeListener cListener) {        
    }

    public String getVal() {
        return iField.getVal();
    }

    public void setVal(String s) {
        iField.setVal(s);
    }

    public Widget getWidget() {
        return iField.getMWidget().getWidget();
    }

    public void setReadOnly(boolean readOnly) {
        iField.setReadOnly(readOnly);
        
    }

}
