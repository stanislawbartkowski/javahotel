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
package com.javahotel.client.dialog.user;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.idialog.GetIEditFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestCalculator extends Composite {
    
    public TestCalculator(final IResLocator rI) {
        ILineField e = GetIEditFactory.getTextCalendard(rI);
        VerticalPanel v = new VerticalPanel();
        v.add(e.getMWidget().getWidget());
        ILineField e1 = GetIEditFactory.getNumberCalculator(rI);
        v.add(e1.getMWidget().getWidget());
        initWidget(v);
    }
    
}
