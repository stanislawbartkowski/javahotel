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
package com.gwtmodel.table;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.binder.ICreateBinderWidget;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
public class GWidget implements IGWidget {

	private final Widget w;
	private final BinderWidget bw;

	@Inject
	private static ICreateBinderWidget iBinder;

	public GWidget(Widget w, BinderWidget bw) {
		assert w != null : LogT.getT().widgetCanotbeNull();
		assert bw == null || (bw != null && w instanceof HTMLPanel);
		this.w = w;
		this.bw = bw;
	}

	public GWidget(Widget w) {
		this(w, null);
	}

	@Override
	public Widget getGWidget() {
		return w;
	}

	@Override
	public void completeWidget() {
		if (bw == null)
			return;
		iBinder.buildHTMLPanel((HTMLPanel) w, bw);
	}

}
