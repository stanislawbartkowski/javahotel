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
package com.javahotel.client.idialog;

import com.javahotel.client.ifield.ILineField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetDialogWidgetFactory {

    private GetDialogWidgetFactory() {
    }

    public static Widget createLine(final String label, final ILineField eLine) {
        HorizontalPanel h = new HorizontalPanel();
        h.add(new Label(label));
        h.add(eLine.getMWidget().getWidget());
        return h;
    }
}
