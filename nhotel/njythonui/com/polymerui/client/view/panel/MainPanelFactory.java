/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.polymerui.client.view.panel;

import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.polymerui.client.binder.ICreateBinderWidget;
import com.polymerui.client.util.Utils;

public class MainPanelFactory {

	private ICreateBinderWidget iBinder;

	@Inject
	public MainPanelFactory(ICreateBinderWidget iBinder) {
		this.iBinder = iBinder;
	}

	public IMainPanel produceMainPanel(BinderWidget w, String logoIcon, String version) {
		for (BinderWidget.StyleClass css : w.getStyleList())
			Utils.addStyle(css.getContent(), w.getMap());

		return new MainPanel(iBinder.create(w), logoIcon, version);
	}

}
