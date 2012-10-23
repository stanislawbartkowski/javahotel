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

import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;

public interface IGwtFormView extends IGWidget {

    void showInvalidate(InvalidateFormContainer errContainer);

    void fillHtml(IGWidget g);

    void setHtmlId(String is, IGWidget g);

    void setButtonList(IContrButtonView cList);

    void changeToTab(String tabId);
}
