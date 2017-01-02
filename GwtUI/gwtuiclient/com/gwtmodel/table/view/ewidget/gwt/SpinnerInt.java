/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.ewidget.gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.gwtmodel.table.IConsts;

class SpinnerInt extends TextBoxBase {

    public static native InputElement createInputElement(Document doc,
            String type, String min, String max) /*-{
                                                 var e = doc.createElement("INPUT");
                                                 e.type = type;
                                                 e.min = min;
                                                 e.max = max;
                                                 return e;
                                                 }-*/;

    SpinnerInt(int min, int max) {
        super(createInputElement(Document.get(), "number",
                Integer.toString(min), Integer.toString(max)));
        this.addStyleName(IConsts.SPINNERSTYLE);
    }

}
