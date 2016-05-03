/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.controlpanel;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.util.CreateFormView;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IContrButtonView extends IGWidget {

    void setEnable(ClickButtonType actionId, boolean enable);

    void setHidden(ClickButtonType actionId, boolean hidden);

    void setHtml(IGWidget g);

    void fillHtml(HTMLPanel pa);

    void emulateClick(ClickButtonType actionId);

    CreateFormView.IGetButtons construct();
}
