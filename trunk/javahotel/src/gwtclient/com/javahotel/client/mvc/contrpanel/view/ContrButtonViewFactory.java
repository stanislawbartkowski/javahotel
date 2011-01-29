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
package com.javahotel.client.mvc.contrpanel.view;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ContrButtonViewFactory {

    private ContrButtonViewFactory() {
    }

    public static IContrButtonView getView(final IResLocator rI,
            final IContrPanel model, final IControlClick co) {
        return new ContrButtonView(rI, model, co,true);
    }

    public static IContrButtonView getViewV(final IResLocator rI,
            final IContrPanel model, final IControlClick co) {
        return new ContrButtonView(rI, model, co,false);
    }
}
