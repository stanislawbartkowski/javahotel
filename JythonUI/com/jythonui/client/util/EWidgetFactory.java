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
package com.jythonui.client.util;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

/**
 * @author hotel
 *
 */
class EWidgetFactory {
    
    private EWidgetFactory() {      
    }
  
    static IFormLineView construct(IVField v) {
        EditWidgetFactory eFactory = GwtGiniInjector.getI().getEditWidgetFactory();
        return eFactory.constructEditWidget(v);
    }

}
