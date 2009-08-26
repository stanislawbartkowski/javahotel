/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.controller.onerecordmodif;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class OneRecordModifWidgetFactory {

    private OneRecordModifWidgetFactory() {

    }

    public static IOneRecordModifWidget getWi(final IResLocator rI,
            final IControlClick cl) {
        return new ModifRecordWidget(rI,cl);
    }

    public static IOneRecordModifWidget getTableWi(final IResLocator rI,
            final IControlClick cl) {
        return new TableModifRecordWidget(rI,cl);
    }


}
